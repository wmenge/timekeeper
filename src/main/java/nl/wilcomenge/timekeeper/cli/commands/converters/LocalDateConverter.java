package nl.wilcomenge.timekeeper.cli.commands.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: check for more converter candidates
@Component
public class LocalDateConverter implements Converter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d-M-yyyy");

    @Override
    public LocalDate convert(String dateString) {
        return LocalDate.parse(dateString, FORMATTER);
    }
}
