package ulrich_tech.csvforjson.controllers;

import ulrich_tech.csvforjson.models.ModelVersion;
import ulrich_tech.csvforjson.services.DataConvService;
import ulrich_tech.csvforjson.services.ModelVersionService;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api")
public class CsvToJsonController {

    @Autowired
    private DataConvService dataConvService;
    @Autowired
    private ModelVersionService modelVersionService;

    @PostMapping("/csv-for-json")
    public String convertCsvToJson(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        return dataConvService.convertCsvToJson(file);
    }

    @PostMapping("/json-for-csv")
    public ResponseEntity<String> convertJsonToCsv(@RequestParam("file") MultipartFile file) throws IOException {

        String json = new String(file.getBytes(), "UTF-8");
        // System.out.println("entree = " + row );

        String csv = dataConvService.convertJsonToCsv(json);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping("/model-version")
    public List<ModelVersion> diviserListe(@RequestBody JsonNode jsonNode) {

        return modelVersionService.convertirEnListe(jsonNode);
    }

    @PostMapping("/make-json-to-model-version-csv")
    public ResponseEntity<String> convertMakeJsonToCsv(@RequestParam("file") MultipartFile file) throws IOException {

        String baseMakeJsonAsString = new String(file.getBytes(), "UTF-8");
        //System.out.println("baseVersion:" + baseMakeJsonAsString);

        String listModel = modelVersionService.convertirMakeToModelversionString(baseMakeJsonAsString);

        String stringCsv = dataConvService.convertJsonToCsv(listModel);
        //String stringCsv = "hello";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(stringCsv);
    }
    
   /* @PostMapping("/version-json-to-version-version-csv")
    public ResponseEntity<String> convertMakeJsonToCsv(@RequestParam("file") MultipartFile file) throws IOException {

        String baseMakeJsonAsString = new String(file.getBytes(), "UTF-8");

        String listModel = modelVersionService.convertirMakeToModelversionString(baseMakeJsonAsString);

        String stringCsv = dataConvService.convertJsonToCsv(listModel);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(stringCsv);
    } */
   @PostMapping("/version-engines-json-to-engines-csv")
    public ResponseEntity<String> convertVersionEngines(@RequestParam("file") MultipartFile file) throws IOException {

        String baseVersionEnginesJsonAsString = new String(file.getBytes(), "UTF-8");
        //System.out.println("baseVersion:" + baseVersionEnginesJsonAsString);

        String listEngines = modelVersionService.convertirVersionEnginesToEngines(baseVersionEnginesJsonAsString);

        String stringCsv = dataConvService.convertJsonToCsv(listEngines);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(stringCsv);
    } 
}
