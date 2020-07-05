package nl.wilcomenge.timekeeper.cli.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {

    List<TimeSheetEntry> findByDate(LocalDate date);

    List<TimeSheetEntry> findByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

}
