package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.service.ServicesReservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private ServicesReservation reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    public void testCreateReservation_Success() {
        ReservationDTO reservationDTO = new ReservationDTO();
        Reservation reservation = new Reservation();
        when(reservationService.createReservation(reservationDTO)).thenReturn(reservation);

        ResponseEntity<?> response = reservationController.createReservation(reservationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
        verify(reservationService, times(1)).createReservation(reservationDTO);
    }

    @Test
    public void testCreateReservation_Error() {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.createReservation(reservationDTO)).thenThrow(new RuntimeException("Error creating reservation"));

        ResponseEntity<?> response = reservationController.createReservation(reservationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Error creating reservation", response.getBody());
        verify(reservationService, times(1)).createReservation(reservationDTO);
    }

    @Test
    public void testCancelReservation_Success() {
        when(reservationService.cancelReservation("1")).thenReturn(true);

        ResponseEntity<Void> response = reservationController.cancelReservation("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).cancelReservation("1");
    }

    @Test
    public void testCancelReservation_NotFound() {
        when(reservationService.cancelReservation("2")).thenReturn(false);

        ResponseEntity<Void> response = reservationController.cancelReservation("2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).cancelReservation("2");
    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        when(reservationService.getAllReservations()).thenReturn(reservations);

        List<Reservation> result = reservationController.getAllReservations();

        assertEquals(reservations, result);
        verify(reservationService, times(1)).getAllReservations();
    }
}
