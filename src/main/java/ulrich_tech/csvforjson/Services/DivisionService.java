package ulrich_tech.csvforjson.Services;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ulrich_tech.csvforjson.Model.Division;

@AllArgsConstructor
//@NoArgsConstructor
@Service
public class DivisionService {

    //@Autowired
    //private DivisionService divisionService ;
    
    public Division diviser(String text) {
        int a = text.indexOf(" ");
        int b = text.lastIndexOf('(');
        int c = text.lastIndexOf(')');
    
    
        String b1 = text.substring(0, a).trim();
        String b2 = text.substring(a + 1, b).trim();
        String b3 = text.substring(b + 1, c).trim();
    
        int firstYearStart = b3.indexOf('.') + 1;
        int firstYearEnd = firstYearStart + 4;
        int secondYearStart = b3.indexOf('-') + 2;
    
        String D1 = b3.substring(firstYearStart, firstYearEnd);
    
        String D2 = "N/A";
    
        // Vérifier si une deuxième année est présente et extraire si disponible
        if (secondYearStart > 1 && secondYearStart + 4 <= b3.length()) {
            D2 = b3.substring(secondYearStart, secondYearStart + 4);
        } else {
            D2 = "N/A"; // Changer de "ras" à "N/A" pour la cohérence
        }
    
        Division division = new Division(b1, b2, D1, D2);
    
        return division;
    }
    
    

    public List<Division> diviserListe(JsonNode jsonNode) {

        List <String> names = new ArrayList<>();
        // Parcourir chaque modèle
        for (JsonNode model : jsonNode.get("models")) {
            // Parcourir chaque option de chaque modèle
            for (JsonNode option : model.get("options")) {
                // Ajouter la valeur de "name" à la liste
                names.add(option.get("name").asText());
            }
        }


        return names.stream().map(this::diviser).collect(Collectors.toList());
    }

    
}
