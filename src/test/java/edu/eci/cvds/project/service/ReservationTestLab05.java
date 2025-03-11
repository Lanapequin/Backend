package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Role;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.LaboratoryMongoRepository;
import edu.eci.cvds.project.repository.ReservationMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;

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

public class ReservationTestLab05 {

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        laboratory = new Laboratory("1", "Laboratory1", null);
        user = new User("100011", "Miguel", "lol", null, Role.USER);
        user.setReservations(new ArrayList<>()); // Initialize the reservations list here
        reservation = new Reservation("111000", laboratory, user,
                LocalDateTime.of(2025, 3, 1, 10, 0),
                LocalDateTime.of(2025, 3, 1, 12, 0),
                "Purpose", true);
    }

    @Test
    public void testConsultarReserva_Exitosa() {
        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(reservation));

        List<Reservation> resultado = reservationService.getReservationsInRange(
                LocalDateTime.of(2025, 3, 1, 9, 0),
                LocalDateTime.of(2025, 3, 1, 13, 0));

        assertFalse(resultado.isEmpty());
        assertEquals("111000", resultado.get(0).getId());
    }

    @Test
    public void testGetReservationsInRange_NoReservations() {
        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<Reservation> resultado = reservationService.getReservationsInRange(
                LocalDateTime.of(2025, 3, 1, 9, 0),
                LocalDateTime.of(2025, 3, 1, 13, 0));

        assertTrue(resultado.isEmpty());
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


    @Test
    public void verificacionEliminacionReserva_Exitosa() {
        String reservationId = reservation.getId();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        boolean result = reservationService.cancelReservation(reservationId);

        assertTrue(result, "Reservation should be successfully deleted");
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    public void verificacionEliminacionReserva_ConsultaExitosa() {
        String reservationId = reservation.getId();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        doNothing().when(reservationRepository).deleteById(reservationId);

        reservationService.cancelReservation(reservationId);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        Optional<Reservation> result = reservationRepository.findById(reservationId);

        assertFalse(result.isPresent(), "After deletion, the reservation should no longer exist in the repository");
    }
}