package nl.wilcomenge.timekeeper.cli.model;

import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal;
import nl.wilcomenge.timekeeper.cli.dto.reporting.UtilizationReportEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {

    List<TimeSheetEntry> findByDate(LocalDate date);

    List<TimeSheetEntry> findByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    // TODO: Check if sum(duration) can be returned as duration instead of Long
    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(e.project, EXTRACT(ISO_DAY_OF_WEEK FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.project, period")
    List<TimesheetEntryPeriodTotal> totalsPerProjectByWeekday(LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(EXTRACT(ISO_DAY_OF_WEEK FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period")
    List<TimesheetEntryPeriodTotal> totalsByWeekday(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(e.project, EXTRACT(ISO_WEEK FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.project, period")
    List<TimesheetEntryPeriodTotal> totalsPerProjectByWeek(LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(EXTRACT(ISO_WEEK FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period")
    List<TimesheetEntryPeriodTotal> totalsByWeek(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(e.project, EXTRACT(MONTH FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.project, period")
    List<TimesheetEntryPeriodTotal> totalsPerProjectByMonth(LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal(EXTRACT(MONTH FROM e.date) as period, SUM(e.duration)) " +
            "FROM TimeSheetEntry as e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period")
    List<TimesheetEntryPeriodTotal> totalsByMonth(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.UtilizationReportEntry(" +
            "EXTRACT(ISO_WEEK FROM date) AS period, " +
            "EXTRACT(YEAR FROM date) AS year, " +
            "'WEEK' AS periodtype, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) AS billableHours, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN 0 ELSE e.duration END) AS nonBillableHours, " +
            "(SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) / 3600000000000) * p.hourlyRate AS revenue) " +
            "FROM TimeSheetEntry AS e, " + //JOIN Project as p ON e.project = p.id " +
            "UserProfile AS p " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period, year, periodtype")
    List<UtilizationReportEntry> utilizationPerWeek(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.UtilizationReportEntry(" +
            "EXTRACT(MONTH FROM date) AS period, " +
            "EXTRACT(YEAR FROM date) AS year, " +
            "'MONTH' AS periodtype, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) AS billableHours, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN 0 ELSE e.duration END) AS nonBillableHours, " +
            "(SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) / 3600000000000) * p.hourlyRate AS revenue) " +
            "FROM TimeSheetEntry AS e, " + //JOIN Project as p ON e.project = p.id " +
            "UserProfile AS p " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period, year, periodtype")
    List<UtilizationReportEntry> utilizationPerMonth(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new nl.wilcomenge.timekeeper.cli.dto.reporting.UtilizationReportEntry(" +
            "EXTRACT(YEAR FROM date) AS period, " +
            "EXTRACT(YEAR FROM date) AS year, " +
            "'YEAR' AS periodtype, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) AS billableHours, " +
            "SUM(CASE WHEN e.project.billable = TRUE THEN 0 ELSE e.duration END) AS nonBillableHours, " +
            "(SUM(CASE WHEN e.project.billable = TRUE THEN e.duration ELSE 0 END) / 3600000000000) * p.hourlyRate AS revenue) " +
            "FROM TimeSheetEntry AS e, " + //JOIN Project as p ON e.project = p.id " +
            "UserProfile AS p " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY period, year, periodtype")
    List<UtilizationReportEntry> utilizationPerYear(LocalDate startDate, LocalDate endDate);

}
