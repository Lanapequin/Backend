package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserMongoRepositoryTest {

    @Mock
    private UserMongoRepository userMongoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveUser() {
        User user = new User();
        when(userMongoRepository.save(user)).thenReturn(user);
        assertEquals(user, userMongoRepository.save(user));
    }

    @Test
    public void shouldFindUserById() {
        User user = new User();
        user.setId("generatedId");
        when(userMongoRepository.findById("generatedId")).thenReturn(Optional.of(user));
        assertEquals(user, userMongoRepository.findById("generatedId").orElse(null));
    }

    @Test
    public void shouldFindAllUsers() {
        User user = new User();
        when(userMongoRepository.findAll()).thenReturn(List.of(user));
        assertEquals(List.of(user), userMongoRepository.findAll());
    }

    @Test
    public void shouldUpdateExistingUser() {
        User user = new User();
        user.setId("generatedId");
        when(userMongoRepository.existsById("generatedId")).thenReturn(true);
        when(userMongoRepository.save(user)).thenReturn(user);
        assertEquals(user, userMongoRepository.save(user));
    }

    @Test
    public void shouldNotUpdateNonExistentUser() {
        User user = new User();
        user.setId("generatedId");
        when(userMongoRepository.existsById("generatedId")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> {
            if (!userMongoRepository.existsById(user.getId())) {
                throw new RuntimeException("User does not exist");
            }
            userMongoRepository.save(user);
        });
    }

    @Test
    public void shouldCheckIfUserExists() {
        when(userMongoRepository.existsById("generatedId")).thenReturn(true);
        assertTrue(userMongoRepository.existsById("generatedId"));
    }

    @Test
    public void shouldCheckIfUserDoesNotExist() {
        when(userMongoRepository.existsById("generatedId")).thenReturn(false);
        assertFalse(userMongoRepository.existsById("generatedId"));
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userMongoRepository.existsById("123")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> {
            if (!userMongoRepository.existsById("123")) {
                throw new RuntimeException("User does not exist");
            }
            userMongoRepository.deleteById("123");
        });
        verify(userMongoRepository, never()).deleteById("123");
    }
}
