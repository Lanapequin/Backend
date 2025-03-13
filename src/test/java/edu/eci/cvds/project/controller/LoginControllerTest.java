package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.exception.UserException;
import edu.eci.cvds.project.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testLogin_Success() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "testpassword");
        String token = "testtoken";
        when(loginService.loginUser("testuser", "testpassword")).thenReturn(token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = loginController.login(credentials, response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(token, responseEntity.getBody());
        assertEquals(token, response.getCookie("session-token").getValue());
        assertEquals(60 * 60 * 12, response.getCookie("session-token").getMaxAge());
    }

    @Test
    public void testLogin_UserNotFound() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "nonexistentuser");
        credentials.put("password", "testpassword");
        when(loginService.loginUser("nonexistentuser", "testpassword")).thenThrow(new UserException.UserNotFoundException("User not found!"));

        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = loginController.login(credentials, response);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    public void testLogin_IncorrectPassword() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "wrongpassword");
        when(loginService.loginUser("testuser", "wrongpassword")).thenThrow(new UserException.UserIncorrectPasswordException("wrongpassword"));

        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = loginController.login(credentials, response);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Incorrect password", responseEntity.getBody());
    }

    @Test
    public void testLogin_UnknownError() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "testpassword");
        when(loginService.loginUser("testuser", "testpassword")).thenThrow(new RuntimeException("Unknown error"));

        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = loginController.login(credentials, response);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Unknown server error", responseEntity.getBody());
    }
}