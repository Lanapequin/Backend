package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.exception.UserException;
import edu.eci.cvds.project.exception.UserException;
import edu.eci.cvds.project.service.LoginService;
import edu.eci.cvds.project.service.ServicesLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // Changed from HashMap to Map

@RestController
@RequestMapping("/api/authenticate")
public class LoginController {

    @Autowired
    private ServicesLogin loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        try {
            String token = loginService.loginUser(username, password);
            Cookie cookie = new Cookie("session-token", token);
            cookie.setMaxAge(60 * 60 * 12);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok(token);
        } catch (UserException.UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (UserException.UserIncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown server error");
        }
    }
}