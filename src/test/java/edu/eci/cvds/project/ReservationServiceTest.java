package edu.eci.cvds.project;

import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.Role;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.LaboratoryMongoRepository;

import edu.eci.cvds.project.repository.ReservationMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;

import edu.eci.cvds.project.service.ReservationService;
import edu.eci.cvds.project.service.ServicesReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationMongoRepository reservationRepository;

    @Mock
    private UserMongoRepository userRepository;
    @Mock
    private LaboratoryMongoRepository laboratoryRepository;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationDTO reservationDTO;
    private Reservation reservation;
    private Laboratory laboratory;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ArrayList<Reservation> reservations=new ArrayList<>();
        LocalDateTime start= LocalDateTime.of(2025, 3, 10, 18, 0);
        LocalDateTime end=LocalDateTime.of(2025, 3, 10, 20, 0);
        Reservation reservations1 = new Reservation("10222",laboratory,user,start,end,"nose",true);
        reservations.add(reservations1);

        laboratory = new Laboratory("1", "Laboratory1", reservations);
        user = new User("100011", "Miguel", "password", reservations, Role.USER);

        reservationDTO = new ReservationDTO(
                "Laboratory1",
                "Miguel",
                LocalDateTime.of(2025, 3, 10, 18, 0),
                LocalDateTime.of(2025, 3, 10, 20, 0),
                "Study session",
                true
        );

        reservation = new Reservation();
        reservation.setId("1");
        reservation.setLaboratory(laboratory);
        reservation.setUser(user);
        reservation.setStartDateTime(reservationDTO.getStartDateTime());
        reservation.setEndDateTime(reservationDTO.getEndDateTime());
        reservation.setPurpose(reservationDTO.getPurpose());
        reservation.setStatus(true);
    }

    @Test
    public void testCreateReservation_Success() {
        when(laboratoryRepository.findByName("Laboratory1")).thenReturn(List.of(laboratory));
        when(userRepository.findUserByUsername("Miguel")).thenReturn(user);
        when(reservationRepository.findByLaboratory(laboratory)).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation resultado = reservationService.createReservation(reservationDTO);

        assertNotNull(resultado);
        assertEquals("Miguel", resultado.getUser().getUsername());
        assertEquals("Laboratory1", resultado.getLaboratory().getName());
    }

    @Test
    public void testCreateReservation_LaboratoryNotAvailable() {
        when(laboratoryRepository.findByName("Laboratory1")).thenReturn(List.of(laboratory));
        when(userRepository.findUserByUsername("Miguel")).thenReturn(user);
        when(reservationRepository.findByLaboratory(laboratory)).thenReturn(List.of(reservation));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(reservationDTO));

        assertEquals("Laboratory is not available for the given time frame.", exception.getMessage());
    }

    @Test
    public void testCancelReservation_Success() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        boolean result = reservationService.cancelReservation("1");

        assertTrue(result);
        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    public void testCancelReservation_NotFound() {
        when(reservationRepository.findById("2")).thenReturn(Optional.empty());

        boolean result = reservationService.cancelReservation("2");

        assertFalse(result);
        verify(reservationRepository, never()).deleteById(anyString());
    }

    @Test
    public void testGetReservationsInRange() {
        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(reservation));

        List<Reservation> resultado = reservationService.getReservationsInRange(
                LocalDateTime.of(2025, 3, 10, 9, 0),
                LocalDateTime.of(2025, 3, 10, 13, 0));

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    public void testIsLaboratoryAvailable_Available() {
        when(reservationRepository.findByLaboratory(laboratory)).thenReturn(List.of());

        boolean result = reservationService.isLaboratoryAvilable(laboratory,
                LocalDateTime.of(2025, 3, 10, 10, 0),
                LocalDateTime.of(2025, 3, 10, 12, 0));

        assertTrue(result);
    }

    @Test
    public void testIsLaboratoryAvailable_NotAvailable() {
        when(reservationRepository.findByLaboratory(laboratory)).thenReturn(List.of(reservation));

        boolean result = reservationService.isLaboratoryAvilable(laboratory,
                LocalDateTime.of(2025, 3, 10, 11, 0),
                LocalDateTime.of(2025, 3, 10, 19, 0));

        assertFalse(result);
    }

    @Test
    public void testIsReservationAvailable_ValidTime() {
        reservation.setEndDateTime(LocalDateTime.now().plusHours(2));

        boolean result = reservationService.isReservationAvailable(reservation);

        assertTrue(result);
    }

    @Test
    public void testIsReservationAvailable_Expired() {
        reservation.setEndDateTime(LocalDateTime.now().minusHours(1));

        boolean result = reservationService.isReservationAvailable(reservation);

        assertFalse(result);
    }

    @Test
    public void testGenerateUniqueId() {
        String id1 = reservationService.generateUniqueId();
        String id2 = reservationService.generateUniqueId();

        assertNotEquals(id1, id2);
    }
}