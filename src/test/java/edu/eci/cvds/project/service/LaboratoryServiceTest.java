package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.DTO.LaboratoryDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.repository.LaboratoryMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LaboratoryServiceTest {

    @Mock
    private LaboratoryMongoRepository laboratoryRepository;

    @InjectMocks
    private LaboratoryService laboratoryService;

    private Laboratory laboratory;
    private LaboratoryDTO laboratoryDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        laboratory = new Laboratory("1", "Lab A", null);
        laboratoryDTO = new LaboratoryDTO("Lab A");
    }

    @Test
    public void testGetAllLaboratories() {
        when(laboratoryRepository.findAll()).thenReturn(List.of(laboratory));

        List<Laboratory> resultado = laboratoryService.getAllLaboratories();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Lab A", resultado.get(0).getName());
    }

    @Test
    public void testGetLaboratoryById_Found() {
        when(laboratoryRepository.findById("1")).thenReturn(Optional.of(laboratory));

        Optional<Laboratory> resultado = laboratoryService.getLaboratoryById("1");

        assertTrue(resultado.isPresent());
        assertEquals("Lab A", resultado.get().getName());
    }

    @Test
    public void testGetLaboratoryById_NotFound() {
        when(laboratoryRepository.findById("2")).thenReturn(Optional.empty());

        Optional<Laboratory> resultado = laboratoryService.getLaboratoryById("2");

        assertFalse(resultado.isPresent());
    }

//    @Test
//    public void testSaveLaboratory_Success() {
//        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(laboratory);
//
//        Laboratory resultado = laboratoryService.saveLaboratory(laboratoryDTO);
//
//        assertNotNull(resultado);
//        assertEquals("Lab A", resultado.getName());
//    }

    @Test
    public void testIsLaboratoryAvailable_Available() {
        laboratory.setReservations(List.of());

        boolean resultado = laboratoryService.isLaboratoryAvailable(laboratory,
                LocalDateTime.of(2025, 3, 10, 10, 0));

        assertTrue(resultado);
    }

    @Test
    public void testIsLaboratoryAvailable_NotAvailable() {
        Reservation reservation = new Reservation();
        reservation.setStartDateTime(LocalDateTime.of(2025, 3, 10, 9, 0));
        reservation.setEndDateTime(LocalDateTime.of(2025, 3, 10, 11, 0));

        laboratory.setReservations(List.of(reservation));

        boolean resultado = laboratoryService.isLaboratoryAvailable(laboratory,
                LocalDateTime.of(2025, 3, 10, 10, 0));

        assertFalse(resultado);
    }
}