package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.LaboratoryMongoRepository;
import edu.eci.cvds.project.repository.ReservationMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationMongoRepository reservationRepository;

    @Mock
    private UserMongoRepository userRepository;

    @Mock
    private LaboratoryMongoRepository laboratoryRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Laboratory laboratory;
    private User user;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        laboratory = new Laboratory("1", "Laboratory1", List.of());
        user = new User("100011", "Miguel", "password", List.of(), null);
        reservationDTO = new ReservationDTO(
                "Laboratory1",
                "Miguel",
                LocalDateTime.of(2025, 3, 10, 21, 0),
                LocalDateTime.of(2025, 3, 10, 22, 0),
                "Study session"
        );

        reservation = new Reservation();
        reservation.setId("1");
        reservation.setLaboratoryname(laboratory.getName());
        reservation.setUser(user);
        reservation.setStartDateTime(reservationDTO.getStartDateTime());
        reservation.setEndDateTime(reservationDTO.getEndDateTime());
        reservation.setPurpose(reservationDTO.getPurpose());
        reservation.setStatus(true);
    }

    @Test
    void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        List<Reservation> result = reservationService.getAllReservations();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

//    @Test
//    void testCreateReservation_Success() {
//        when(laboratoryRepository.findByName(reservationDTO.getLabName()))
//                .thenReturn(List.of(laboratory));
//        when(userRepository.findUserByUsername(reservationDTO.getUsername()))
//                .thenReturn(user);
//        when(reservationRepository.findByLaboratory(laboratory))
//                .thenReturn(List.of());
//        when(reservationRepository.save(any(Reservation.class)))
//                .thenReturn(reservation);
//
//        Reservation createdReservation = reservationService.createReservation(reservationDTO);
//
//        assertNotNull(createdReservation);
//        assertEquals(reservation.getPurpose(), createdReservation.getPurpose());
//        verify(reservationRepository, times(1)).save(any(Reservation.class));
//    }

//    @Test
//    void testCreateReservation_LaboratoryNotFound() {
//        when(laboratoryRepository.findLaboratoriesByName(reservationDTO.getLabName());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                reservationService.createReservation(reservationDTO));
//        assertEquals("Laboratory not found", exception.getMessage());
//    }

//    @Test
//    void testCancelReservation_Success() {
//        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
//
//        boolean result = reservationService.cancelReservation("1");
//
//        assertTrue(result);
//        verify(reservationRepository, times(1)).deleteById("1");
//    }
//
//    @Test
//    void testCancelReservation_NotFound() {
//        when(reservationRepository.findById("1")).thenReturn(Optional.empty());
//
//        boolean result = reservationService.cancelReservation("1");
//
//        assertFalse(result);
//        verify(reservationRepository, never()).deleteById(anyString());
//    }

    @Test
    void testGetReservationsInRange() {
        LocalDateTime start = LocalDateTime.of(2025, 3, 10, 18, 0);
        LocalDateTime end = LocalDateTime.of(2025, 3, 10, 22, 0);
        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(start, end))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationService.getReservationsInRange(start, end);

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }

    @Test
    void testIsLaboratoryAvailable_True() {
        when(reservationRepository.findByLaboratoryname(laboratory.getName())).thenReturn(List.of());
        boolean result = reservationService.isLaboratoryAvilable(laboratory, reservation.getStartDateTime(), reservation.getEndDateTime());
        assertTrue(result);
    }

    @Test
    void testIsLaboratoryAvailable_False() {
        when(reservationRepository.findByLaboratoryname(laboratory.getName())).thenReturn(List.of(reservation));
        boolean result = reservationService.isLaboratoryAvilable(laboratory, reservation.getStartDateTime(), reservation.getEndDateTime());
        assertFalse(result);
    }

    @Test
    void testIsReservationAvailable_True() {
        reservation.setEndDateTime(LocalDateTime.now().plusHours(1));
        assertTrue(reservationService.isReservationAvailable(reservation));
    }

    @Test
    void testIsReservationAvailable_False() {
        reservation.setEndDateTime(LocalDateTime.now().minusHours(1));
        assertFalse(reservationService.isReservationAvailable(reservation));
    }
}
