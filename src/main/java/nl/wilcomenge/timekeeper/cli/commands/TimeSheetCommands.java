package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import nl.wilcomenge.timekeeper.cli.ui.table.headers.impl.TimesheetEntryHeaderProvider;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView.MessageType;
import org.jline.utils.AttributedString;
import org.springframework.data.domain.Sort;
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

    @Resource
    private TimesheetEntryHeaderProvider headerProvider;

    @ShellMethod("Add a timesheet entry.")
    public AttributedString addEntry(@NonNull String duration, @ShellOption(defaultValue = "") String remark) {
        TimeSheetEntry entry = new TimeSheetEntry();
        entry.setProject(state.getOptionalProject().get());
        entry.setDate(state.getDate());
        entry.setDuration(DurationFormatter.getInstance().parse(duration));
        entry.setRemark(remark);

        timeSheetEntryRepository.save(entry);
        List<TimeSheetEntry> entries = timeSheetEntryRepository.findByDate(state.getDate());
        return ResultView.build(MessageType.INFO, "Created entry", entries).render(headerProvider);
    }

    public Availability addEntryAvailability() {
        return state.getOptionalProject().isPresent()
                ? Availability.available()
                : Availability.unavailable("there is no project selected");
    }

    @ShellMethod("Change a timesheet entry.")
    public AttributedString changeEntry(@NonNull Long id, @NonNull String duration, @ShellOption(defaultValue = "") String remark) {
        TimeSheetEntry entry = timeSheetEntryRepository.findById(id).get();
        entry.setDuration(DurationFormatter.getInstance().parse(duration));
        if (remark != null && remark.length() > 0) {
            entry.setRemark(remark);
        }
        timeSheetEntryRepository.save(entry);
        return ResultView.build(MessageType.INFO, "Changed entry", entry).render(headerProvider);
    }

    @ShellMethod("Remove a timesheet entry.")
    public AttributedString removeEntry(@NonNull Long id) {
        TimeSheetEntry entry = timeSheetEntryRepository.findById(id).get();
        timeSheetEntryRepository.delete(entry);
        List<TimeSheetEntry> entries = timeSheetEntryRepository.findByDate(state.getDate());;
        return ResultView.build(MessageType.INFO, "Removed entry", entries).render(headerProvider);
    }

    @ShellMethod("List entries.")
    public AttributedString listEntries(boolean showAll) {
        List<TimeSheetEntry> entries = showAll ?
                timeSheetEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "date").and(Sort.by(Sort.Direction.ASC, "id"))) :
                timeSheetEntryRepository.findByDate(state.getDate());
        String message = showAll ? "Showing all entries" : String.format("Showing entries of %s", state.getDate());
        return ResultView.build(MessageType.INFO, message, entries).render(headerProvider);
    }

}