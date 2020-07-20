package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.reporting.UtilizationReportEntry;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.MonthReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.WeekReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.YearReportingPeriod;
import nl.wilcomenge.timekeeper.cli.service.ReportingService;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import nl.wilcomenge.timekeeper.cli.ui.table.TableBuilder;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@ShellComponent
public class ReportCommands {

    @Resource
    State state;

    @Resource
    ReportingService reportingService;

    @Resource
    UserProfileService userProfileService;

    @ShellMethod("Weekly report")
    public AttributedString reportWeekly(@ShellOption(defaultValue = "0") int week, @ShellOption(defaultValue = "0") int year) {
        ReportingPeriod weekPeriod = getWeek(week, year);
        List<TimesheetEntryAggregrate> report = reportingService.getWeekReport(weekPeriod);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Weekly Report of week %s", weekPeriod), report).render(TableBuilder.getWeeklyReportHeaders());
    }

    private ReportingPeriod getWeek(int week, int year) {
        if (week == 0 && year == 0) {
            return WeekReportingPeriod.getFor(state.getDate());
        } else {
            if (week == 0) week = state.getDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
            if (year == 0) year = state.getDate().getYear();
            return new WeekReportingPeriod(week, year);
        }
    }

    @ShellMethod("Monthly report")
    public AttributedString reportMonthly(@ShellOption(defaultValue = "0") int month, @ShellOption(defaultValue = "0") int year) {
        ReportingPeriod monthPeriod = getMonth(month, year);
        List<TimesheetEntryAggregrate> report = reportingService.getMonthReport(monthPeriod);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Monthly Report of month %s", monthPeriod), report).render(TableBuilder.getMonthlyReportHeaders());
    }

    private ReportingPeriod getMonth(int month, int year) {
        if (month == 0 && year == 0) {
            return MonthReportingPeriod.getFor(state.getDate());
        } else {
            if (month == 0) month = state.getDate().getMonthValue();
            if (year == 0) year = state.getDate().getYear();
            return new WeekReportingPeriod(month, year);
        }
    }

    @ShellMethod("Yearly report")
    public AttributedString reportYearly(@ShellOption(defaultValue = "0") int year) {
        ReportingPeriod yearPeriod = getYear(year);
        List<TimesheetEntryAggregrate> report = reportingService.getYearReport(yearPeriod);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Yearly Report of %s", yearPeriod), report).render(TableBuilder.getYearlyReportHeaders());
    }

    @ShellMethod("Utilization weekly YTD")
    @ShellMethodAvailability("utilizationAvailabilityCheck")
    public AttributedString reportUtilizationWeekly(@ShellOption(defaultValue = "0") int year) {
        ReportingPeriod yearPeriod = getYear(year);

        List<UtilizationReportEntry> report = reportingService.getUtilizationReportPerWeek(yearPeriod);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Weekly Utilization Report of %s", yearPeriod), report).render(TableBuilder.getUtilizationReportHeaders());
    }

    @ShellMethod("Utilization monthly YTD")
    @ShellMethodAvailability("utilizationAvailabilityCheck")
    public AttributedString reportUtilizationMonthly(@ShellOption(defaultValue = "0") int year) {
        ReportingPeriod yearPeriod = getYear(year);

        List<UtilizationReportEntry> report = reportingService.getUtilizationReportPerMonth(yearPeriod);
        return ResultView.build(ResultView.MessageType.INFO, String.format("Monthly Utilization Report of %s", yearPeriod), report).render(TableBuilder.getUtilizationReportHeaders());
    }

    private ReportingPeriod getYear(int year) {
        if (year == 0) year = state.getDate().getYear();
        return new YearReportingPeriod(year);
    }

    public Availability utilizationAvailabilityCheck() {
        return userProfileService.getProfile().getFulltimeFactor() != null
                ? Availability.available()
                : Availability.unavailable("no fulltime percentage has been set");
    }
}
