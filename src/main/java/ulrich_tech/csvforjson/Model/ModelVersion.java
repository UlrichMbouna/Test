package ulrich_tech.csvforjson.Model;


//package utilitaire fontion
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelVersion {   
   
    private String model;
    private String version;
    private int startYear;  
    private int endYear;  

    public ModelVersion(String b1, String b2, String d1, String d2) {
        
    }
}

