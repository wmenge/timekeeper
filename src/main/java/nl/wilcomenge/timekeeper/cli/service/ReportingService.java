package nl.wilcomenge.timekeeper.cli.service;

import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrateTotal;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "project.customer.name").and(Sort.by(Sort.Direction.ASC, "project.name"));
    @Resource
    TimeSheetEntryRepository timeSheetEntryRepository;

    public List<TimesheetEntryAggregrate> getWeekReport(ReportingPeriod period) {
        List<TimesheetEntryPeriodTotal> totalsPerProject = timeSheetEntryRepository.totalsPerProjectByWeekday(period.getFirstDate(), period.getLastDate(), DEFAULT_SORT);
        List<TimesheetEntryPeriodTotal> totals = timeSheetEntryRepository.totalsByWeekday(period.getFirstDate(), period.getLastDate());
        return getAggregrates(period, totalsPerProject, totals);
    }

    public List<TimesheetEntryAggregrate> getMonthReport(ReportingPeriod period) {
        List<TimesheetEntryPeriodTotal> totalsPerProject = timeSheetEntryRepository.totalsPerProjectByWeek(period.getFirstDate(), period.getLastDate(), DEFAULT_SORT);
        List<TimesheetEntryPeriodTotal> totals = timeSheetEntryRepository.totalsByWeek(period.getFirstDate(), period.getLastDate());
        return getAggregrates(period, totalsPerProject, totals);
    }

    public List<TimesheetEntryAggregrate> getYearReport(ReportingPeriod period) {
        List<TimesheetEntryPeriodTotal> totalsPerProject = timeSheetEntryRepository.totalsPerProjectByMonth(period.getFirstDate(), period.getLastDate(), DEFAULT_SORT);
        List<TimesheetEntryPeriodTotal> totals = timeSheetEntryRepository.totalsByMonth(period.getFirstDate(), period.getLastDate());
        return getAggregrates(period, totalsPerProject, totals);
    }

    private List<TimesheetEntryAggregrate> getAggregrates(ReportingPeriod period, List<TimesheetEntryPeriodTotal> totalsPerProject, List<TimesheetEntryPeriodTotal> totals) {
        List<TimesheetEntryAggregrate> result =  new ArrayList<>();

        totalsPerProject.forEach(element -> {
            TimesheetEntryAggregrate newElement = new TimesheetEntryAggregrate(element.getProject());
            if (!result.contains(newElement)) {
                result.add(newElement);
            }
            result.get(result.indexOf(newElement)).getDurations().put(element.getPeriod(), element.getTotal());
        });

        TimesheetEntryAggregrateTotal totalElement = new TimesheetEntryAggregrateTotal();

        totals.forEach(element -> {
            totalElement.getDurations().put(element.getPeriod(), element.getTotal());
        });

        result.add(totalElement);

        return result;
    }
}
