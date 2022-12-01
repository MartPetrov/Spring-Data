package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentDTO;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.*;
import static softuni.exam.constans.Paths.JSON_AGENTS_PATH;
import static softuni.exam.constans.Paths.JSON_TOWNS_PATH;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final TownRepository townRepository;
    private final ValidationUtils validationUtils;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;

    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(JSON_AGENTS_PATH);
    }

    @Override
    public String importAgents() throws IOException {
        String json = this.readAgentsFromFile();
        ImportAgentDTO[] importAgentDTOS = this.gson.fromJson(json, ImportAgentDTO[].class);
        return Arrays.stream(importAgentDTOS).map(this::importAgent).collect(Collectors.joining("\n"
        ));
    }

    private String importAgent(ImportAgentDTO importAgentDTO) {
        boolean isValid = this.validationUtils.isValid(importAgentDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_AGENT;
        } else {
            Optional<Agent> optAgent = this.agentRepository.findByFirstName(importAgentDTO.getFirstName());
            Optional<Town> townByTownName = this.townRepository.findByTownName(importAgentDTO.getTown());
            if (optAgent.isEmpty() && townByTownName.isPresent()) {
                Agent agent = this.modelMapper.map(importAgentDTO, Agent.class);
                townByTownName.ifPresent(agent::setTown);
                this.agentRepository.save(agent);
                result = String.format(VALID_AGENT_FORMAT, agent.getFirstName(), agent.getLastName());
            } else {
                result = INVALID_AGENT;
            }
        }
        return result;
    }
}
