package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.dto.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.Week;
import nl.wilcomenge.timekeeper.cli.service.ReportingService;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import java.util.List;

@ShellComponent
public class ReportCommands {

    @Resource
    State state;

    @Resource
    ReportingService reportingService;

    @ShellMethod("Weekly report")
    public AttributedString reportWeekly() {

        Week week = Week.getWeekFor(state.getDate());
        List<TimesheetEntryAggregrate> report = reportingService.getWeekReport(week);
        return ResultView.build(ResultView.MessageType.INFO, "Weekly Report", report).render(TimesheetEntryAggregrate.class);
    }


}
