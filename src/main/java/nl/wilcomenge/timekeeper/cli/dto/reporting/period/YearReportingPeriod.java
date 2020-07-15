package nl.wilcomenge.timekeeper.cli.dto.reporting.period;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class YearReportingPeriod implements ReportingPeriod {

    private int year;

    public static YearReportingPeriod getFor(LocalDate date) {
        return new YearReportingPeriod(date.getYear());
    }

    public YearReportingPeriod(int year) {
        this.year = year;
    }

    @Override
    public LocalDate getFirstDate() {
        return LocalDate.now().withYear(year).with(TemporalAdjusters.firstDayOfYear());
    }

    @Override
    public LocalDate getLastDate() {
        return LocalDate.now().withYear(year).with(TemporalAdjusters.lastDayOfYear());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearReportingPeriod that = (YearReportingPeriod) o;
        return year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }

    @Override
    public String toString() {
        return String.format("%d", year);
    }
}
