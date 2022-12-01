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
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.*;
import static softuni.exam.constans.Paths.JSON_CITIES_PATH;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ValidationUtils validationUtils;
    private final Gson gson;
    private final ModelMapper modelMapper;


    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(JSON_CITIES_PATH);
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();
        ImportCitiesDTO[] importCitiesDTOS = this.gson.fromJson(json, ImportCitiesDTO[].class);
        return Arrays.stream(importCitiesDTOS).map(this::importCity).collect(Collectors.joining("\n"
        ));
    }

    private String importCity(ImportCitiesDTO importCityDTO) {

        boolean isValid = this.validationUtils.isValid(importCityDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_CITY;
        } else {
            Optional<City> optCity = this.cityRepository.findByCityName(importCityDTO.getCityName());

            if (optCity.isEmpty()) {
                long country_id = importCityDTO.getCountry();
                City city = this.modelMapper.map(importCityDTO, City.class);
                Country country = this.countryRepository.findById(country_id);
                city.setCountry(country);
                this.cityRepository.save(city);
                result = String.format(VALID_CITY_FORMAT, city.getCityName(), city.getPopulation());
            } else {
                result = INVALID_CITY;
            }
        }
        return result;
    }
}
