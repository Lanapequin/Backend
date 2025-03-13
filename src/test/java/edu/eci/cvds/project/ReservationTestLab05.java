//package edu.eci.cvds.project;
//
//import edu.eci.cvds.project.model.Laboratory;
//import edu.eci.cvds.project.model.Reservation;
//import edu.eci.cvds.project.model.DTO.ReservationDTO;
//import edu.eci.cvds.project.model.Role;
//import edu.eci.cvds.project.model.User;
//import edu.eci.cvds.project.repository.LaboratoryMongoRepository;
//import edu.eci.cvds.project.repository.ReservationMongoRepository;
//import edu.eci.cvds.project.repository.UserMongoRepository;
//
//import edu.eci.cvds.project.service.ReservationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ReservationTestLab05 {
//
//    @Mock
//    private ReservationMongoRepository reservationRepository;
//
//    @Mock
//    private UserMongoRepository userRepository;
//
//    @Mock
//    private LaboratoryMongoRepository laboratoryRepository;
//
//    @InjectMocks
//    private ReservationService reservationService;
//
//    private Reservation reservation;
//    private Laboratory laboratory;
//    private User user;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        laboratory = new Laboratory("1", "Laboratory1", null);
//        user = new User("100011", "Miguel", "lol", null, Role.USER);
//        reservation = new Reservation("111000", laboratory, user,
//                LocalDateTime.of(2025, 3, 1, 10, 0),
//                LocalDateTime.of(2025, 3, 1, 12, 0),
//                "Purpose", true);
//    }
//
//    @Test
//    public void testConsultarReserva_Exitosa() {
//        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
//                any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(List.of(reservation));
//
//        List<Reservation> resultado = reservationService.getReservationsInRange(
//                LocalDateTime.of(2025, 3, 1, 9, 0),
//                LocalDateTime.of(2025, 3, 1, 13, 0));
//
//        assertFalse(resultado.isEmpty());
//        assertEquals("111000", resultado.get(0).getId());
//    }
//
//    @Test
//    public void testGetReservationsInRange_NoReservations() {
//        when(reservationRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
//                any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(List.of());
//
//        List<Reservation> resultado = reservationService.getReservationsInRange(
//                LocalDateTime.of(2025, 3, 1, 9, 0),
//                LocalDateTime.of(2025, 3, 1, 13, 0));
//
//        assertTrue(resultado.isEmpty());
//    }
//
//    @Test
//    public void testCreateReservation_Success() {
//        ReservationDTO reservationDTO = new ReservationDTO();
//        reservationDTO.setLabName("Laboratory1");
//        reservationDTO.setUsername("Miguel");
//        reservationDTO.setStartDateTime(LocalDateTime.now().plusDays(1)); // Fecha en el futuro
//        reservationDTO.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(2)); // Asegurar que el final es futuro
//        reservationDTO.setPurpose("Purpose");
//
////        when(laboratoryRepository.findByName("Laboratory1")).thenReturn(List.of(laboratory));
//        when(userRepository.findUserByUsername("Miguel")).thenReturn(user);
//        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
//
//        Reservation resultado = reservationService.createReservation(reservationDTO);
//
//        assertNotNull(resultado);
//        assertEquals("111000", resultado.getId());
//        assertEquals("Laboratory1", resultado.getLaboratory().getName());
//        assertEquals("Miguel", resultado.getUser().getUsername());
//    }
//
//
//    @Test
//    public void verificacionEliminacionReserva_Exitosa() {
//        String reservationId = reservation.getId();
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
//
//        boolean result = reservationService.cancelReservation(reservationId);
//
//        assertTrue(result, "Reservation should be successfully deleted");
//        verify(reservationRepository, times(1)).deleteById(reservationId);
//    }
//
//    @Test
//    public void verificacionEliminacionReserva_ConsultaExitosa() {
//        String reservationId = reservation.getId();
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
//
//        doNothing().when(reservationRepository).deleteById(reservationId);
//
//        reservationService.cancelReservation(reservationId);
//
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
//
//        Optional<Reservation> result = reservationRepository.findById(reservationId);
//
//        assertFalse(result.isPresent(), "After deletion, the reservation should no longer exist in the repository");
//    }
//}