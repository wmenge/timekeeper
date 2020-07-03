package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.reporting.Week;
import nl.wilcomenge.timekeeper.cli.service.ReportingService;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.util.List;

@ShellComponent
public class ReportCommands {

    @Resource
    State state;

    @Resource
    ReportingService reportingService;

    @ShellMethod("Weekly report")
    public AttributedString reportWeekly(@ShellOption(defaultValue = "0")int week, @ShellOption(defaultValue = "0")int year) {

        Week weekModel = null;

        if (week == 0 && year == 0) {
            weekModel = Week.getWeekFor(state.getDate());
        } else {
            if (week == 0) week = Week.getWeekFor(state.getDate()).getWeek();
            if (year == 0) year = state.getDate().getYear();
            weekModel = new Week(week, year);
        }

        List<TimesheetEntryAggregrate> report = reportingService.getWeekReport(weekModel);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Weekly Report of week %s", weekModel), report).render(TimesheetEntryAggregrate.class);
    }


}
