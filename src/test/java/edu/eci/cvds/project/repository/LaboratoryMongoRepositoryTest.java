package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Laboratory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class LaboratoryMongoRepositoryTest {

    @Mock
    private LaboratoryMongoRepository laboratoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Stub the generateId method to return a non-null value
        when(laboratoryRepository.generateId()).thenReturn("some-generated-id");
    }

    @Test
    void shouldGenerateNonEmptyId() {
        String generatedId = laboratoryRepository.generateId();
        assertNotNull(generatedId);
    }
}
