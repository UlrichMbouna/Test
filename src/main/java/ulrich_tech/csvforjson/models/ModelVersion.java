package ulrich_tech.csvforjson.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelVersion {
    private int versionId;
    private String model;
    private String version;
    private int startYear;
    private int endYear;

}
