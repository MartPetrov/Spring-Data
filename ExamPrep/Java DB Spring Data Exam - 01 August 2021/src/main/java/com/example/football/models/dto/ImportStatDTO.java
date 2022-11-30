package com.example.football.models.dto;

import lombok.Getter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatDTO {

    @XmlElement
    @Positive
    private float passing;

    @XmlElement
    @Positive
    private float shooting;

    @XmlElement
    @Positive
    private float endurance;

}
