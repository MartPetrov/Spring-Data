package softuni.exam.constans;

import java.nio.file.Path;

public enum Paths {
    ;
    public static final Path JSON_AGENTS_PATH = Path.of("src/main/resources/files/json/agents.json");
    public static final Path JSON_TOWNS_PATH = Path.of("src/main/resources/files/json/towns.json");

    public static final Path XML_APARTMENTS = Path.of("src/main/resources/files/xml/apartments.xml");
    public static final Path XML_OFFERS = Path.of("src/main/resources/files/xml/offers.xml");
}
