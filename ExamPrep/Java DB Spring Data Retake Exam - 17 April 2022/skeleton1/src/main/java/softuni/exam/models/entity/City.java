package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
@Getter
@Setter

public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "city_name",nullable = false,unique = true)
    private String cityName;


    @Column(columnDefinition =  "TEXT")
    private String description;

    @Column(nullable = false)
    private int population;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private Country country;



}
