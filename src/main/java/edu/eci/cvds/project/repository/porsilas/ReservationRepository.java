package edu.eci.cvds.project.repository.porsilas;


import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface ReservationRepository {

    Reservation saveReservation(Reservation reservation);
    Reservation findReservationById(String id);
    List<Reservation> findAllReservations();
    void deleteReservation(Reservation reservation);
    void deleteReservationById(String id);
    Reservation updateReservation(Reservation reservation);
    boolean existsById(String id);
    List<Reservation> findByLaboratory(Laboratory laboratory);
    List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(LocalDateTime start, LocalDateTime end);



}
