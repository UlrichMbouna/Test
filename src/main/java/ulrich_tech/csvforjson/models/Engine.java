package ulrich_tech.csvforjson.models;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ulrich_tech.csvforjson.constants.UtilConstants.CarFuelType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Engine {

    private int versionId;
    private int engineId;
    private CarFuelType fuelType;
    private String engineRef;
    private double cylinder;
    private double powerKw;
    private double powerCv;
    private int startYear;
    private int endYear;
}
 
