package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.service.ServicesReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar reservas.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ServicesReservation reservationService;

    /**
     * Crea una nueva reserva.
     * @param reservationDTO Objeto Reservation recibido en la solicitud.
     * @return La reserva creada.
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation reservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    /**
     * Cancela una reserva existente.
     * @param id Identificador de la reserva a cancelar.
     * @return ResponseEntity con estado 204 si se cancela correctamente o 404 si no se encuentra.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable String id) {
        boolean cancelled = reservationService.cancelReservation(id);
        if (cancelled) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtiene la lista de todas las reservas.
     * @return Lista de reservas.
     */
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
