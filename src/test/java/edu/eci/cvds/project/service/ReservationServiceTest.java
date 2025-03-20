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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        // Asegurar que las listas sean mutables
        laboratory = new Laboratory("1", "Laboratory1", new ArrayList<>());
        user = new User("100011", "Miguel", "password", new ArrayList<>(), null);

        reservationDTO = new ReservationDTO(
                "Laboratory1",
                "Miguel",
                LocalDateTime.of(2025, 3, 10, 21, 0),
                LocalDateTime.of(2025, 3, 10, 22, 0),
                "Study session",
                3
        );

        reservation = new Reservation();
        reservation.setId("1");
        reservation.setLaboratoryname(laboratory.getName());
        reservation.setUsername(user.getUsername());
        reservation.setStartDateTime(reservationDTO.getStartDateTime());
        reservation.setEndDateTime(reservationDTO.getEndDateTime());
        reservation.setPurpose(reservationDTO.getPurpose());
        reservation.setStatus(true);

        laboratory.getReservations().add(reservation);
        user.getReservations().add(reservation);
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
//    void testCreateReservation_LaboratoryNotFound() {
//        when(laboratoryRepository.findLaboratoriesByName(reservationDTO.getLabName());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                reservationService.createReservation(reservationDTO));
//        assertEquals("Laboratory not found", exception.getMessage());
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

//    @Test
//    void shouldUpdateReservationSuccessfully() {
//        when(reservationRepository.existsById("1")).thenReturn(true);
//        when(laboratoryRepository.findLaboratoriesByName("Laboratory1")).thenReturn(laboratory);
//        when(userRepository.findUserById("100011")).thenReturn(user);
//        when(reservationRepository.updateReservation(any(Reservation.class))).thenReturn(reservation);
//
//        Reservation updatedReservation = reservationService.updateReservation(reservation);
//
//        assertNotNull(updatedReservation);
//        assertEquals("1", updatedReservation.getId());
//        assertTrue(laboratory.getReservations().contains(reservation));
//        assertTrue(user.getReservations().contains(reservation));
//
//        verify(laboratoryRepository).updateLaboratory(laboratory);
//        verify(userRepository).updateUser(user);
//        verify(reservationRepository).updateReservation(reservation);
//    }

    @Test
    void shouldGenerateUniqueIdSequentially() {
        ReservationService service = new ReservationService();

        String id1 = service.generateUniqueId();
        String id2 = service.generateUniqueId();
        String id3 = service.generateUniqueId();

        assertEquals("1", id1);
        assertEquals("2", id2);
        assertEquals("3", id3);
    }

    @Test
    void shouldThrowExceptionIfReservationDoesNotExist() {
        when(reservationRepository.existsById("1")).thenReturn(false);

        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> reservationService.updateReservation(reservation));

        assertEquals("Reservation not found: ", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionIfTransactionFails() {
        when(reservationRepository.existsById("1")).thenReturn(true);
        when(laboratoryRepository.findLaboratoriesByName("Laboratory1")).thenReturn(laboratory);
        when(userRepository.findUserById("100011")).thenReturn(user);
        when(reservationRepository.updateReservation(reservation))
                .thenThrow(new TransactionSystemException("Database error"));

        Exception exception = assertThrows(TransactionSystemException.class,
                () -> reservationService.updateReservation(reservation));

        assertEquals("Error creating reservation", exception.getMessage());
    }

}