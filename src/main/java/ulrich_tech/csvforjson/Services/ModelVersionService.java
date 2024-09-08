package ulrich_tech.csvforjson.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ulrich_tech.csvforjson.constants.UtilConstants;
import ulrich_tech.csvforjson.models.ModelVersion;

@AllArgsConstructor
@Service
public class ModelVersionService {

    private final ObjectMapper objectMapper = new ObjectMapper();

        public ModelVersion diviser(int versionId, String text) {
        int a = text.indexOf(" ");
        int b = text.lastIndexOf('(');
        int c = text.lastIndexOf(')');

        String model = text.substring(0, a).trim();
        String version = text.substring(a + 1, b).trim();
        String b3 = text.substring(b + 1, c).trim();

        int firstYearStart = b3.indexOf('.') + 1;
        int firstYearEnd = firstYearStart + 4;
        int secondYearStart = b3.indexOf('-') + 2;//2
        int sp = b3.indexOf('.',secondYearStart);

        int startYear = Integer.parseInt(  b3.substring(firstYearStart, firstYearEnd) );
        

        int endYear = (sp != -1 && sp + 4 <= b3.length()) ?  Integer.parseInt(  b3.substring(sp+1, sp+5) ) : UtilConstants.DEFAULT_END_YEAR ;

        ModelVersion division = new ModelVersion(versionId, model, version, startYear, endYear);

        return division;
    }

    public List<ModelVersion> convertirEnListe (JsonNode jsonNode) {

        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        // Parcourir chaque modèle
        for (JsonNode model : jsonNode.get("models")) {
            // Parcourir chaque option de chaque modèle
            for (JsonNode option : model.get("options")) {
                // Ajouter la valeur de "name" à la liste
                names.add(option.get("name").asText());
                ids.add(option.get("id").asInt());

            }
        }
        
        List <ModelVersion> resultat = new ArrayList<>() ;
        int i = 0;
        int total= names.size();
        while (i<total) {
            ModelVersion ver = new ModelVersion();
            ver= diviser(ids.get(i), names.get(i));
            resultat.add(ver);
            i++;
        }
        return resultat;
        //return res.stream().map(this::diviser).collect(Collectors.toList());
    }

    public List<ModelVersion> convertirMakeToModelversion (String baseMakeJsonAsString) throws JsonMappingException, JsonProcessingException{

        JsonNode json = objectMapper.readTree(baseMakeJsonAsString);

        List<ModelVersion> listModel = convertirEnListe ( json) ;

        return listModel; 
    }

    public String convertirMakeToModelversionString (String baseMakeJsonAsString) throws JsonMappingException, JsonProcessingException{

        JsonNode json = objectMapper.readTree(baseMakeJsonAsString);
        

        List<ModelVersion> listModel = convertirEnListe ( json) ;

        String jsonString = objectMapper.writeValueAsString(listModel);

        return jsonString; 
    }

}
