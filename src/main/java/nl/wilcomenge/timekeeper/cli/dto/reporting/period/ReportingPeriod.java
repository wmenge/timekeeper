package nl.wilcomenge.timekeeper.cli.dto.reporting.period;

import java.time.LocalDate;

public interface ReportingPeriod {

    LocalDate getFirstDate();

    LocalDate getLastDate();
}
