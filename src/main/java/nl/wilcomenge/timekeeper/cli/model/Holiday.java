package nl.wilcomenge.timekeeper.cli.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Holiday {

    public Holiday() {
    }

    @Id
    LocalDate date;

    @NonNull
    String name;
}
