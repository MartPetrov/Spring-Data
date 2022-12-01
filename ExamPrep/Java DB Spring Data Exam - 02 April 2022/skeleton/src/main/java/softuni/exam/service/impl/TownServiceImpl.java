package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.INVALID_TOWN;
import static softuni.exam.constans.Messages.VALID_TOWN_FORMAT;
import static softuni.exam.constans.Paths.JSON_TOWNS_PATH;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ValidationUtils validationUtils;
    private final Gson gson;
    private final ModelMapper modelMapper;


    public TownServiceImpl(TownRepository townRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(JSON_TOWNS_PATH);
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(json, ImportTownDTO[].class);
        return Arrays.stream(importTownDTOS).map(this::importTown).collect(Collectors.joining("\n"
        ));
    }

    private String importTown(ImportTownDTO importTownDTO) {
        boolean isValid = this.validationUtils.isValid(importTownDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_TOWN;
        } else {
            Optional<Town> optTown = this.townRepository.findByTownName(importTownDTO.getTownName());

            if (optTown.isEmpty()) {
                Town town = this.modelMapper.map(importTownDTO, Town.class);
                this.townRepository.save(town);
                result = String.format(VALID_TOWN_FORMAT, town.getTownName(), town.getPopulation());
            } else {
                result = INVALID_TOWN;
            }
        }
        return result;
    }
}
