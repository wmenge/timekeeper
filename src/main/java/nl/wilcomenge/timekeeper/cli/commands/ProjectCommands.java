package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Project;
import nl.wilcomenge.timekeeper.cli.model.ProjectRepository;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView.MessageType;
import org.jline.utils.AttributedString;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import java.util.List;

@ShellComponent
public class ProjectCommands {

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private State state;

    @ShellMethod("Add a project.")
    public AttributedString projectAdd(@NonNull String name) {
        Project project = new Project();
        project.setName(name);
        project.setCustomer(state.getOptionalCustomer().get());
        projectRepository.save(project);
        state.setSelectedProject(project);
        return ResultView.build(MessageType.INFO, "Created project", project).render(Project.class);
    }

    public Availability projectAddAvailability() {
        return state.getOptionalCustomer().isPresent()
                ? Availability.available()
                : Availability.unavailable("no customer selected");
    }

    @ShellMethod("Change a Project name.")
    public AttributedString projectChangeName(@NonNull Long id, @NonNull String name) {
        Project project = projectRepository.findById(id).get();
        project.setName(name);
        projectRepository.save(project);
        return ResultView.build(MessageType.INFO, "Changed project name", project).render(Project.class);
    }

    @ShellMethod("Select a project.")
    public void projectSelect(@NonNull Long id) {
        state.setSelectedProject(projectRepository.findById(id).get());
    }

    @ShellMethod("List projects.")
    public AttributedString projectList(boolean showAll) {
        List<Project> projectList = (showAll || state.getOptionalCustomer().isEmpty()) ?
            projectRepository.findAll(Sort.by(Sort.Direction.ASC, "customer.id")) : projectRepository.findByCustomer(state.getOptionalCustomer().get());

        return ResultView.build(MessageType.INFO, "Showing project:", projectList).render(Project.class);

    }
}
