package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCitiesDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final Validator validator;
    private final Gson gson;
    private final ModelMapper modelMapper;


    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, Validator validator, Gson gson, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.validator = validator;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cities.json");
        return Files.readString(path);
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();
        ImportCitiesDTO[] importCitiesDTOS = this.gson.fromJson(json, ImportCitiesDTO[].class);
        return Arrays.stream(importCitiesDTOS).map(this::importCity).collect(Collectors.joining("\n"
        ));
    }

    private String importCity(ImportCitiesDTO importCitiesDTO) {
        Set<ConstraintViolation<ImportCitiesDTO>> errors = this.validator.validate(importCitiesDTO);

        if (!errors.isEmpty()) {
            return "Invalid city";
        }

        Optional<City> optCountry =  this.cityRepository.findByCityName(importCitiesDTO.getCityName());
        if (optCountry.isPresent()) {
            return "Invalid city";
        } else {
            City city = this.modelMapper.map(importCitiesDTO, City.class);
            Country country =  this.countryRepository.findById(importCitiesDTO.getCountry());
            city.setCountry(country);
            this.cityRepository.save(city);
            return String.format("Successfully imported %s – %s", city.getCityName(),city.getPopulation());
        }
    }
}
