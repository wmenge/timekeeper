package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.service.UserProfileService;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import org.jline.utils.AttributedString;
import org.springframework.lang.NonNull;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Currency;

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

    @ShellMethod("Set Currency")
    public AttributedString setCurrency(@NonNull String currencyString) {
        UserProfile profile = userProfileService.getProfile();
        profile.setCurrency(Currency.getInstance(currencyString));
        userProfileService.save(profile);
        return showCurrency();
    }

    @ShellMethod("Show Currencuy")
    public AttributedString showCurrency() {
        UserProfile profile = userProfileService.getProfile();
        if (profile.getCurrency() == null) {
            return ResultView.build(ResultView.MessageType.WARNING, "Currency not set").render();
        } else {
            return ResultView.build(
                    ResultView.MessageType.INFO,
                    String.format("Currency set to %s", profile.getCurrency().getSymbol())).render();
        }
    }

    @ShellMethod("Set Hourly rate")
    @ShellMethodAvailability("rateAvailabilityCheck")
    public AttributedString setHourlyRate(@NonNull String rateString) {
        UserProfile profile = userProfileService.getProfile();
        profile.setHourlyRate(BigDecimal.valueOf(Double.parseDouble(rateString)));
        userProfileService.save(profile);
        return showHourlyRate();
    }

    @ShellMethod("Show Hourly rate")
    @ShellMethodAvailability("rateAvailabilityCheck")
    public AttributedString showHourlyRate() {
        UserProfile profile = userProfileService.getProfile();
        if (profile.getHourlyRate() == null) {
            return ResultView.build(ResultView.MessageType.WARNING, "Hourly rate not set").render();
        } else {
            return ResultView.build(
                    ResultView.MessageType.INFO,
                    String.format("Hourly rate set to %s %.2f", profile.getCurrency().getSymbol(), profile.getHourlyRate().doubleValue())).render();
        }
    }

    public Availability rateAvailabilityCheck() {
        return userProfileService.getProfile().getCurrency() != null
                ? Availability.available()
                : Availability.unavailable("no currency has been set");
    }
}