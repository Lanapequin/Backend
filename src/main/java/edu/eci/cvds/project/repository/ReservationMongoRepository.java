package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;


@Repository
public interface ReservationMongoRepository extends MongoRepository<Reservation, String> {

    /**
     * Encuentra todas las reservas asociadas a un laboratorio específico.
     * @param laboratory El laboratorio a buscar.
     * @return Lista de reservas del laboratorio.
     */
    List<Reservation> findByLaboratory(Laboratory laboratory);

    /**
     * Encuentra todas las reservas que comienzan después de una fecha específica.
     * @param startDateTime Fecha y hora de inicio.
     * @return Lista de reservas encontradas.
     */
    List<Reservation> findByStartDateTimeAfter(LocalDateTime startDateTime);

    /**
     * Encuentra todas las reservas dentro de un rango de fechas.
     * @param start Fecha y hora de inicio.
     * @param end Fecha y hora de fin.
     * @return Lista de reservas en el rango especificado.
     */
    List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(LocalDateTime start, LocalDateTime end);

    /**
     * Obtiene todas las reservas.
     * @return Lista de todas las reservas.
     */
    public default List<Reservation> findAllReservations(){
        return findAll();
    }

    /**
     * Guarda una nueva reserva en la base de datos.
     * Si la reserva no tiene un ID, se genera automáticamente.
     * @param reservation Reserva a guardar.
     * @return La reserva guardada.
     */
    public default Reservation saveReservation(Reservation reservation) {
        reservation.setStatus(false);
        reservation.setEndDateTime(null);
        reservation.setStartDateTime(LocalDateTime.now());
        if(reservation.getId() == null){
            reservation.setId(generateId());
        }
        save(reservation);
        return reservation;
    }

    /**
     * Genera un identificador único para una reserva utilizando UUID.
     * @return ID único generado.
     */
    private String generateId() {
        return UUID.randomUUID().toString();
    }
    /**
     * Verifica si existe una reserva con el ID dado.
     * @param id Identificador de la reserva.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public default boolean existsById(String id) {
        return findById(id).isPresent();
    }
    /**
     * Elimina una reserva por su ID.
     * @param id Identificador de la reserva a eliminar.
     * @throws RuntimeException Si la reserva no se encuentra.
     */
    default void deleteReservationById(String id) {
        if (!existsById(id)) {
            throw new RuntimeException("Reservation not found");
        }
        deleteById(id);
    }
    /**
     * Encuentra una reserva por su ID.
     * @param id Identificador de la reserva.
     * @return La reserva encontrada o null si no existe.
     */
    public default Reservation findReservationById(String id) {
        return findById(id).orElse(null);
    }

    /**
     * Elimina una reserva específica.
     * @param reservation Reserva a eliminar.
     * @throws RuntimeException Si la reserva no se encuentra.
     */
    public default void deleteReservation(Reservation reservation){
        if(!existsById(reservation.getId())){
            throw new RuntimeException("Reservation not found");
        }
        delete(reservation);
    }

}
