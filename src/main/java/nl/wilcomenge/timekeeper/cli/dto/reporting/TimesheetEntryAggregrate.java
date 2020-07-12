package nl.wilcomenge.timekeeper.cli.dto.reporting;

import lombok.Data;
import nl.wilcomenge.timekeeper.cli.model.Project;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class TimesheetEntryAggregrate {

    public TimesheetEntryAggregrate() {

    }

    public TimesheetEntryAggregrate(Project project) {
        this.project = project;
    }

    private Project project;

    private Map<Integer, Duration> durations = new HashMap<>();

    public String getDescription1() {
        return this.getProject().getCustomer().getName();
    }

    public String getDescription2() {
        return this.getProject().getName();
    }

    public Duration getTotal() {
        return this.durations.values().stream().reduce(Duration.ZERO, (sum, element) -> { return sum.plus(element); });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimesheetEntryAggregrate that = (TimesheetEntryAggregrate) o;
        return project.equals(that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project);
    }
}
