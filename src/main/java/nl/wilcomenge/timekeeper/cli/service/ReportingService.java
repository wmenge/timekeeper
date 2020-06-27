package nl.wilcomenge.timekeeper.cli.service;

import nl.wilcomenge.timekeeper.cli.dto.TimesheetEntryAggregateTotal;
import nl.wilcomenge.timekeeper.cli.dto.TimesheetEntryAggregrate;
import nl.wilcomenge.timekeeper.cli.dto.Week;
import nl.wilcomenge.timekeeper.cli.model.Project;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

    @Resource
    TimeSheetEntryRepository timeSheetEntryRepository;

    public List<TimesheetEntryAggregrate> getWeekReport(Week week) {

        Map<Project, TimesheetEntryAggregrate> lines = new HashMap<>();

        List<TimeSheetEntry> entries = timeSheetEntryRepository.findByDateBetween(week.getFirstDate(), week.getLastDate());

        // TODO: Employ some stream voodoo
        entries.forEach(entry -> {

            if (!lines.containsKey(entry.getProject())) {
                lines.put(entry.getProject(), new TimesheetEntryAggregrate(entry.getProject()));
            }

            TimesheetEntryAggregrate line = lines.get(entry.getProject());

            if (!line.getDurations().containsKey(entry.getDate().getDayOfWeek().getValue())) {
                line.getDurations().put(entry.getDate().getDayOfWeek().getValue(), entry.getDuration());
            } else {
                line.getDurations().put(entry.getDate().getDayOfWeek().getValue(), line.getDurations().get(entry.getDate().getDayOfWeek().getValue()).plus(entry.getDuration()));
            }

            line.setTotal(line.getTotal().plus(entry.getDuration()));

        });

        TimesheetEntryAggregrate total = lines.values().stream()
                .reduce(
                        new TimesheetEntryAggregateTotal(),
                        (subtotal, element) -> {
                            subtotal.setTotal(subtotal.getTotal().plus(element.getTotal()));

                            element.getDurations().forEach((k,v) -> {
                                if (!subtotal.getDurations().containsKey(k)) {
                                    subtotal.getDurations().put(k,v);
                                } else {
                                    subtotal.getDurations().put(k,v.plus(subtotal.getDurations().get(k)));
                                }
                            });

                            return subtotal;
                        });

        return ListUtils.union(List.copyOf(lines.values()), Arrays.asList(total));
    }
}
