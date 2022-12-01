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

import static softuni.exam.constans.Messages.*;
import static softuni.exam.constans.Paths.XML_FORECAST;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final Unmarshaller unmarshaller;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;




    @Autowired
    public ForecastServiceImpl(
            ForecastRepository forecastRepository,
            CityRepository cityRepository,
            ValidationUtils validationUtils,
            ModelMapper modelMapper) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.validationUtils = validationUtils;
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
        return Files.readString(XML_FORECAST);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRoot forecastDTOs = (ImportForecastRoot) this.unmarshaller.unmarshal(new FileReader(XML_FORECAST.toFile()));
        return forecastDTOs.getForecasts().stream().map(this::importForecast).collect(Collectors.joining("\n"));
    }

    private String importForecast(ImportForecastDTO importForecastDTO) {
        boolean isValid = this.validationUtils.isValid(importForecastDTO);
        String result = "";
        if (!isValid) {
            result = INVALID_FORECAST;
        } else {
            Optional<Forecast> byCityAndDayOfWeek = this.forecastRepository.findAllByCity_IdAndDayOfWeek(importForecastDTO.getCityId(), importForecastDTO.getDayOfWeek());
            Optional<City> city = this.cityRepository.findById(importForecastDTO.getCityId());

            if (byCityAndDayOfWeek.isPresent() || city.isEmpty()) {
                result = INVALID_COUNTRY;

            } else {
                Forecast forecast = this.modelMapper.map(importForecastDTO, Forecast.class);
                forecast.setCity(city.get());
                this.forecastRepository.save(forecast);
                result = String.format(VALID_FORECAST_FORMAT, forecast.getDayOfWeek(), forecast.getMaxTemperature());
            }
        }
        return result;
    }

    @Override
    public String exportForecasts() {

        int population = 150000;
        DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;
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
