package nl.wilcomenge.timekeeper.cli.ui.table;

import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.Project;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableModel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class TableBuilder<T> {


    public Table build(T entry, Class itemClass) {
        List<T> list = Collections.singletonList(entry);
        TableModel model = new BeanListTableModel<T>(list, getHeaders(itemClass));
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        return tableBuilder.build();
    }

    public Table build(Iterable<T> list, Class itemClass) {
        TableModel model = new BeanListTableModel<T>(list, getHeaders(itemClass));
        org.springframework.shell.table.TableBuilder tableBuilder = new org.springframework.shell.table.TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
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
            headers.put("formattedDuration", "Duration");
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

        return headers;
    }

}
