package nl.wilcomenge.timekeeper.cli.ui;

import nl.wilcomenge.timekeeper.cli.application.State;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomPromptProvider implements PromptProvider {

    @Resource
    private State state;

    @Override
    public AttributedString getPrompt() {

        List<AttributedString> components = new ArrayList<>();

        if (state.getDate() != null) {
            components.add(new AttributedString(state.getDate().toString(), AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)));
        }

        if (state.getSelectedCustomer() != null) {
            components.add(new AttributedString(state.getSelectedCustomer().getName(), AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)));
        }

        if (state.getSelectedProject() != null) {
            components.add(new AttributedString(state.getSelectedProject().getName(), AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)));
        }

        AttributedString separator = new AttributedString(" -> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

        return AttributedString.join(new AttributedString(""), AttributedString.join(separator, components), new AttributedString(" $ "));
    }

}