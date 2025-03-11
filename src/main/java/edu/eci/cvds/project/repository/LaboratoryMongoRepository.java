package edu.eci.cvds.project.repository;

import edu.eci.cvds.project.model.Laboratory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface LaboratoryMongoRepository extends MongoRepository<Laboratory, String> {
    /**
     * Busca los laboratorios por nombre exacto.
     * @param name el nombre exacto del laboratorio.
     * @return una lista de laboratorios que coinciden con el nombre proporcionado.
     */
    List<Laboratory> findByName(String name);

    /**
     * Busca los laboratorios cuyo nombre contiene la cadena proporcionada, ignorando mayúsculas y minúsculas.
     * @param name el fragmento del nombre del laboratorio a buscar.
     * @return una lista de laboratorios cuyos nombres contienen la cadena proporcionada.
     */
    List<Laboratory> findByNameContainingIgnoreCase(String name);

    /**
     * Método privado para generar un identificador único utilizando {@link UUID}.
     * Este método crea un identificador único al azar para ser usado en la creación de nuevas instancias
     * de laboratorio. El valor generado es una cadena de caracteres representando un UUID.
     * @return un identificador único generado de forma aleatoria como una cadena de texto.
     */
    default String generateId() {
        return UUID.randomUUID().toString();
    }

}
