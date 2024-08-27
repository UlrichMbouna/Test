package ulrich_tech.csvforjson.Services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CsvToJsonService {

    // Création d'une instance d'ObjectMapper pour convertir les données en JSON.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convertit un fichier CSV en une chaîne JSON.
     * 
     * @param file Le fichier MultipartFile contenant les données CSV.
     * @return Une chaîne JSON représentant les données du fichier CSV.
     * @throws IOException  Si une erreur d'entrée/sortie se produit lors de la
     *                      lecture du fichier.
     * @throws CsvException Si une erreur se produit lors de la lecture du CSV.
     */
    public String convertCsvToJson(MultipartFile file) throws IOException, CsvException {
        // Lire les données CSV à partir du fichier MultipartFile.
        List<String[]> csvData = readCsv(file.getInputStream());
        // Liste pour stocker les données JSON.
        List<Map<String, String>> jsonData = new ArrayList<>();

        // Si les données CSV sont vides, retourner un tableau JSON vide.
        if (csvData.isEmpty()) {
            return "[]";
        }

        // Obtenir les en-têtes des colonnes à partir de la première ligne du CSV.
        String[] headers = csvData.get(0);
        // Parcourir chaque ligne du CSV, en commençant par la deuxième ligne.
        for (int i = 1; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            // Créer une map pour représenter une ligne JSON.
            Map<String, String> jsonRow = new java.util.HashMap<>();
            // Associer chaque valeur de la ligne à son en-tête correspondant.
            for (int j = 0; j < headers.length; j++) {
                jsonRow.put(headers[j], row[j]);
            }
            // Ajouter la ligne JSON à la liste des données JSON.
            jsonData.add(jsonRow);
        }

        // Convertir la liste des données JSON en une chaîne JSON.
        return objectMapper.writeValueAsString(jsonData);
    }

    /**
     * Lit un fichier CSV à partir d'un InputStream et retourne les données sous
     * forme de liste de tableaux de chaînes.
     * 
     * @param inputStream Le flux d'entrée contenant les données CSV.
     * @return Une liste de tableaux de chaînes représentant les lignes du fichier
     *         CSV.
     * @throws IOException  Si une erreur d'entrée/sortie se produit lors de la
     *                      lecture du fichier.
     * @throws CsvException Si une erreur se produit lors de la lecture du CSV.
     */
    private List<String[]> readCsv(InputStream inputStream) throws IOException, CsvException {
        // Utilisation d'un CSVReader pour lire le fichier CSV.
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Lire toutes les lignes du fichier CSV.
            return reader.readAll();
        }
    }

    public String convertJsonToCsv(String json) throws IOException {
        // Lire les données JSON à partir de la chaîne.
        List<Map<String, String>> jsonData = objectMapper.readValue(
                json, new TypeReference<List<Map<String, String>>>() {
                });

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


    private String[] getHeaders(List<Map<String, String>> jsonData) {
        if (jsonData.isEmpty()) {
            return new String[0];
        }
        return jsonData.get(0).keySet().toArray(new String[0]);
    }
}
