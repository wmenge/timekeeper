package nl.wilcomenge.timekeeper.cli.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Data
//name should be unique within a tenant
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name", "customer_id"})
})
public class Project {

    public Project() { }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Customer customer;

    @NotNull
    @Size(min=1)
    private String name;

    private Boolean billable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
