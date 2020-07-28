package nl.wilcomenge.timekeeper.cli.dto.reporting.period;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface ReportingPeriod {

    LocalDate getFirstDate();

    LocalDate getLastDate();

    // TODO: This will probably fall apart at the end/beginning of a year
    default List<WeekReportingPeriod> weekNumbers() {
        int year = getFirstDate().getYear();
        int firstWeekNumber = getFirstDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        int lastWeekNumber = getLastDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        assert(firstWeekNumber <= lastWeekNumber);
        List<WeekReportingPeriod> result = new ArrayList<>();

        for (int i = firstWeekNumber; i <= lastWeekNumber; i++) {
            result.add(new WeekReportingPeriod(i, year));
        }

        return result;
    }
}
