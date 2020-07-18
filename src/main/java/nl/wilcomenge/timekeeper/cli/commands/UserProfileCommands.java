package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.math.BigDecimal;

@ShellComponent
public class UserProfileCommands {

    @Resource
    private UserProfileService userProfileService;

    @ShellMethod("Set working hours")
    public String setWorkingHours(@ShellOption(defaultValue = "40") @NonNull BigDecimal workingHours) {
        UserProfile profile = userProfileService.getProfile();
        profile.setWorkingHours(workingHours);
        userProfileService.save(profile);

        return String.format("Working hours set to %s", profile.getWorkingHours());
    }
}