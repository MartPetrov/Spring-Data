package softuni.exam.models.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "forecasts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Forecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "max_temperature", nullable = false)
    private double maxTemperature;

    @Column(name = "min_temperature", nullable = false)
    private double minTemperature;

    @Column(nullable = false)
    private LocalTime sunrise;

    @Column(nullable = false)
    private LocalTime sunset;

    @ManyToOne(optional = false)
    private City city;


    @Override
    public String toString() {
        return String.format("City: %s:%n" +
                "\t-min temperature: %.2f%n" +
                "\t--max temperature: %.2f%n" +
                "\t---sunrise: %s%n" +
                "\t----sunset: %s",
                this.city.getCityName(),
                this.minTemperature,
                this.maxTemperature,
                this.sunrise,
                this.sunset);
    }


}
