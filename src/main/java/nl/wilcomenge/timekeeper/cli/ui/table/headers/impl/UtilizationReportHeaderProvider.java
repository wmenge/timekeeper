package nl.wilcomenge.timekeeper.cli.ui.table.headers.impl;

import nl.wilcomenge.timekeeper.cli.ui.table.headers.HeaderProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class UtilizationReportHeaderProvider implements HeaderProvider {

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("period", "Period");
        headers.put("billableHours", "Billable Hours");
        headers.put("nonBillableHours", "Non-Billable Hours");
        headers.put("workingHours", "Working Hours");
        headers.put("utilizationPercentageString", "Utilization");

        // TODO: Only show column when revenue is available
        headers.put("revenueString", "Revenue");
        return headers;
    }
}
