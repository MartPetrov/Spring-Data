package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.INVALID_COUNTRY;
import static softuni.exam.constans.Messages.VALID_COUNTRY_FORMAT;
import static softuni.exam.constans.Paths.JSON_COUNTRIES_PATH;


@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ValidationUtils validationUtils;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public CountryServiceImpl(CountryRepository countryRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(JSON_COUNTRIES_PATH);
    }

    @Override
    public String importCountries() throws IOException {
        String json = this.readCountriesFromFile();
        ImportCountryDTO[] importCountryDTOs = this.gson.fromJson(json, ImportCountryDTO[].class);
        return Arrays.stream(importCountryDTOs).map(this::importCountry).collect(Collectors.joining("\n"
        ));

    }

    private String importCountry(ImportCountryDTO importCountryDTO) {
        boolean isValid = this.validationUtils.isValid(importCountryDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_COUNTRY;
        } else {
            Optional<Country> optCountry = this.countryRepository.findByCountryName(importCountryDTO.getCountryName());

            if (optCountry.isEmpty()) {
                Country country = this.modelMapper.map(importCountryDTO, Country.class);
                this.countryRepository.save(country);
                result = String.format(VALID_COUNTRY_FORMAT, country.getCountryName(), country.getCurrency());
            } else {
                result = INVALID_COUNTRY;
            }
        }
        return result;
    }
}
