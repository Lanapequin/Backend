package edu.eci.cvds.project.service;
import edu.eci.cvds.project.model.DTO.LaboratoryDTO;
import edu.eci.cvds.project.model.DTO.UserDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.repository.porsilas.ReservationRepository;
import edu.eci.cvds.project.repository.porsilas.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements ServicesUser {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Guarda un nuevo usuario en el sistema.
     * @param userdto DTO que contiene la información del usuario.
     * @return El usuario guardado.
     */
    @Override
    public User save(UserDTO userdto) {
        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setRole(userdto.getRole());
        return userRepository.saveUser(user);
    }

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
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        List<Reservation> reservations = reservationRepository.findAllReservations();
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getUser().equals(id)) {
                userReservations.add(reservation);
            }
        }
        return userReservations;
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username Nombre de usuario.
     * @return El usuario correspondiente al nombre de usuario.
     */
    @Override
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
