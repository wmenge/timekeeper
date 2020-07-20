package nl.wilcomenge.timekeeper.service;

import nl.wilcomenge.timekeeper.cli.dto.reporting.period.MonthReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.ReportingPeriod;
import nl.wilcomenge.timekeeper.cli.dto.reporting.period.WeekReportingPeriod;
import nl.wilcomenge.timekeeper.cli.model.Holiday;
import nl.wilcomenge.timekeeper.cli.model.HolidayRepository;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.service.ReportingService;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ReportingServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public ReportingService reportingService() {
            return new ReportingService();
        }
    }

    @Autowired
    ReportingService reportingService;

    @MockBean
    TimeSheetEntryRepository timeSheetEntryRepository;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    HolidayRepository holidayRepository;

    @BeforeEach
    public void setUp() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFulltimeFactor(BigDecimal.ONE);
        Mockito.when(userProfileService.getProfile()).thenReturn(userProfile);
        Mockito.when(holidayRepository.findAll()).thenReturn(Collections.emptyList());
    }

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


    @Test
    public void testGetWorkingDurationForWeekPartTimeFactor() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFulltimeFactor(BigDecimal.valueOf(0.9));
        Mockito.when(userProfileService.getProfile()).thenReturn(userProfile);

        ReportingPeriod period = new WeekReportingPeriod(25, 2020);
        assertEquals(Duration.ofHours(36), reportingService.getWorkingDurationForPeriod(period));
    }
    
    @Test
    public void testGetWorkingDurationForWeekWithHoliday() {
        // setup a holiday
        Holiday holiday = new Holiday();
        holiday.setDate(LocalDate.of(2020, 1, 1));
        holiday.setName("Some holiday");
        Mockito.when(holidayRepository.findAll()).thenReturn(Collections.singletonList(holiday));

        ReportingPeriod period = new WeekReportingPeriod(1, 2020);
        assertEquals(Duration.ofHours(32), reportingService.getWorkingDurationForPeriod(period));
    }

}
