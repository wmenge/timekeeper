package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class CustomerHeaderProvider implements HeaderProvider {

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");
        return headers;
    }
}
