package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.util.LocalTimeAdapter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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

    @NotNull
    @XmlElement(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @NotNull
    @XmlElement(name = "max_temperature")
    @DecimalMin("-20")
    @DecimalMax("60")
    private double maxTemperature;

    @DecimalMin("-50")
    @DecimalMax("40")
    @NotNull
    @XmlElement(name = "min_temperature")
    private double minTemperature;

    @NotNull
    @XmlElement()
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime sunrise;

    @NotNull
    @XmlElement()
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime sunset;

    @NotNull
    @XmlElement(name = "city")
    private long cityId;
}
