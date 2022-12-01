package softuni.exam.constans;

import java.nio.file.Path;

public enum Paths {
    ;

    public static final Path JSON_COUNTRIES_PATH = Path.of("src/main/resources/files/json/countries.json");
    public static final Path JSON_CITIES_PATH = Path.of("src/main/resources/files/json/cities.json");

    public static final Path XML_FORECAST = Path.of("src/main/resources/files/xml/forecasts.xml");
}
