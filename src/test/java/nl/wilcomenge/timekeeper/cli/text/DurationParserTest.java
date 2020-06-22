package nl.wilcomenge.timekeeper.cli.text;

import nl.wilcomenge.timekeeper.cli.ui.formatter.DurationFormatter;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DurationParserTest {

    /*@Test
    public void parserTestEmpty() {
        assertEquals(Duration.ofMinutes(0), DurationFormatter.parse(""));
    }

    @Test
    public void parserTest0() {
        assertEquals(Duration.ofMinutes(0), DurationFormatter.parse("0"));
    }*/

    @Test
    public void parserTest0m() {
        assertEquals(Duration.ofMinutes(0), DurationFormatter.parse("0m"));
    }

    @Test
    public void parserTest0h() {
        assertEquals(Duration.ofMinutes(0), DurationFormatter.parse("0h"));
    }

    @Test
    public void parserTest30() {
        assertEquals(Duration.ofMinutes(30), DurationFormatter.parse("30"));
    }

    @Test
    public void parserTest30m() {
        assertEquals(Duration.ofMinutes(30), DurationFormatter.parse("30m"));
    }

    @Test
    public void parserTest60m() {
        assertEquals(Duration.ofMinutes(60), DurationFormatter.parse("60m"));
    }

    @Test
    public void parserTest1h() {
        assertEquals(Duration.ofMinutes(60), DurationFormatter.parse("1h"));
    }

    @Test
    public void parserTest90m() {
        assertEquals(Duration.ofMinutes(90), DurationFormatter.parse("90m"));
    }

    @Test
    public void parserTest1h30m() {
        assertEquals(Duration.ofMinutes(90), DurationFormatter.parse("1h 30m"));
    }

    @Test
    public void parserTest1h30mNoSpaces() {
        assertEquals(Duration.ofMinutes(90), DurationFormatter.parse("1h30m"));
    }

    /*@Test
    public void parserTest1point5h() {
        assertEquals(Duration.ofMinutes(90), DurationFormatter.parse("1.5h"));
    }

    @Test
    public void parserTestOneThirty() {
        assertEquals(Duration.ofMinutes(90), DurationFormatter.parse("1:30"));
    }*/
}
