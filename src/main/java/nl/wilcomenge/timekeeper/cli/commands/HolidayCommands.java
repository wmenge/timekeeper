package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Holiday;
import nl.wilcomenge.timekeeper.cli.model.HolidayRepository;
import nl.wilcomenge.timekeeper.cli.service.ImportExportService;
import nl.wilcomenge.timekeeper.cli.ui.table.TableBuilder;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@ShellComponent
public class HolidayCommands {

    @Resource
    private HolidayRepository holidayRepository;

    @Resource
    private ImportExportService importExportService;

    @Resource
    private State state;

    @ShellMethod("Add a holiday.")
    public AttributedString addHoliday(@NonNull LocalDate date, @NonNull String name) {
        Holiday holiday = new Holiday();
        holiday.setDate(date);
        holiday.setName(name);
        holidayRepository.save(holiday);
        return ResultView.build(ResultView.MessageType.INFO, "Created holiday", holiday).render(TableBuilder.getHolidayHeaders());
    }

    @ShellMethod("Change a holiday name.")
    public AttributedString changeHolidayName(@NonNull LocalDate date, @NonNull String name) {
        Holiday holiday = holidayRepository.findById(date).get();
        holiday.setName(name);
        holidayRepository.save(holiday);
        return ResultView.build(ResultView.MessageType.INFO, "Changed holiday name", holiday).render(TableBuilder.getHolidayHeaders());
    }

    @ShellMethod("Remove a holiday.")
    @Transactional
    public AttributedString removeHoliday(@NonNull LocalDate date) {
        Holiday holiday = holidayRepository.findById(date).get();
        holidayRepository.delete(holiday);
        List<Holiday> holidayList = holidayRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        return ResultView.build(ResultView.MessageType.INFO, "Removed holiday", holidayList).render(TableBuilder.getHolidayHeaders());
    }

    @ShellMethod("List holidays.")
    public AttributedString listHolidays() {
        List<Holiday> holidayList = holidayRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        return ResultView.build(ResultView.MessageType.INFO, "Showing holidays:", holidayList).render(TableBuilder.getHolidayHeaders());
    }

    @ShellMethod("Export holidays.")
    public String exportHolidays(File file) throws IOException {
        String result = importExportService.exportHolidayData();

        // TODO: Error if exists
        file.createNewFile();
        Path path = file.toPath();
        byte[] strToBytes = result.getBytes();

        Files.write(path, strToBytes);

        return "Ok";
    }

    @ShellMethod("Import holidays.")
    public String importHolidays(File file, @ShellOption(defaultValue = "false")Boolean confirm) throws IOException {
        if (!confirm) return "Please confirm import";
        String contents = Files.readString(file.toPath());
        importExportService.importHolidayData(contents);
        return "Ok";
    }

}
