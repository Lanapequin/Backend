package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Laboratory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {
    List<Laboratory> findByName(String name);

    List<Laboratory> findByNameContainingIgnoreCase(String name);
}
