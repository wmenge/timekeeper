package nl.wilcomenge.timekeeper.cli.application;

import lombok.Getter;
import lombok.Setter;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.Project;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class State {

    @Getter
    private Optional<Customer> optionalCustomer = Optional.empty();

    @Getter
    private Optional<Project> optionalProject = Optional.empty();

    @Setter
    @Getter
    LocalDate date = LocalDate.now();

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.optionalCustomer = Optional.of(selectedCustomer);

        if (this.getOptionalProject().isPresent() && !selectedCustomer.equals(this.getOptionalProject().get().getCustomer())) {
            this.optionalProject = Optional.empty();
        }
    }

    public void setSelectedProject(Project selectedProject) {
        this.optionalProject = Optional.of(selectedProject);
        setSelectedCustomer(selectedProject.getCustomer());
    }
}
