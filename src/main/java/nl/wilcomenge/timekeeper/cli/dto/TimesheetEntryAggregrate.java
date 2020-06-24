package nl.wilcomenge.timekeeper.cli.dto;

import lombok.Data;
import nl.wilcomenge.timekeeper.cli.model.Project;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimesheetEntryAggregrate {

    public TimesheetEntryAggregrate(Project project) {
        this.project = project;
    }

    private Project project;

    private Map<Integer, Duration> durations = new HashMap<>();

    private Duration total = Duration.ZERO;
}
