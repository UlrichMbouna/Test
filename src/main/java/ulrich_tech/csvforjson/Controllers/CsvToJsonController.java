package ulrich_tech.csvforjson.Controllers;
// controller en minuscule

import ulrich_tech.csvforjson.Model.ModelVersion;
import ulrich_tech.csvforjson.Services.CsvToJsonService;
import ulrich_tech.csvforjson.Services.ModelVersionService;

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
@RequestMapping("/api")
public class CsvToJsonController {

    @Autowired
    private CsvToJsonService csvToJsonService;
    @Autowired
    private ModelVersionService modelVersionService;

    @PostMapping("/csv-for-json")
    public String convertCsvToJson(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        return csvToJsonService.convertCsvToJson(file);
    }
     

     @PostMapping("/json-for-csv")
    public ResponseEntity<String> convertJsonToCsv(@RequestParam("file") MultipartFile file) throws IOException {
        String json = new String(file.getBytes());
        String csv = csvToJsonService.convertJsonToCsv(json);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping("/model-version")
    public List<ModelVersion> diviserListe(@RequestBody JsonNode jsonNode){
        return modelVersionService.Liste(jsonNode);
    }
}
