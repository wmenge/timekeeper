package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.WeekReportingPeriod;
import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

//@Component
public class MonthlyReportHeaderProvider implements HeaderProvider {

    private ReportingPeriod monthPeriod;

    public MonthlyReportHeaderProvider(ReportingPeriod monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    @Override
    public LinkedHashMap<String, Object> getHeaders() {

        List<WeekReportingPeriod> weekNumbers = monthPeriod.weekNumbers();

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("description1", "Customer");
        headers.put("description2", "Project");

        weekNumbers.forEach(week -> {
            headers.put(String.format("durations[%d]", week.getWeek()), StringUtils.rightPad(String.format("%s", week), 6));
        });

        headers.put("total", "Total  ");
        return headers;
    }
}
