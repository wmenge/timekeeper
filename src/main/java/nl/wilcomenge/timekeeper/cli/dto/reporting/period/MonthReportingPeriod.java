package nl.wilcomenge.timekeeper.cli.dto.reporting.period;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class MonthReportingPeriod implements ReportingPeriod {

    private int month;
    private int year;

    public static MonthReportingPeriod getFor(LocalDate date) {
        return new MonthReportingPeriod(date.getMonthValue(), date.getYear());
    }

    public MonthReportingPeriod(int month, int year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public LocalDate getFirstDate() {
        return LocalDate.now()
                .withYear(year)
                .withMonth(month)
                .with(TemporalAdjusters.firstDayOfMonth());
    }

    @Override
    public LocalDate getLastDate() {
        return LocalDate.now()
                .withYear(year)
                .withMonth(month)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthReportingPeriod that = (MonthReportingPeriod) o;
        return month == that.month &&
                year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, year);
    }

    @Override
    public String toString() {
        return String.format("%d-%d", month, year);
    }
}
