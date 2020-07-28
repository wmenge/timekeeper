package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class HolidayHeaderProvider implements HeaderProvider {

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("date", "Date");
        headers.put("name", "Name");
        return headers;
    }
}
