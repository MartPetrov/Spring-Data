package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.util.LocalTimeAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportForecastDTO {


    @XmlElement(name = "day_of_week")
    @NotNull
    private DayOfWeek dayOfWeek;

    @XmlElement(name = "max_temperature")
    private double maxTemperature;

    @XmlElement(name = "min_temperature")
    private double minTemperature;

    @XmlElement()
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @NotNull
    private LocalTime sunrise;


    @XmlElement()
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @NotNull
    private LocalTime sunset;

    @XmlElement(name = "city")
    private long cityId;
}
