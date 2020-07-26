package nl.wilcomenge.timekeeper.cli.ui.table;

import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import org.springframework.shell.table.*;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class TableBuilder<T> {

    public Table build(T entry, LinkedHashMap<String, Object> headers) {
        List<T> list = Collections.singletonList(entry);
        TableModel model = new BeanListTableModel<T>(list, headers);
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(DurationFormatter.getInstance());
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder.build();
    }

    public Table build(Iterable<T> list, LinkedHashMap<String, Object> headers) {
        TableModel model = new BeanListTableModel<T>(list, headers);
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(DurationFormatter.getInstance());
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder.build();
    }

    public static LinkedHashMap<String, Object> getTimeSheetEntryHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("date", "Date");
        headers.put("project.customer.name", "Customer");
        headers.put("project.name", "Project");
        headers.put("duration", "Duration");
        headers.put("remark", "Remark");
        return headers;
    }

    public static LinkedHashMap<String, Object> getCustomerHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");
        return headers;
    }

    public static LinkedHashMap<String, Object> getHolidayHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("date", "Date");
        headers.put("name", "Name");
        return headers;
    }

    public static LinkedHashMap<String, Object> getProjectHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("customer.name", "Customer");
        headers.put("name", "Name");
        headers.put("billable", "Billable");
        return headers;
    }

    public static LinkedHashMap<String, Object> getWeeklyReportHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("description1", "Customer");
        headers.put("description2", "Project");
        headers.put("durations[1]", "Mon   ");
        headers.put("durations[2]", "Tue   ");
        headers.put("durations[3]", "Wed   ");
        headers.put("durations[4]", "Thu   ");
        headers.put("durations[5]", "Fri   ");
        headers.put("durations[6]", "Sat   ");
        headers.put("durations[7]", "Sun   ");
        headers.put("total", "Total  ");
        return headers;
    }

    public static LinkedHashMap<String, Object> getMonthlyReportHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("description1", "Customer");
        headers.put("description2", "Project");
        headers.put("durations[1]", "1     ");
        headers.put("durations[2]", "2     ");
        headers.put("durations[3]", "3     ");
        headers.put("durations[4]", "4     ");
        headers.put("durations[5]", "5     ");
        headers.put("total", "Total  ");
        return headers;
    }

    public static LinkedHashMap<String, Object> getYearlyReportHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("description1", "Customer");
        headers.put("description2", "Project");
        headers.put("durations[1]", "Jan   ");
        headers.put("durations[2]", "Feb   ");
        headers.put("durations[3]", "Mar   ");
        headers.put("durations[4]", "Apr   ");
        headers.put("durations[5]", "May   ");
        headers.put("durations[6]", "Jun   ");
        headers.put("durations[7]", "Jul   ");
        headers.put("durations[8]", "Aug   ");
        headers.put("durations[9]", "Sep   ");
        headers.put("durations[10]", "Oct   ");
        headers.put("durations[11]", "Nov   ");
        headers.put("durations[12]", "Dec   ");
        headers.put("total", "Total  ");
        return headers;
    }

    public static LinkedHashMap<String, Object> getUtilizationReportHeaders() {
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