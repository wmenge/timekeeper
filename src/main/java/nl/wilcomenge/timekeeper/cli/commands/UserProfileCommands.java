package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import java.math.BigDecimal;

@ShellComponent
public class UserProfileCommands {

    @Resource
    private UserProfileService userProfileService;

    @ShellMethod("Set fulltime percentage")
    public AttributedString setFulltimePercentage(@NonNull String fullTimePercentageString) {
        UserProfile profile = userProfileService.getProfile();
        profile.setFulltimeFactor(BigDecimal.valueOf(Double.parseDouble(fullTimePercentageString) / 100));
        userProfileService.save(profile);
        return showFulltimePercentage();
    }

    @ShellMethod("Show fulltime percentage")
    public AttributedString showFulltimePercentage() {
        UserProfile profile = userProfileService.getProfile();
        if (profile.getFulltimeFactor() == null) {
            return ResultView.build(ResultView.MessageType.WARNING, "Fulltime percentage not set").render();
        } else {
            return ResultView.build(
                    ResultView.MessageType.INFO,
                    String.format("Fulltime percentage set to %.2f %%", profile.getFulltimeFactor().doubleValue()
                            * 100)).render();
        }
    }
}