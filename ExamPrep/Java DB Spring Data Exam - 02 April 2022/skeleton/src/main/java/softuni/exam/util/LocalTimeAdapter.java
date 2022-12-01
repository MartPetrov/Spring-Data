package softuni.exam.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime unmarshal(String time) throws Exception {
        return LocalTime.parse(time, timeFormatter);
    }

    @Override
    public String marshal(LocalTime time) throws Exception {
        return time.format(timeFormatter);
    }
}
