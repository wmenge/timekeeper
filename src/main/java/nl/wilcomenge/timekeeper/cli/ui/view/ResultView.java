package nl.wilcomenge.timekeeper.cli.ui.view;

import nl.wilcomenge.timekeeper.cli.ui.table.TableBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ResultView<T> {

    public enum MessageType {
        INFO, WARNING, ERROR, DEFAULT;
    }

    private String message;
    private MessageType type;
    private List list;
    private Object entry;

    public static ResultView build(MessageType type, String message) {
        return new ResultView(type, message);
    }

    public static ResultView build(MessageType type, String message, List list) {
        return new ResultView(type, message, list);
    }

    public static ResultView build(MessageType type, String message, Object entry) {
        return new ResultView(type, message, entry);
    }

    public ResultView(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ResultView(MessageType type, String message, List list) {
        this.type = type;
        this.message = message;
        this.list = list;
    }

    public ResultView(MessageType type, String message, Object entry) {
        this.type = type;
        this.message = message;
        this.entry = entry;
    }

    public AttributedString render() {
        return renderMessage();
    }

    // TODO try to remove the class parameter
    public AttributedString render(LinkedHashMap<String, Object> headers) {
        if (list == null && entry == null) {
            return renderMessage();
        } else if(list != null) {
            return AttributedString.join( new AttributedString("\n"), Arrays.asList(renderMessage(), renderTable(headers)));
        } else {
            return AttributedString.join( new AttributedString("\n"), Arrays.asList(renderMessage(), renderRecord(headers)));
        }
    }

    private AttributedString renderMessage() {
        return new AttributedString(message, getStyle(this.type));
    }

    private AttributedStyle getStyle(MessageType type) {
        switch (type) {
            case INFO:
                return AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
            case ERROR:
                return AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
            case WARNING:
                return AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW);
            default:
                return AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE);
        }
    }

    private AttributedString renderTable(LinkedHashMap<String, Object> headers) {
        return AttributedString.join( new AttributedString(""), Arrays.asList(
                new AttributedString(new TableBuilder<T>().build(this.list, headers).render(160)),
                tableMessage()));
    }

    private AttributedString tableMessage() {
        return (this.list.size() == 0) ?
                new AttributedString("No results", getStyle(MessageType.WARNING)) :
                new AttributedString(String.format("%d result(s)", this.list.size()), getStyle(MessageType.DEFAULT));
    }

    private AttributedString renderRecord(LinkedHashMap<String, Object> headers) {
        return new AttributedString(new TableBuilder<T>().build((T)this.entry, headers).render(160));
    }
}
