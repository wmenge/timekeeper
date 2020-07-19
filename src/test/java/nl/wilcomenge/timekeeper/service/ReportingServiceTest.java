package nl.wilcomenge.timekeeper.service;

import nl.wilcomenge.timekeeper.cli.dto.reporting.period.MonthReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.WeekReportingPeriod;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import nl.wilcomenge.timekeeper.cli.service.ReportingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportingServiceTest {

    ReportingService reportingService = new ReportingService();

    @MockBean
    TimeSheetEntryRepository timeSheetEntryRepository;

    @Test
    public void testGetWorkingDurationForWeek() {
        ReportingPeriod period = new WeekReportingPeriod(25, 2020);
        assertEquals(Duration.ofHours(40), reportingService.getWorkingDurationForPeriod(period));
    }

    @Test
    public void testGetWorkingDurationForMonth() {
        ReportingPeriod period = new MonthReportingPeriod(6, 2020);
        assertEquals(Duration.ofHours(176), reportingService.getWorkingDurationForPeriod(period));
    }

}
