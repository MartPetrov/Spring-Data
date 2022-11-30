package com.example.football.models.dto;

import com.example.football.models.entity.PlayerPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
public class ImportPlayerDTO {

    @Size(min = 2)
    @XmlElement(name = "first-name")
    private String firstName;

    @Size(min = 2)
    @XmlElement(name = "last-name")
    private String lastName;

    @Email
    @XmlElement
    private String email;

    @XmlElement(name = "birth-date")
    private String birthDate;

    @XmlElement
    private PlayerPosition position;

    @XmlElement(name = "town")
    private NameDTO town;

    @XmlElement(name = "team")
    private NameDTO team;

    @XmlElement(name = "stat")
    private StatIdDTO stat;
}
