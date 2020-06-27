package nl.wilcomenge.timekeeper.cli.ui.table;

import nl.wilcomenge.timekeeper.cli.dto.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.Project;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import org.springframework.shell.table.*;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class TableBuilder<T> {

    public Table build(T entry, Class itemClass) {
        List<T> list = Collections.singletonList(entry);
        TableModel model = new BeanListTableModel<T>(list, getHeaders(itemClass));
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(DurationFormatter.getInstance());
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder.build();
    }

    public Table build(Iterable<T> list, Class itemClass) {
        TableModel model = new BeanListTableModel<T>(list, getHeaders(itemClass));
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(DurationFormatter.getInstance());
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder.build();
    }

    // TODO: Some cool polymorphism trick
    private LinkedHashMap<String, Object> getHeaders(Class itemClass) {

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();

        if (TimeSheetEntry.class.equals(itemClass)) {
            headers.put("id", "Id");
            headers.put("date", "Date");
            headers.put("project.customer.name", "Customer");
            headers.put("project.name", "Project");
            headers.put("duration", "Duration");
            headers.put("remark", "Remark");
        }

        if (Customer.class.equals(itemClass)) {
            headers.put("id", "Id");
            headers.put("name", "Name");
        }

        if (Project.class.equals(itemClass)) {
            headers.put("id", "Id");
            headers.put("customer.name", "Customer");
            headers.put("name", "Name");
        }

        if (TimesheetEntryAggregrate.class.equals(itemClass)) {
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
        }

        return headers;
    }

}
