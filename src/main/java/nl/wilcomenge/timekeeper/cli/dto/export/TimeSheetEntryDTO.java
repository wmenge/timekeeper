package nl.wilcomenge.timekeeper.cli.dto.export;

import lombok.Data;

@Data
public class TimeSheetEntryDTO {

    private ProjectInTimesheetEntryDTO project;

    private String date;

    private String duration;

    private String remark;

}
