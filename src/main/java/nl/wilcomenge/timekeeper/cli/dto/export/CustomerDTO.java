package nl.wilcomenge.timekeeper.cli.dto.export;

import lombok.Data;

import java.util.Collection;

@Data
public class CustomerDTO {

    private String name;

    private Collection<ProjectDTO> projects;
}
