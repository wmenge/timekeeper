package nl.wilcomenge.timekeeper.cli.application;

import lombok.Data;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.Project;
import org.springframework.stereotype.Component;

@Component
@Data
public class State {

    // TODO optionals
    Customer selectedCustomer;

    Project selectedProject;

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;

        if (this.getSelectedProject() != null && !selectedCustomer.equals(this.getSelectedProject().getCustomer())) {
            this.selectedProject = null;
        }
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        setSelectedCustomer(selectedProject.getCustomer());
    }
}
