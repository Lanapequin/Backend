package edu.eci.cvds.project.repository.porsilas;

import edu.eci.cvds.project.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository {
    User saveUser(User user);
    User findUserById(String id);
    User updateUser(User user);
    List<User> findAllUsers();
    void deleteUserById(String id);
    boolean existsById(String id);
    User findUserByUsername(String username);
    boolean existsByUsername(String username);

}
