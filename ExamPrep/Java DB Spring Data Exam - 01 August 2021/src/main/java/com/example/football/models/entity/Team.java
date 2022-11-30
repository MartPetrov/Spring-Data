package com.example.football.models.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "stadium_name",nullable = false)
    private String stadiumName;

    @Column(name ="fan_base", nullable = false)
    private int fanBase;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String history;

    @ManyToOne(optional = false)
    private Town town;

    @OneToMany(targetEntity = Player.class, mappedBy = "team")
    private Set<Player> players;


    @Override
    public String toString() {
        return  name + " - " +
                 fanBase;
    }
}
