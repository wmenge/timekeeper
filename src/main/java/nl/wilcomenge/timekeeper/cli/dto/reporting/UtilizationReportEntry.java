package nl.wilcomenge.timekeeper.cli.dto.reporting;

import lombok.Data;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.MonthReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.WeekReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.YearReportingPeriod;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Currency;

@Data
public class UtilizationReportEntry {

    public enum periodType { WEEK, MONTH, YEAR };

    public UtilizationReportEntry(Integer period, Integer year, String periodType, Long billableHours, Long nonBillableHours, BigDecimal revenue) {
        this.period = getReportingPeriod(period, year, periodType);
        this.billableHours = Duration.ofNanos(billableHours);
        this.nonBillableHours = Duration.ofNanos(nonBillableHours);
        this.revenue = revenue;
    }

    /*public UtilizationReportEntry(Integer period, Integer year, String periodType, Long billableHours, Long nonBillableHours, Long revenue) {
        this.period = getReportingPeriod(period, year, periodType);
        this.billableHours = Duration.ofNanos(billableHours);
        this.nonBillableHours = Duration.ofNanos(nonBillableHours);
        this.revenue = BigDecimal.valueOf(revenue);
    }*/

    ReportingPeriod getReportingPeriod(Integer period, Integer year, String periodTypeString) {

        periodType periodTypeEnum = periodType.valueOf(periodTypeString);

        switch (periodTypeEnum) {
            case WEEK:
                return new WeekReportingPeriod(period, year);
            case MONTH:
                return new MonthReportingPeriod(period, year);
            default:
                return new YearReportingPeriod(year);
        }
    }

    private ReportingPeriod period;

    private Duration billableHours;

    private Duration nonBillableHours;

    private Duration workingHours;

    private BigDecimal revenue;

    private double getUtilizationFactor() {
        return (double)billableHours.toMinutes() / (double)workingHours.toMinutes();
    }

    public String getUtilizationPercentageString() {
        return String.format("%.2f %%", getUtilizationFactor() * 100);
    }

    public String getRevenueString() {
        return String.format("%s %,.2f", Currency.getInstance("EUR").getSymbol(), revenue);
    }

}
