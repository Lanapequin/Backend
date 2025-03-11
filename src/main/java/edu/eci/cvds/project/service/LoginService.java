package edu.eci.cvds.project.service;

import com.auth0.jwt.algorithms.Algorithm;
import edu.eci.cvds.project.exception.UserExcepion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import edu.eci.cvds.project.model.User;

import java.time.LocalDate;

@Service
public class LoginService {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String loginUser(String username, String password) throws UserExcepion.UserNotFoundException, UserExcepion.UserIncorrectPasswordException {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UserExcepion.UserNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserExcepion.UserIncorrectPasswordException("Incorrect password");
        }
        String token = JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().toString())
                .sign(Algorithm.HMAC256("secret"));
        return token;
    }


}
