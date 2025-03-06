package edu.eci.cvds.project.controller;

import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    @GetMapping
    public List<Laboratory> getAllLaboratories() {
        return laboratoryService.getAllLaboratories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLaboratoryById(@PathVariable String id) {
        Optional<Laboratory> laboratory = laboratoryService.getLaboratoryById(id);
        if (laboratory.isPresent()) {
            return ResponseEntity.ok(laboratory.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Laboratory> createLaboratory(@RequestBody Laboratory laboratory) {
        Laboratory created = laboratoryService.saveLaboratory(laboratory);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}/availability")
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

