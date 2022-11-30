package softuni.exam.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class ImportCountryDTO {

    @Size(min = 2,max = 60)
    private String countryName;

    @Size(min = 2,max = 20)
    private String currency;
}
