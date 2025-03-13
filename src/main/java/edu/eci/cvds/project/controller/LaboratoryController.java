package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.model.DTO.LaboratoryDTO;
import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.service.ServicesLab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar laboratorios.
 */
@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {

    @Autowired
    private ServicesLab laboratoryService;

    /**
     * Obtiene la lista de todos los laboratorios.
     * @return Lista de laboratorios.
     */
    @GetMapping
    public List<Laboratory> getAllLaboratories() {
        return laboratoryService.getAllLaboratories();
    }

    /**
     * Obtiene un laboratorio por su ID.
     * @param id Identificador del laboratorio.
     * @return ResponseEntity con el laboratorio encontrado o un estado 404 si no existe.
     */
    @GetMapping("id/{id}")
    public ResponseEntity<Laboratory> getLaboratoryById(@PathVariable String id) {
        Optional<Laboratory> laboratory = laboratoryService.getLaboratoryById(id);
        if (laboratory.isPresent()) {
            return ResponseEntity.ok(laboratory.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Laboratory> getLaboratoryByName(@PathVariable String name) {
        Laboratory laboratory = laboratoryService.getLaboratoryByName(name);
        if (laboratory != null) {
            return ResponseEntity.ok(laboratory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo laboratorio.
     * @param laboratoryDTO Objeto Laboratory recibido en la solicitud.
     * @return ResponseEntity con el laboratorio creado.
     */
    @PostMapping("/create")
    public ResponseEntity<Laboratory> createLaboratory(@RequestBody LaboratoryDTO laboratoryDTO) {
        Laboratory created = laboratoryService.saveLaboratory(laboratoryDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Verifica la disponibilidad de un laboratorio en una fecha y hora específica.
     * @param id Identificador del laboratorio.
     * @param dateTimeString Fecha y hora en formato de cadena.
     * @return ResponseEntity con un mensaje indicando si está disponible o no.
     */
    @GetMapping("avaiable/{id}")
    public ResponseEntity<String> checkLaboratoryAvailability(@PathVariable String id, @RequestParam("dateTime") String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Optional<Laboratory> laboratoryOpt = laboratoryService.getLaboratoryById(id);

        if (laboratoryOpt.isEmpty()) {
            return new ResponseEntity<>("Laboratory not found", HttpStatus.NOT_FOUND);
        }

        Laboratory laboratory = laboratoryOpt.get();
        boolean available = laboratoryService.isLaboratoryAvailable(laboratory, dateTime);

        if (available) {
            return new ResponseEntity<>("Laboratory is available", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Laboratory is not available", HttpStatus.CONFLICT);
        }
    }
}
