package com.example.football.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private float shooting;

    private float passing;

    private float endurance;


    @Override
    public String toString() {
        return passing + " - " + shooting + " - " +
                endurance
                ;
    }
}
