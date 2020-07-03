package nl.wilcomenge.timekeeper.cli.dto.export;

import lombok.Data;

import java.util.Collection;

@Data
public class ExportData {

    // TODO: Modify to stream?
    private Collection<CustomerDTO> customers;

    private Collection<TimeSheetEntryDTO> entries;

}
