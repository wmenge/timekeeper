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

        List<String> components = new ArrayList<>();

        if (state.getDate() != null) {
            components.add(state.getDate().toString());
        }

        if (state.getSelectedCustomer() != null) {
            components.add(state.getSelectedCustomer().getName());
        }

        if (state.getSelectedProject() != null) {
            components.add(state.getSelectedProject().getName());
        }

        return new AttributedString(String.join(">", components) + ":>");
    }


}