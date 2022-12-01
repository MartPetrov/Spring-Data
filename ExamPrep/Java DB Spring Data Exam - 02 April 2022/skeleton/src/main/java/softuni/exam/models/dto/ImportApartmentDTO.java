package softuni.exam.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softuni.exam.models.entity.ApartmentType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportApartmentDTO {

    @XmlElement
    @NotNull
    private ApartmentType apartmentType;

    @XmlElement
    @Min(40)
    @NotNull
    private BigDecimal area;

    @XmlElement
    @NotNull
    private String town;
}
