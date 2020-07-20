package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.service.ImportExportService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class ImportExportCommands {

    @Resource
    private ImportExportService importExportService;

    @ShellMethod(value = "Export data", key = "export")
    @Transactional
    public String exportData(File file) throws IOException {
        String result = importExportService.exportData();

        // TODO: Error if exists
        file.createNewFile();
        Path path = file.toPath();
        byte[] strToBytes = result.getBytes();

        Files.write(path, strToBytes);
        return "Ok";
    }

    @ShellMethod(value = "Import data", key = "import")
    @Transactional
    public String importData(File file, @ShellOption(defaultValue = "false")Boolean confirm) throws IOException {
        if (!confirm) return "Please confirm import";
        String contents = Files.readString(file.toPath());
        importExportService.importData(contents);
        return "Ok";
    }


}
