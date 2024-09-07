package ulrich_tech.csvforjson.Model;

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
    private String startYear;
    private String endYear;

}
