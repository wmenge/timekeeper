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
        return Duration.parse(normalize(string));
    }

    // https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-
    private static String normalize(String string) {
        if (string == null || string.length() == 0 || "0".equals(string)) return "0m";

        // If no unit has been given, assume one
        if (isInteger(string)) {
            string += Integer.parseInt(string) < 10 ? "h" : "m";
        }

        return "PT" + string.replace(" ", "").toUpperCase();
    }

    private static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
