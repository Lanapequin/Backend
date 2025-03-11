package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Laboratory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class LaboratoryMongoRepositoryTest {

    @Autowired
    private LaboratoryMongoRepository laboratoryRepository;

    @BeforeEach
    void setUp() {
        laboratoryRepository.deleteAll();

        Laboratory lab1 = new Laboratory("1", "Physics Lab", List.of());
        Laboratory lab2 = new Laboratory("2", "Chemistry Lab", List.of());
        Laboratory lab3 = new Laboratory("3", "Advanced Physics Lab", List.of());

        laboratoryRepository.saveAll(List.of(lab1, lab2, lab3));
    }

    @Test
    void shouldFindLaboratoryByExactName() {
        List<Laboratory> result = laboratoryRepository.findByName("Physics Lab");

        assertEquals(1, result.size());
        assertEquals("Physics Lab", result.get(0).getName());
    }

    @Test
    void shouldFindLaboratoriesByNameContainingIgnoreCase() {
        List<Laboratory> result = laboratoryRepository.findByNameContainingIgnoreCase("physics");

        assertFalse(result.isEmpty());

        boolean foundPhysicsLab = false;
        boolean foundAdvancedPhysicsLab = false;
        for (Laboratory lab : result) {
            if (lab.getName().equals("Physics Lab")) {
                foundPhysicsLab = true;
            }
            if (lab.getName().equals("Advanced Physics Lab")) {
                foundAdvancedPhysicsLab = true;
            }
        }
        assertTrue(foundPhysicsLab);
        assertTrue(foundAdvancedPhysicsLab);
    }

    @Test
    void shouldGenerateNonEmptyId() {
        String generatedId = laboratoryRepository.generateId();

        assertNotNull(generatedId);
    }
}