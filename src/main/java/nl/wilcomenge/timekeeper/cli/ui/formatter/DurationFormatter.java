package nl.wilcomenge.timekeeper.cli.ui.formatter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DurationFormatter {

    public static String format(Duration duration) {
        List<String> components = new ArrayList<>();
        if (duration.toHours() > 0) components.add( String.format("%dh", duration.toHours()));
        if ((duration.toHours() == 0 || duration.toMinutesPart() > 0)) components.add(String.format("%dm", duration.toMinutesPart()));
        return String.join(" ", components);
    }

    public static Duration parse(String string) {
        return Duration.ofHours(1);
    }
}
