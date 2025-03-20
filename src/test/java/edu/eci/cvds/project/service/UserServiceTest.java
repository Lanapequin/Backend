package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.DTO.UserDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.model.Role;

import edu.eci.cvds.project.repository.ReservationMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMongoRepository userRepository;

    @Mock
    private ReservationMongoRepository reservationRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private Reservation reservation;
    private Laboratory laboratory;


    @BeforeEach
    void setUp() {
        ArrayList<Reservation> reservations=new ArrayList<>();
        laboratory = new Laboratory("1", "Laboratory1", reservations);
        user = new User("100011", "Miguel", "password", reservations, Role.USER);
        LocalDateTime start= LocalDateTime.of(2025, 3, 10, 22, 0);
        LocalDateTime end=LocalDateTime.of(2025, 3, 10, 23, 0);
        reservation = new Reservation("10222",laboratory.getName(),user.getUsername(),start,end,"nose",true,4);
        reservations.add(reservation);

        userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());



    }

    @Test
    void testSaveUser() {
        when(userRepository.saveUser(any(User.class))).thenReturn(user);

        User savedUser = userService.save(userDTO);

        assertNotNull(savedUser);
        assertEquals("Miguel", savedUser.getUsername());
        assertEquals(Role.USER, savedUser.getRole());

        verify(userRepository, times(1)).saveUser(any(User.class));
    }


    @Test
    void testGetUserById() {
        when(userRepository.findUserById("100011")).thenReturn(user);

        User foundUser = userService.getUserById("100011");

        assertNotNull(foundUser);
        assertEquals("Miguel", foundUser.getUsername());

        verify(userRepository, times(1)).findUserById("100011");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteUserById("100011");

        assertDoesNotThrow(() -> userService.deleteUser("100011"));

        verify(userRepository, times(1)).deleteUserById("100011");
    }

//    @Test
//    void testGetAllReservationByUserId_UserExists() {
//        when(userRepository.existsById("100011")).thenReturn(true);
//        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));
//
//        List<Reservation> reservations = userService.getAllReservationByUserId("100011");
//
//        assertNotNull(reservations);
//        assertEquals("Miguel", reservations.get(0).getUser().getUsername());
//
//        verify(userRepository, times(1)).existsById("100011");
//        verify(reservationRepository, times(1)).findAll();
//    }
//    @Test
//    void testGetAllReservationByUserId_UserExists() {
//        // Simular que findById devuelve un usuario con las reservas
//        when(userRepository.findById("100011")).thenReturn(Optional.of(user));
//
//        // Simular que reservationRepository devuelve las reservas del usuario
//        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));
//
//        // Llamada al servicio
//        List<Reservation> reservations = userService.getAllReservationByUserId("100011");
//
//        // Verificación de que la lista de reservas tiene un solo elemento
//        assertEquals(1, reservations.size());
//        assertEquals("Miguel", reservations.get(0).getUser().getUsername());
//
//        // Verificación de las interacciones con los repositorios
//        verify(userRepository, times(1)).findById("100011");
//        verify(reservationRepository, times(1)).findAll();  // Aunque no se use directamente aquí, depende de tu implementación
//    }
//
//    @Test
//    void testGetAllReservationByUserId_UserNotExists() {
//        when(userRepository.existsById("1")).thenReturn(false);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAllReservationByUserId("1"));
//
//        assertEquals("User not found", exception.getMessage());
//
//        verify(userRepository, times(0)).existsById("1");
//        verify(reservationRepository, never()).findAllReservations();

    @Test
    void testGetUserByUsername() {
        when(userRepository.findUserByUsername("Miguel")).thenReturn(user);
        User foundUser = userService.getUserByUsername("Miguel");
        assertNotNull(foundUser);
        assertEquals("Miguel", foundUser.getUsername());
        verify(userRepository, times(1)).findUserByUsername("Miguel");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAllUsers()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllUser();

        assertEquals(1, users.size());
        assertEquals("Miguel", users.get(0).getUsername());

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void testUpdateUser() {
        List<Reservation> updatedReservations = new ArrayList<>();
        User updatedUser = new User("100011", "Miguel", "newpassword", updatedReservations, Role.ADMIN);

        when(userRepository.saveUser(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("Miguel", result.getUsername());
        assertEquals(Role.ADMIN, result.getRole());
        assertEquals("newpassword", result.getPassword());
        assertEquals(updatedReservations, result.getReservations());

        verify(userRepository, times(1)).saveUser(updatedUser);
    }

    @Test
    void testGetAllReservationByUserId_UserExists() {
        when(userRepository.findById("100011")).thenReturn(Optional.of(user));

        List<Reservation> reservations = userService.getAllReservationByUserId("100011");

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));

        verify(userRepository, times(1)).findById("100011");
    }

    @Test
    void testGetAllReservationByUserId_UserNotExists() {
        when(userRepository.findById("99999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAllReservationByUserId("99999"));

        assertEquals("Usuario no encontrado con ID: 99999", exception.getMessage());

        verify(userRepository, times(1)).findById("99999");
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(userDTO);
        });
        assertEquals("User already exists", exception.getMessage());
    }
}