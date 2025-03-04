package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findByLaboratoryId(String laboratoryId);

    List<Reservation> findByUser(String user);

    List<Reservation> findByStartDateTimeAfter(LocalDateTime startDateTime);

    List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
            LocalDateTime start, LocalDateTime end);
}
