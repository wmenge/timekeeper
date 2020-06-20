package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import nl.wilcomenge.timekeeper.cli.ui.table.TableBuilder;
import org.springframework.lang.NonNull;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.util.List;

@ShellComponent
public class TimeSheetCommands {

    @Resource
    private State state;

    @Resource
    private TimeSheetEntryRepository timeSheetEntryRepository;

    @ShellMethod("Add a timesheet entry.")
    public String entryAdd(@NonNull String duration, @ShellOption(defaultValue = "") String remark) {
        TimeSheetEntry entry = new TimeSheetEntry();
        entry.setProject(state.getSelectedProject());
        entry.setDate(state.getDate());
        entry.setDuration(DurationFormatter.parse(duration));
        entry.setRemark(remark);

        timeSheetEntryRepository.save(entry);
        List<TimeSheetEntry> entries = timeSheetEntryRepository.findAll();
        return new TableBuilder<TimeSheetEntry>().build(entries, TimeSheetEntry.class).render(80);
    }

    @ShellMethod("Change a timesheet entry.")
    public String entryChange(@NonNull Long id, @NonNull String duration, @ShellOption(defaultValue = "") String remark) {
        TimeSheetEntry entry = timeSheetEntryRepository.findById(id).get();
        entry.setDuration(DurationFormatter.parse(duration));
        if (remark != null && remark.length() > 0) {
            entry.setRemark(remark);
        }

        timeSheetEntryRepository.save(entry);
        return new TableBuilder<TimeSheetEntry>().build(entry, TimeSheetEntry.class).render(80);
    }

    @ShellMethod("Rempve a timesheet entry.")
    public String entryRemove(@NonNull Long id) {
        TimeSheetEntry entry = timeSheetEntryRepository.findById(id).get();
        timeSheetEntryRepository.delete(entry);
        List<TimeSheetEntry> entries = timeSheetEntryRepository.findAll();
        return "entry removed\n" + new TableBuilder<TimeSheetEntry>().build(entries, TimeSheetEntry.class).render(80);
    }

    public Availability entryAddAvailability() {
        return state.getSelectedProject() != null
                ? Availability.available()
                : Availability.unavailable("there is no project selected");
    }

    @ShellMethod("List entries.")
    public String entryList() {
        List<TimeSheetEntry> entries = timeSheetEntryRepository.findAll();
        return new TableBuilder<TimeSheetEntry>().build(entries, TimeSheetEntry.class).render(80);
    }

}
