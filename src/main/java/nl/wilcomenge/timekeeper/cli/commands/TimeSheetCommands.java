package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import org.springframework.lang.NonNull;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
public class TimeSheetCommands {

    @Resource
    private State state;

    @Resource
    private TimeSheetEntryRepository timeSheetEntryRepository;

    @ShellMethod("Add a timesheet entry.")
    public String entryAdd(@NonNull Double hours, @ShellOption(defaultValue = "") String remark) {
        TimeSheetEntry entry = new TimeSheetEntry();
        entry.setProject(state.getSelectedProject());
        entry.setDate(state.getDate());
        entry.setMinutes((int)Math.round(hours * 60));
        entry.setRemark(remark);

        timeSheetEntryRepository.save(entry);
        return entry.getId().toString();
    }

    public Availability entryAddAvailability() {
        return state.getSelectedProject() != null
                ? Availability.available()
                : Availability.unavailable("there is no project selected");
    }

    @ShellMethod("List entries.")
    public String entryList() {

        List<TimeSheetEntry> customerList = timeSheetEntryRepository.findAll();

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("timestamp", "Timestamp");
        headers.put("project.customer.name", "Project");
        headers.put("project.name", "Project");
        headers.put("minutes", "Minutes");

        TableModel model = new BeanListTableModel<>(customerList, headers);
        TableBuilder tableBuilder = new TableBuilder(model);

        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        return tableBuilder.build().render(80);
    }


}
