package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public boolean cancelReservation(String id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Reservation> getReservationsInRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(start, end);
    }
}
