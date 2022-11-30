package softuni.exam.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class ImportCitiesDTO {



    @Size(min = 2, max = 60)
    private String cityName;
    @Size(min = 2)
    private String description;
    @Min(500)
    private int population;

    private long country;
}
