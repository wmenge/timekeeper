package nl.wilcomenge.timekeeper.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import nl.wilcomenge.timekeeper.cli.dto.export.CustomerDTO;
import nl.wilcomenge.timekeeper.cli.dto.export.ExportData;
import nl.wilcomenge.timekeeper.cli.dto.export.TimeSheetEntryDTO;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.CustomerRepository;
import nl.wilcomenge.timekeeper.cli.model.TimeSheetEntryRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class ImportExportService {

    @Resource
    CustomerRepository customerRepository;

    @Resource
    TimeSheetEntryRepository timeSheetEntryRepository;

    //TODO Move separate class or mapping service
    private Converter<Customer, String> customerConverter = new Converter<Customer, String>() {
        @Override
        public String convert(MappingContext<Customer, String> context) {
            return context.getSource().getName();
        }
    };

    public String export() throws JsonProcessingException {
        // TODO setup with spring
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        return mapper.writeValueAsString(getExportData());
    }

    private ExportData getExportData() {
        Type customerListType = new TypeToken<List<CustomerDTO>>() {}.getType();
        Type timeSheetListType = new TypeToken<List<TimeSheetEntryDTO>>() {}.getType();
        ExportData export = new ExportData();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(customerConverter);

        export.setCustomers(modelMapper.map(customerRepository.findAll(), customerListType));
        export.setEntries(modelMapper.map(timeSheetEntryRepository.findAll(), timeSheetListType));

        return export;
    }
}
