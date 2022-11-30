package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportForecastRoot;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static softuni.exam.constans.Paths.XML_FORECAST;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final Unmarshaller unmarshaller;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private  final DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;
    private  final int population = 150000;


    @Autowired
    public ForecastServiceImpl(
            ForecastRepository forecastRepository,
            CityRepository cityRepository,
            Validator validator,
            ModelMapper modelMapper) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;


        JAXBContext context = JAXBContext.newInstance(ImportForecastRoot.class);
        this.unmarshaller = context.createUnmarshaller();
    }
    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(XML_FORECAST));
    }

    private String importForecast(ImportForecastDTO importForecastDTO) {
        Set<ConstraintViolation<ImportForecastDTO>> errors = this.validator.validate(importForecastDTO);
        if (!errors.isEmpty()) {
            return "Invalid forecast";
        }


        Optional<Forecast> byCityAndDayOfWeek = this.forecastRepository.findAllByCity_IdAndDayOfWeek(importForecastDTO.getCityId(), importForecastDTO.getDayOfWeek());
        Optional<City> city = this.cityRepository.findById(importForecastDTO.getCityId());
        if(importForecastDTO.getDayOfWeek() == null) {
            return "Invalid forecast";
        }
        if (byCityAndDayOfWeek.isPresent() || city.isEmpty()) {
            return "Invalid forecast";
        } else {
            Forecast forecast = this.modelMapper.map(importForecastDTO, Forecast.class);
            forecast.setCity(city.get());
            this.forecastRepository.save(forecast);
            return String.format("Successfully imported %s – %s", forecast.getDayOfWeek(),forecast.getMaxTemperature());
        }

    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRoot forecastDTOs = (ImportForecastRoot) this.unmarshaller.unmarshal(new FileReader(Path.of(XML_FORECAST).toAbsolutePath().toString()));
        return forecastDTOs.getForecasts().stream().map(this::importForecast).collect(Collectors.joining("\n"));
    }

    @Override
    public String exportForecasts() {

        Optional<List<Forecast>> forecastsOpt =
                this.forecastRepository
                        .findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(dayOfWeek, population);

        List<Forecast> forecasts = forecastsOpt.orElseThrow(NoSuchElementException::new);

        return forecasts
                .stream()
                .map(Forecast::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
