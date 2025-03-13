package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.model.DTO.UserDTO;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.Role;
import edu.eci.cvds.project.model.User;
import edu.eci.cvds.project.service.ServicesUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controlador REST para gestionar usuarios.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private ServicesUser userService;

    /**
     * Obtiene la lista de todos los usuarios.
     * @return Lista de usuarios.
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

//    /**
//     * Crea un nuevo usuario con rol de usuario estándar.
//     * @param user Objeto User recibido en la solicitud.
//     * @return ResponseEntity con el usuario creado o un error en caso de fallo.
//     */
//    @PostMapping("/create")
//    public ResponseEntity<?> saveUser(@RequestBody User user) {
//        HashMap<String, String> response;
//        try {
//            user.setId(null);
//            user.setRole(Role.USER);
//            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
//        } catch (Exception e) {
//            response = new HashMap<>();
//            response.put("error", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    /**
//     * Crea un nuevo usuario con rol de administrador.
//     * @param user Objeto User recibido en la solicitud.
//     * @return ResponseEntity con el usuario creado o un error en caso de fallo.
//     */
//    @PostMapping("/create/admin")
//    public ResponseEntity<?> saveAdmin(@RequestBody User user) {
//        HashMap<String, String> response;
//        try {
//            user.setId(null);
//            user.setRole(Role.ADMIN);
//            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
//        } catch (Exception e) {
//            response = new HashMap<>();
//            response.put("error", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
    /**
     * Crea un nuevo usuario con rol de usuario estándar.
     * @param userDTO Objeto UserDTO recibido en la solicitud.
     * @return ResponseEntity con el usuario creado o un error en caso de fallo.
     */
    @PostMapping("/create")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        HashMap<String, String> response;
        try {
            userDTO.setRole(Role.USER);
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDTO));
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Crea un nuevo usuario con rol de administrador.
     * @param userDTO Objeto UserDTO recibido en la solicitud.
     * @return ResponseEntity con el usuario creado o un error en caso de fallo.
     */
    @PostMapping("/create/admin")
    public ResponseEntity<?> saveAdmin(@RequestBody UserDTO userDTO) {
        HashMap<String, String> response;
        try {
            userDTO.setRole(Role.ADMIN);
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDTO));
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id Identificador del usuario.
     * @return ResponseEntity con el usuario encontrado o un error si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        HashMap<String, String> response;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        HashMap<String, String> response;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la traza de la excepción en la consola
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Elimina un usuario por su ID.
     * @param id Identificador del usuario a eliminar.
     * @return ResponseEntity con confirmación de eliminación o un error en caso de fallo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        HashMap<String, String> response;
        try {
            userService.deleteUser(id);
            response = new HashMap<>();
            response.put("user-delete", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Obtiene todas las reservas asociadas a un usuario por su ID.
     * @param id Identificador del usuario.
     * @return ResponseEntity con la lista de reservas o un error en caso de fallo.
     */
    @GetMapping("/getReservations/{id}")
    public ResponseEntity<?> getAllReservationByUserId(@PathVariable String id) {
        HashMap<String, String> response;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllReservationByUserId(id));
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateReservation(@RequestBody User user) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(user));
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
