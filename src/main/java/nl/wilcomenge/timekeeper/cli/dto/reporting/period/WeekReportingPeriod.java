package nl.wilcomenge.timekeeper.cli.dto.reporting.period;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

public class WeekReportingPeriod implements ReportingPeriod {

    private int week;
    private int year;

    public static WeekReportingPeriod getFor(LocalDate date) {
        int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        return new WeekReportingPeriod(weekNumber, date.getYear());
    }

    public WeekReportingPeriod(int week, int year) {
        this.week = week;
        this.year = year;
    }

    @Override
    public LocalDate getFirstDate() {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.now()
                .withYear(year)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1);
    }

    @Override
    public LocalDate getLastDate() {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.now()
                .withYear(year)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekReportingPeriod week1 = (WeekReportingPeriod) o;
        return week == week1.week &&
                year == week1.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(week, year);
    }

    @Override
    public String toString() {
        return String.format("%d-%s", week, year);
    }
}
