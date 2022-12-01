package softuni.exam.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
public class ImportApartmentRoot {

    @XmlElement(name = "apartment")
    private List<ImportApartmentDTO> apartments;
}
