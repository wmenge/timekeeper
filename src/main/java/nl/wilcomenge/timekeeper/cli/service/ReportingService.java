package nl.wilcomenge.timekeeper.cli.service;

import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryAggregrateTotal;
import nl.wilcomenge.timekeeper.cli.dto.reporting.TimesheetEntryPeriodTotal;
import nl.wilcomenge.timekeeper.cli.dto.reporting.Week;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    @Resource
    TimeSheetEntryRepository timeSheetEntryRepository;

    public List<TimesheetEntryAggregrate> getWeekReport(Week week) {

        List<TimesheetEntryPeriodTotal> totalsPerProject = timeSheetEntryRepository.totalsPerProjectByWeekday(
                week.getFirstDate(),
                week.getLastDate(),
                Sort.by(Sort.Direction.ASC, "project.customer.name").and(Sort.by(Sort.Direction.ASC, "project.name"))
        );

        List<TimesheetEntryAggregrate> result =  new ArrayList<>();

        totalsPerProject.forEach(element -> {
            TimesheetEntryAggregrate newElement = new TimesheetEntryAggregrate(element.getProject());
            if (!result.contains(newElement)) {
                result.add(newElement);
            }
            result.get(result.indexOf(newElement)).getDurations().put(element.getPeriod(), element.getTotal());
        });

        List<TimesheetEntryPeriodTotal> totals = timeSheetEntryRepository.totalsByWeekday(
                week.getFirstDate(),
                week.getLastDate()
        );

        TimesheetEntryAggregrateTotal totalElement = new TimesheetEntryAggregrateTotal();

        totals.forEach(element -> {
            totalElement.getDurations().put(element.getPeriod(), element.getTotal());
        });

        result.add(totalElement);

        return result;
    }
}
