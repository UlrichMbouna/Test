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
import ulrich_tech.csvforjson.constants.UtilConstants.CarFuelType;
import ulrich_tech.csvforjson.models.Engine;
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

    public String convertirVersionEnginesToEngines( String baseVersionEnginesJsonAsString ){

        JsonNode enginesAsJson ;

        try{
         enginesAsJson = objectMapper.readTree(baseVersionEnginesJsonAsString);
        } catch (Exception e){
            return "is not json";
        }

        List<Engine> list = new ArrayList<Engine>();

        int versionId = 0;
        int engineId = 0;
        String fuel = "";
        String name = "";
        for(JsonNode node: enginesAsJson){
            versionId = node.get("version").asInt();
            for(JsonNode vehicle: node.get("vehicles")){
                fuel = vehicle.get("label").asText();
                for(JsonNode option: vehicle.get("options")){
                    engineId = option.get("id").asInt();
                    name = option.get("name").asText();
                    System.out.println(versionId + " - " + engineId + " - " + fuel + " - " + name);
                    Engine engine = convertNameToEngine(versionId, engineId, fuel, name);
                    list.add(engine);
                }
            }
        }

        String jsonString;
        try{
        jsonString = objectMapper.writeValueAsString(list);
         } catch (Exception e){
        return "impossible de convertir";
        }


        return jsonString; 

    }

    private Engine convertNameToEngine(int versionId, int engineId, String fuel, String name){
         
        int r2 = name.indexOf("(");
        int r3 = name.lastIndexOf("(");
        
        String refAndCyl = name.substring(0, r2-1);
        String power = name.substring(r2, r3-2);

        int r11 = refAndCyl.lastIndexOf(" ");
        int r21 = power.indexOf("K");
        int r22 = power.indexOf("/");
        int r23 = power.indexOf("C");
        String engineRef = "";
        double cylinder = UtilConstants.DEFAULT_CYLINDER;

        if(r11 == -1){
            engineRef = refAndCyl;
        }
        else {
            engineRef = refAndCyl.substring(0, r11);
            try {
                cylinder = Double.parseDouble(refAndCyl.substring(r11+1));
            }
            catch(Exception e){
                engineRef = refAndCyl;
            }
        }
        double powerKw = Double.parseDouble(name.substring(r2+1, r2+r21-1));
        double powerCv = Double.parseDouble(name.substring(r2+r22+2, r2+r23-1));
        int startYear = Integer.parseInt(name.substring(r3+4, r3+8));
        int endYear = UtilConstants.DEFAULT_END_YEAR;
        //endYear = name.charAt(r3+11) == '.' ? UtilsConstants.DEFAULT_END_YEAR : Integer.parseInt(name.substring(r3+14, r3+18));
        try {
            endYear = Integer.parseInt(name.substring(r3+14, r3+18));
        } catch(Exception e ){

        }

        CarFuelType fuelType = CarFuelType.OTHER;

        switch (fuel) {
            case "Essence":{
                fuelType = CarFuelType.PETROL;
                break;
            }
                
            case "Essence/électrique":{
                fuelType = CarFuelType.PETROL_ELECTRIC;
                break;
            }
            case "Diesel":{
                fuelType = CarFuelType.DIESEL;
                break;
            }
            case "Diesel/électrique":{
                fuelType = CarFuelType.DIESEL_ELECTRIC;
                break;
            }
            default:
                    break;
        }
        return new Engine(versionId, engineId, fuelType, engineRef, cylinder, powerKw, powerCv, startYear, endYear);
    }
}
