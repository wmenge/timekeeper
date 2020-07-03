package nl.wilcomenge.timekeeper.cli.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.wilcomenge.timekeeper.cli.service.ImportExportService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ShellComponent
public class ImportExportCommands {

    @Resource
    ImportExportService importExportService;

    @ShellMethod("Export data")
    @Transactional
    public String export(File file) throws IOException {

        String result = importExportService.export();

        // TODO: Error if exists
        file.createNewFile();
        Path path = file.toPath();
        byte[] strToBytes = result.getBytes();

        Files.write(path, strToBytes);

        return "Ok";

    }

}
