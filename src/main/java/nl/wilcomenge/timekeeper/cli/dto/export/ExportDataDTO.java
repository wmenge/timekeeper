package nl.wilcomenge.timekeeper.cli.dto.export;

import lombok.Data;

import java.util.Collection;

@Data
public class ExportDataDTO {

    private Collection<HolidayDTO> holidays;

    private Collection<CustomerDTO> customers;

    private Collection<TimeSheetEntryDTO> entries;

}
