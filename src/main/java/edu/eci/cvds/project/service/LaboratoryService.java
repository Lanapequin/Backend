package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.repository.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public List<Laboratory> getAllLaboratories() {
        return laboratoryRepository.findAll();
    }

    public Optional<Laboratory> getLaboratoryById(String id) {
        return laboratoryRepository.findById(id);
    }
    public Laboratory saveLaboratory(Laboratory laboratory) {
        return laboratoryRepository.save(laboratory);
    }

}
