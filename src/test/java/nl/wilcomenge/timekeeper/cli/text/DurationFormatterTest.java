package nl.wilcomenge.timekeeper.cli.text;

import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DurationFormatterTest {

    @Test
    public void formatterTest0M() {
        Duration duration = Duration.ofMinutes(0);
        assertEquals("0m", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest30M() {
        Duration duration = Duration.ofMinutes(30);
        assertEquals("30m", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest60M() {
        Duration duration = Duration.ofMinutes(60);
        assertEquals("1h", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest90M() {
        Duration duration = Duration.ofMinutes(90);
        assertEquals("1h 30m", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest120M() {
        Duration duration = Duration.ofMinutes(120);
        assertEquals("2h", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest8H() {
        Duration duration = Duration.ofMinutes(8 * 60);
        assertEquals("8h", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest24H() {
        Duration duration = Duration.ofMinutes(24 * 60);
        assertEquals("24h", DurationFormatter.getInstance().format(duration)[0]);
    }

    @Test
    public void formatterTest40H() {
        Duration duration = Duration.ofMinutes(40 * 60);
        assertEquals("40h", DurationFormatter.getInstance().format(duration)[0]);
    }

}
