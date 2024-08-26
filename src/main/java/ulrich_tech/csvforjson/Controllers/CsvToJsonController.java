package ulrich_tech.csvforjson.Controllers;

import ulrich_tech.csvforjson.Services.CsvToJsonService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@RequestMapping("/api/csv")
public class CsvToJsonController {

    @Autowired
    private CsvToJsonService csvToJsonService;

    @PostMapping("/CsvForJson")
    public String convertCsvToJson(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        return csvToJsonService.convertCsvToJson(file);
    }
}
