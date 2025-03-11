package edu.eci.cvds.project.repository.porsilas;
import edu.eci.cvds.project.model.Laboratory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface LaboratoryRepository {
//    List<Laboratory> findByName(String name);

    List<Laboratory> findByNameContainingIgnoreCase(String name);



}
