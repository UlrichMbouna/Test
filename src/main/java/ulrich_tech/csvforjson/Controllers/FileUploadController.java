package ulrich_tech.csvforjson.Controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ulrich_tech.csvforjson.Services.JsonToCsvService;

import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
//@RequestMapping("/api")
public class FileUploadController {

    private final JsonToCsvService jsonToCsvService;

    public FileUploadController(JsonToCsvService jsonToCsvService) {
        this.jsonToCsvService = jsonToCsvService;
    }

    /**
     * Endpoint pour télécharger un fichier JSON et obtenir un fichier CSV.
     *
     * @param file Le fichier JSON téléchargé.
     * @return Une réponse contenant le fichier CSV.
     * @throws IOException Si une erreur se produit lors de la lecture ou de la conversion.
     */
    @PostMapping("/JsonForCsv")
    public ResponseEntity<String> convertJsonToCsv(@RequestParam("file") MultipartFile file) throws IOException {
        String json = new String(file.getBytes());
        String csv = jsonToCsvService.convertJsonToCsv(json);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.csv\"");

        return ResponseEntity.ok().headers(headers).body(csv);
    }
}
