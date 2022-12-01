package softuni.exam.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

@NoArgsConstructor
@Getter
public class ImportCitiesDTO {



    @Size(min = 2, max = 60)
    private String cityName;
    @Size(min = 2)
    private String description;
    @Min(500)
    private int population;

    @NotNull
    @XmlElement(name = "country_id")
    private long country;
}
