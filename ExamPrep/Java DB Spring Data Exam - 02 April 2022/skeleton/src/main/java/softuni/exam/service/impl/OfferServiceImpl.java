package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOffer;
import softuni.exam.models.dto.ImportOfferRoot;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.constans.Messages.INVALID_OFFER;
import static softuni.exam.constans.Messages.VALID_OFFER_FORMAT;
import static softuni.exam.constans.Paths.XML_OFFERS;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ApartmentRepository apartmentRepository;
    private final AgentRepository agentRepository;
    private final Unmarshaller unmarshaller;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;

    public OfferServiceImpl(OfferRepository offerRepository, ApartmentRepository apartmentRepository, AgentRepository agentRepository, ValidationUtils validationUtils, ModelMapper modelMapper) throws JAXBException {
        this.offerRepository = offerRepository;
        this.apartmentRepository = apartmentRepository;
        this.agentRepository = agentRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;

        JAXBContext context = JAXBContext.newInstance(ImportOfferRoot.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(XML_OFFERS);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        ImportOfferRoot offerRoot = (ImportOfferRoot) this.unmarshaller.unmarshal(new FileReader(XML_OFFERS.toFile()));
        return offerRoot.getOffers().stream().map(this::importOffer)
                .collect(Collectors.joining("\n"));
    }

    private String importOffer(ImportOffer importOffer) {
        boolean isValid = this.validationUtils.isValid(importOffer);
        String result = "";
        Optional<Agent> agentOptional = this.agentRepository.findByFirstName(importOffer.getAgent().getName());
        if (agentOptional.isEmpty() || !isValid) {
            result = INVALID_OFFER;
        } else {
            Offer offer = this.modelMapper.map(importOffer, Offer.class);
            Optional<Apartment> apartment = this.apartmentRepository.findById(importOffer.getApartment().getId());

            offer.setAgent(agentOptional.get());
            offer.setApartment(apartment.get());

            this.offerRepository.save(offer);
            result = String.format(VALID_OFFER_FORMAT, offer.getPrice());
        }

        return result;
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        List<Offer> offerListThreeRooms = offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.three_rooms);

        offerListThreeRooms
                .forEach(offer -> {
                    sb.append(String.format("Agent %s %s with offer №%d:%n" +
                                            "   -Apartment area: %.2f%n" +
                                            "   --Town: %s%n" +
                                            "   ---Price: %.2f$",
                                    offer.getAgent().getFirstName(),
                                    offer.getAgent().getLastName(),
                                    offer.getId(),
                                    offer.getApartment().getArea(),
                                    offer.getApartment().getTown().getTownName(),
                                    offer.getPrice()))
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }
}
