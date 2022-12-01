package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportApartmentDTO;
import softuni.exam.models.dto.ImportApartmentRoot;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.INVALID_APARTMENT;
import static softuni.exam.constans.Messages.VALID_APARTMENT_FORMAT;
import static softuni.exam.constans.Paths.XML_APARTMENTS;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final TownRepository townRepository;
    private final Unmarshaller unmarshaller;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository, ValidationUtils validationUtils, ModelMapper modelMapper) throws JAXBException {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;

        JAXBContext context = JAXBContext.newInstance(ImportApartmentRoot.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(XML_APARTMENTS);
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        ImportApartmentRoot apartmentDTOs = (ImportApartmentRoot) this.unmarshaller.unmarshal(new FileReader(XML_APARTMENTS.toFile()));
        return apartmentDTOs.getApartments().stream().map(this::importApartment).collect(Collectors.joining("\n"));
    }

    private String importApartment(ImportApartmentDTO importApartmentDTO) {
        boolean isValid = this.validationUtils.isValid(importApartmentDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_APARTMENT;
        } else {
            Optional<Town> townOptional = this.townRepository.findByTownName(importApartmentDTO.getTown());
            Optional<Apartment> apartmentInDataBase =
                    this.apartmentRepository.findApartmentByTownAndArea
                            (townOptional.get(), importApartmentDTO.getArea());

            if (apartmentInDataBase.isPresent()) {
                result = INVALID_APARTMENT;

            } else {
                Apartment apartment = this.modelMapper.map(importApartmentDTO, Apartment.class);
                apartment.setTown(townOptional.get());
                this.apartmentRepository.save(apartment);
                result = String.format(VALID_APARTMENT_FORMAT, apartment.getApartmentType(), apartment.getArea());
            }
        }
        return result;
    }
}
