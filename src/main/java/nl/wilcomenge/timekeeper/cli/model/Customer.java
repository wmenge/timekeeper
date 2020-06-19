package nl.wilcomenge.timekeeper.cli.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class Customer {

    public Customer() { }

    // TODO move stuff to abstract base class?
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(unique=true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
