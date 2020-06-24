package nl.wilcomenge.timekeeper.cli.dto;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

public class Week {

    int week;

    int year;

    public static Week getWeekFor(LocalDate date) {
        int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        return new Week(weekNumber, date.getYear());
    }

    public Week(int week, int year) {
        this.week = week;
        this.year = year;
    }

    public LocalDate getFirstDate() {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.now()
                .withYear(year)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1);
    }

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
        Week week1 = (Week) o;
        return week == week1.week &&
                year == week1.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(week, year);
    }
}
