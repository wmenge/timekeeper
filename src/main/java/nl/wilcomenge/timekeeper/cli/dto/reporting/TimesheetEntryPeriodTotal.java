package nl.wilcomenge.timekeeper.cli.dto.reporting;

import lombok.Data;
import nl.wilcomenge.timekeeper.cli.model.Project;

import java.time.Duration;

@Data
public class TimesheetEntryPeriodTotal {

    public TimesheetEntryPeriodTotal(Integer period, Long duration) {
        this.project = null;
        this.period = period;
        this.total = Duration.ofNanos(duration);
    }

    public TimesheetEntryPeriodTotal(Project project, Integer period, Long duration) {
        this.project = project;
        this.period = period;
        this.total = Duration.ofNanos(duration);
    }

    private Project project;

    private Integer period;

    private Duration total;
}
