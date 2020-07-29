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
}