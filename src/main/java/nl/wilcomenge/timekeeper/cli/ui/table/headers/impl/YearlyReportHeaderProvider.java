package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;

@Component
public class YearlyReportHeaderProvider implements HeaderProvider {

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("description1", "Customer");
        headers.put("description2", "Project");

        int i = 1;
        // TODO: Only provide days that are in the dataset
        for(Month month: Month.values()) {
            headers.put(String.format("durations[%d]", i), StringUtils.rightPad(month.getDisplayName(TextStyle.SHORT, LocaleContextHolder.getLocale()), 6));
            i++;
        }

        headers.put("total", "Total  ");
        return headers;
    }
}
