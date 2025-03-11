package edu.eci.cvds.project.service;

import edu.eci.cvds.project.model.DTO.LaboratoryDTO;
import edu.eci.cvds.project.model.Laboratory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServicesLab {
    List<Laboratory> getAllLaboratories();
    Optional<Laboratory> getLaboratoryById(String id);
    Laboratory saveLaboratory(LaboratoryDTO laboratoryDTO);
    boolean isLaboratoryAvailable(Laboratory laboratory, LocalDateTime localDateTime);
}
