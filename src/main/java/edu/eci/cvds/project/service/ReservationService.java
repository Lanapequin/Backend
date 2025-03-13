package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.DTO.ReservationDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.LaboratoryMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;
import edu.eci.cvds.project.repository.ReservationMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class ReservationService implements ServicesReservation {

    @Autowired
    private ReservationMongoRepository reservationRepository;
    @Autowired
    private UserMongoRepository userRepository;
    @Autowired
    private LaboratoryMongoRepository laboratoryRepository;


    private static final AtomicLong idCounter = new AtomicLong(0);
    @Autowired
    private UserService userService;

    /**
     * Obtiene todas las reservas registradas.
     * @return Lista de reservas.
     */
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }



    /**
     * Crea una nueva reserva basándose en los datos proporcionados en el DTO.
     *
     * @param dto Objeto DTO que contiene la información de la reserva.
     * @return La reserva creada.
     * @throws IllegalArgumentException Si el laboratorio o el usuario no existen,
     * o si la reserva no es válida.
     */
    @Transactional
    @Override
    public Reservation createReservation(ReservationDTO dto) {
        Laboratory lab = Objects.requireNonNull(
                laboratoryRepository.findLaboratoriesByName(dto.getLabName()),
                "Lab not found");

        User user = Objects.requireNonNull(
                userRepository.findUserByUsername(dto.getUsername())
        );

        Reservation reservation = new Reservation();
        reservation.setLaboratoryname(dto.getLabName());
        reservation.setUser(user);
        reservation.setStartDateTime(dto.getStartDateTime());
        reservation.setEndDateTime(dto.getEndDateTime());
        reservation.setPurpose(dto.getPurpose());
        reservation.setStatus(true);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!isLaboratoryAvilable(lab, reservation.getStartDateTime(), reservation.getEndDateTime())) {
            throw new IllegalArgumentException("Laboratory is not available for the given time frame.");
        }

        if (!isReservationAvailable(reservation)) {
            throw new IllegalArgumentException("The reservation is not available because the end time has already passed.");
        }

        return reservationRepository.saveReservation(reservation);
    }

    /**
     * Cancela una reserva dado su ID.
     * @param id Identificador de la reserva.
     * @return true si la reserva fue cancelada, false si no se encontró.
     */
    @Override
    public boolean cancelReservation(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID de la reserva no puede ser null");
        }

        Reservation reservation = reservationRepository.findReservationById(id);
        if (reservation == null) {
            throw new DataIntegrityViolationException("Reservation not found: " + id);
        }

        Laboratory lab = laboratoryRepository.findLaboratoriesByName(reservation.getLaboratoryname());
        if (lab == null) {
            throw new DataIntegrityViolationException("Laboratory not found: " + reservation.getLaboratoryname());
        }

        User user = reservation.getUser();

        boolean removedFromLab = lab.getReservations().removeIf(r -> r.getId().equals(id));
        boolean removedFromUser = user.getReservations().removeIf(r -> r.getId().equals(id));

        reservationRepository.delete(reservation);

        boolean existsAfter = reservationRepository.findReservationById(id) != null;

        laboratoryRepository.save(lab);
        userRepository.save(user);

        return !existsAfter;
    }



    /**
     * Obtiene una lista de reservas dentro de un rango de fechas especificado.
     * @param start Fecha de inicio del rango.
     * @param end Fecha de fin del rango.
     * @return Lista de reservas dentro del rango de fechas.
     */
    @Override
    public List<Reservation> getReservationsInRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(start, end);
    }

    /**
     * Verifica si un laboratorio está disponible dentro de un rango de fechas.
     * @param laboratory Laboratorio a verificar.
     * @param start Fecha y hora de inicio.
     * @param end Fecha y hora de fin.
     * @return true si el laboratorio está disponible, false si está ocupado.
     */
    @Override
    public boolean isLaboratoryAvilable(Laboratory laboratory, LocalDateTime start, LocalDateTime end) {
        List<Reservation> reservations = reservationRepository.findByLaboratoryname(laboratory.getName());

        for (Reservation reservation : reservations) {
            LocalDateTime existingStart = reservation.getStartDateTime();
            LocalDateTime existingEnd = reservation.getEndDateTime();
            if (start.isBefore(existingEnd) && end.isAfter(existingStart)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Verifica si una reserva es válida con respecto al tiempo actual.
     * @param reservation Reserva a validar.
     * @return true si la reserva aún está dentro de su tiempo válido, false si ya ha pasado.
     */
    @Override
    public boolean isReservationAvailable(Reservation reservation) {
        LocalDateTime actualTime =LocalDateTime.now();
        LocalDateTime endTime = reservation.getEndDateTime();
        if(actualTime.isBefore(endTime)){
            return true;
        }else{
            return false;
        }

    }
    // Método para generar un ID único secuencial
    @Override
    public String generateUniqueId() {
        return String.valueOf(idCounter.incrementAndGet());  // Genera un ID secuencial único
    }

    @Transactional
    @Override
    public Reservation updateReservation(Reservation reservation) {
        try {
            if (!reservationRepository.existsById(reservation.getId())) {
                throw new DataIntegrityViolationException("Reservation not found: ");
            }
            Laboratory laboratory =laboratoryRepository.findLaboratoriesByName(reservation.getLaboratoryname());
            laboratory.reservations.add(reservation);
            laboratoryRepository.updateLaboratory(laboratory);
            User user = userRepository.findUserById(reservation.getUser().getId());
            user.reservations.add(reservation);
            userRepository.updateUser(user);
            if(user == null){
                throw new RuntimeException("User not found");
            }
            return reservationRepository.updateReservation(reservation);
        } catch (TransactionSystemException e) {
            throw new TransactionSystemException("Error creating reservation");
        }
    }
}
