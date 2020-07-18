package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;

@ShellComponent
public class UserProfileCommands {

    @Resource
    private UserProfileService userProfileService;

    @ShellMethod("Set working hours")
    public AttributedString setWorkingHours(@NonNull String workingHours) {
        UserProfile profile = userProfileService.getProfile();
        profile.setWorkingHours(DurationFormatter.getInstance().parse(workingHours));
        userProfileService.save(profile);
        return showWorkingHours();
    }

    @ShellMethod("Show working hours")
    public AttributedString showWorkingHours() {
        UserProfile profile = userProfileService.getProfile();
        if (profile.getWorkingHours() == null) {
            return ResultView.build(ResultView.MessageType.WARNING, "Working hours not set").render();
        } else {
            return ResultView.build(
                    ResultView.MessageType.INFO,
                    String.format("Working hours set to %s", DurationFormatter.getInstance().format(profile.getWorkingHours()))).render();
        }
    }

}