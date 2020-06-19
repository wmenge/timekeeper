package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Project;
import nl.wilcomenge.timekeeper.cli.model.ProjectRepository;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntry;
import nl.wilcomenge.timekeeper.cli.ui.table.TableBuilder;
import org.springframework.lang.NonNull;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableModel;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
public class ProjectCommands {

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private State state;

    @ShellMethod("Add a project.")
    public String projectAdd(@NonNull String name) {
        Project project = new Project();
        project.setName(name);
        project.setCustomer(state.getSelectedCustomer());
        projectRepository.save(project);
        return project.getId().toString();
    }

    public Availability projectAddAvailability() {
        return state.getSelectedCustomer() != null
                ? Availability.available()
                : Availability.unavailable("no customer selected");
    }

    @ShellMethod("Select a project.")
    public String projectSelect(@NonNull Long id) {
        state.setSelectedProject(projectRepository.findById(id).get());
        return String.format("Selected project %d", state.getSelectedProject().getId());
    }

    @ShellMethod("List projects.")
    public String projectList() {
        List<Project> projectList = projectRepository.findAll();
        return new TableBuilder<Project>().build(projectList, Project.class).render(80);
    }

    @ShellMethod("Change a Project name.")
    public String projectChangeName(@NonNull Long id, @NonNull String name) {
        Project project = projectRepository.findById(id).get();
        project.setName(name);
        projectRepository.save(project);
        return project.getId().toString();
    }
}
