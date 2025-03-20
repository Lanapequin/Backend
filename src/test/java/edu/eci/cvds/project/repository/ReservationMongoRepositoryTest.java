//package edu.eci.cvds.project.repository;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.eci.cvds.project.model.Laboratory;
//import edu.eci.cvds.project.model.Reservation;
//import edu.eci.cvds.project.model.Role;
//import edu.eci.cvds.project.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ReservationMongoRepositoryTest {
//    private ReservationMongoRepository repository;
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private ReservationMongoRepository reservationMongoRepository;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void shouldSaveReservation() {
//        Reservation reservation = new Reservation();
//        when(reservationMongoRepository.save(reservation)).thenReturn(reservation);
//        assertEquals(reservation, reservationMongoRepository.save(reservation));
//    }
//
//    @Test
//    public void shouldFindReservationById() {
//        String id = "123";
//        Reservation reservation = new Reservation();
//        reservation.setId(id);
//
//        when(reservationMongoRepository.findById(id)).thenReturn(Optional.of(reservation));
//
//        Reservation foundReservation = reservationMongoRepository.findReservationById(id);
//
//        assertNotNull(foundReservation);
//        assertEquals(id, foundReservation.getId());
//    }
//
//
//    @Test
//    public void shouldFindReservationsByLaboratory() {
//        Laboratory laboratory = new Laboratory();
//        Reservation reservation = new Reservation();
//        reservation.setLaboratory(laboratory);
//        when(reservationMongoRepository.findByLaboratory(laboratory)).thenReturn(List.of(reservation));
//        assertEquals(List.of(reservation), reservationMongoRepository.findByLaboratory(laboratory));
//    }
//
//    @Test
//    public void shouldFindReservationsAfterStartDateTime() {
//        LocalDateTime now = LocalDateTime.now();
//        Reservation reservation = new Reservation();
//        reservation.setStartDateTime(now.plusDays(1));
//        when(reservationMongoRepository.findByStartDateTimeAfter(now)).thenReturn(List.of(reservation));
//        assertEquals(List.of(reservation), reservationMongoRepository.findByStartDateTimeAfter(now));
//    }
//
//    @Test
//    public void shouldFindReservationsWithinDateRange() {
//        LocalDateTime start = LocalDateTime.now();
//        LocalDateTime end = start.plusDays(5);
//        Reservation reservation = new Reservation();
//        reservation.setStartDateTime(start.plusDays(1));
//        reservation.setEndDateTime(end.minusDays(1));
//        when(reservationMongoRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(start, end))
//                .thenReturn(List.of(reservation));
//        assertEquals(List.of(reservation), reservationMongoRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(start, end));
//    }
//
//    @Test
//    public void shouldDeleteExistingReservationById() {
//        String id = "123";
//        when(reservationMongoRepository.existsById(id)).thenReturn(true);
//
//        reservationMongoRepository.deleteReservationById(id);
//
//        verify(reservationMongoRepository).deleteById(id);  // Asegurar que deleteById se llamó correctamente
//    }
//    @Test
//    void testDeleteTask() {
//        ArrayList<Reservation> reservations=new ArrayList<>();
//        Laboratory laboratory = new Laboratory("1", "Laboratory1", reservations);
//        User user = new User("100011", "Miguel", "password", reservations, Role.USER);
//        LocalDateTime start= LocalDateTime.of(2025, 3, 10, 22, 0);
//        LocalDateTime end=LocalDateTime.of(2025, 3, 10, 23, 0);
//        Reservation  reservation = new Reservation("10222",laboratory,user,start,end,"nose",true);
//
//        repository.delete(reservation);
//
//        assertThrows(RuntimeException.class, () -> repository.findReservationById(reservation.getId()));
//    }
//
//
//    @Test
//    public void shouldThrowExceptionWhenDeletingNonExistentReservationById() {
//        String id = "123";
//
//        when(reservationMongoRepository.existsById(id)).thenReturn(false);
//
//        assertThrows(RuntimeException.class, () -> reservationMongoRepository.deleteReservationById(id));
//    }
//
//}
