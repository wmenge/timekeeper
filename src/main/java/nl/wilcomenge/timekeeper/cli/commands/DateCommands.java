package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.time.LocalDate;

@ShellComponent
public class DateCommands {

    @Resource
    private State state;

    @ShellMethod("Go to previous day")
    public String previousDay() {
        state.setDate(state.getDate().minusDays(1));
        // Todo: generalize views
        return String.format("Date set to %s", state.getDate());
    }

    @ShellMethod("Go to next day")
    public String nextDay() {
        state.setDate(state.getDate().plusDays(1));
        return String.format("Date set to %s", state.getDate());
    }

    @ShellMethod("Set date to today")
    public String today() {
        state.setDate(LocalDate.now());
        return String.format("Date set to %s", state.getDate());
    }

    @ShellMethod("Set Date")
    public String setDate(int day, @ShellOption(defaultValue = "0")int month, @ShellOption(defaultValue = "0")int year) {
        if (month == 0) month = state.getDate().getMonthValue();
        if (year == 0) year = state.getDate().getYear();

        state.setDate(LocalDate.of(year, month, day));
        return String.format("Date set to %s", state.getDate());
    }

}