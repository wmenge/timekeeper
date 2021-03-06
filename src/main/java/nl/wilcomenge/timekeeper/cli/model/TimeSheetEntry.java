package nl.wilcomenge.timekeeper.cli.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
public class TimeSheetEntry {

    public TimeSheetEntry() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    private Project project;

    @NonNull
    private LocalDate date;

    @NonNull
    private Duration duration;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSheetEntry that = (TimeSheetEntry) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
