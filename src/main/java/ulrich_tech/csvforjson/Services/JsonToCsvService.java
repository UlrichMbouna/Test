package ulrich_tech.csvforjson.Services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class JsonToCsvService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convertit une chaîne JSON en CSV.
     *
     * @param json Le JSON sous forme de chaîne.
     * @return Le contenu CSV sous forme de chaîne.
     * @throws IOException Si une erreur se produit lors de la conversion.
     */
    public String convertJsonToCsv(String json) throws IOException {
        // Lire les données JSON à partir de la chaîne.
        List<Map<String, String>> jsonData = objectMapper.readValue(
                json, new TypeReference<List<Map<String, String>>>() {});

        // Créer un écrivain pour le CSV.
        StringWriter csvWriter = new StringWriter();
        try (CSVPrinter csvPrinter = new CSVPrinter(csvWriter, CSVFormat.DEFAULT.withHeader(getHeaders(jsonData)))) {
            // Écrire chaque ligne du JSON en tant que ligne CSV.
            for (Map<String, String> row : jsonData) {
                csvPrinter.printRecord(row.values());
            }
        }

        return csvWriter.toString();
    }

    /**
     * Obtenir les en-têtes de colonnes à partir des données JSON.
     *
     * @param jsonData Les données JSON.
     * @return Un tableau de chaînes représentant les en-têtes des colonnes.
     */
    private String[] getHeaders(List<Map<String, String>> jsonData) {
        if (jsonData.isEmpty()) {
            return new String[0];
        }
        return jsonData.get(0).keySet().toArray(new String[0]);
    }
}
