package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class TimesheetEntryHeaderProvider implements HeaderProvider {

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("date", "Date");
        headers.put("project.customer.name", "Customer");
        headers.put("project.name", "Project");
        headers.put("duration", "Duration");
        headers.put("remark", "Remark");
        return headers;
    }
}
