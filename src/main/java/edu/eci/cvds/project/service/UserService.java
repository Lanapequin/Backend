package edu.eci.cvds.project.service;
import edu.eci.cvds.project.exception.UserException;
import edu.eci.cvds.project.model.DTO.LaboratoryDTO;
import edu.eci.cvds.project.model.DTO.UserDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.ReservationMongoRepository;
import edu.eci.cvds.project.repository.UserMongoRepository;

import edu.eci.cvds.project.repository.porsilas.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements ServicesUser {
    @Autowired
    private UserMongoRepository userRepository;
    @Autowired
    private ReservationMongoRepository reservationRepository;

    /**
     * Guarda un nuevo usuario en el sistema.
     * @param userdto DTO que contiene la información del usuario.
     * @return El usuario guardado.
     */
    @Override
    public User save(UserDTO userdto) {
        if(userRepository.existsByUsername(userdto.getUsername())){
            throw new IllegalArgumentException("User already exists");}
        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(userdto.getPassword());
        user.setRole(userdto.getRole());
        user.setReservations(new ArrayList<Reservation>());
        return userRepository.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        user.setReservations(user.getReservations());
        return userRepository.saveUser(user);
    }
//    public void saveReservation(Reservation reservation){
//        User user = reservation.getUser();
//        if (user == null) {
//            System.out.println("User is null");
//        }
//        user.reservations.add(reservation);
//        userRepository.updateUser(user);
//    }
//public void saveReservation(Reservation reservation) {
//    User user = reservation.getUser();
//    if (user == null) {
//        System.out.println("User is null");
//        return; // Salir si el usuario es nulo.
//    }
//
//    if(user.getReservations()==null){
//        user.setReservations(new ArrayList<Reservation>());
//
//    }
//    user.reservations.add(reservation);
//    // Guardar al usuario con la reserva añadida (actualizar las reservas del usuario)
//    userRepository.updateUser(user);
//}


    /**
     * Obtiene un usuario por su identificador.
     * @param id Identificador del usuario.
     * @return El usuario correspondiente al ID.
     */
    @Override
    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    /**
     * Elimina un usuario por su identificador.
     * @param id Identificador del usuario a eliminar.
     */
    @Override
    public void deleteUser(String id) {
        userRepository.deleteUserById(id);
    }

    /**
     * Obtiene todas las reservas asociadas a un usuario específico.
     * @param id Identificador del usuario.
     * @return Lista de reservas del usuario.
     * @throws RuntimeException Si el usuario no existe.
     */
    @Override
    public List<Reservation> getAllReservationByUserId(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Retornar las reservas del usuario
            return user.getReservations();
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }

    }
//    @Override
//    public List<Reservation> getAllReservationByUserId(String id) {
//        if (!userRepository.existsById(id)) {
//            throw new RuntimeException("User not found");
//        }
//        List<Reservation> reservations = reservationRepository.findAllReservations();
//        List<Reservation> userReservations = new ArrayList<>();
//        for (Reservation reservation : reservations) {
//            if (reservation.getUser().equals(id)) {
//                userReservations.add(reservation);
//
//            }
//        }
//        return userReservations;
//
//    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username Nombre de usuario.
     * @return El usuario correspondiente al nombre de usuario.
     */
    @Override
    // En tu UserService
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return Lista de todos los usuarios.
     */
    @Override
    public List<User> getAllUser() {
        return userRepository.findAllUsers();
    }
}
