package nl.wilcomenge.timekeeper.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import nl.wilcomenge.timekeeper.cli.dto.export.CustomerDTO;
import nl.wilcomenge.timekeeper.cli.dto.export.ExportData;
import nl.wilcomenge.timekeeper.cli.dto.export.TimeSheetEntryDTO;
import nl.wilcomenge.timekeeper.cli.model.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class ImportExportService {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private TimeSheetEntryRepository timeSheetEntryRepository;

    // TODO setup with spring?
    private ObjectMapper serializeMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

    private ModelMapper modelMapper = null;

    //TODO Move separate class or mapping service
    private Converter<Customer, String> customerConverter = new Converter<Customer, String>() {
        @Override
        public String convert(MappingContext<Customer, String> context) {
            return context.getSource().getName();
        }
    };

    private Converter<TimeSheetEntryDTO, TimeSheetEntry> entryDTOConverter = new Converter<TimeSheetEntryDTO, TimeSheetEntry>() {
        @Override
        public TimeSheetEntry convert(MappingContext<TimeSheetEntryDTO, TimeSheetEntry> context) {

            TimeSheetEntryDTO source = context.getSource();
            TimeSheetEntry destination = context.getDestination();

            if (destination == null) destination = new TimeSheetEntry();

            // TODO: Throw a scary exception when cust or project cannot be found
            Customer customer = customerRepository.findByName(source.getProject().getCustomer());
            Project project = projectRepository.findByCustomerAndName(customer, source.getProject().getName());

            destination.setProject(project);
            destination.setDate(LocalDate.parse(source.getDate()));
            destination.setDuration(Duration.parse(source.getDuration()));
            destination.setRemark(source.getRemark());

            return destination;
        }
    };

    public String exportData() throws JsonProcessingException {
        return getSerializeMapper().writeValueAsString(getExportData());
    }

    private ExportData getExportData() {
        Type customerListType = new TypeToken<List<CustomerDTO>>() {}.getType();
        Type timeSheetListType = new TypeToken<List<TimeSheetEntryDTO>>() {}.getType();

        ExportData export = new ExportData();

        export.setCustomers(getModelMapper().map(customerRepository.findAll(), customerListType));
        export.setEntries(getModelMapper().map(timeSheetEntryRepository.findAll(), timeSheetListType));

        return export;
    }

    // TODO: Rollback transaction in case of errors during import
    @Transactional
    public void importData(String importContent) throws JsonProcessingException {
        // TODO: read stream instead of string
        // TODO: Validate that export structure contains (valid) data
        ExportData export = getSerializeMapper().readValue(importContent, ExportData.class);

        removeData();
        recreateData(export);
    }

    /***
     * removes all data, does not reset id generators
     */
    private void removeData() {
        timeSheetEntryRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    private void recreateData(ExportData data) {

        Type customerListType = new TypeToken<List<Customer>>() {}.getType();
        Type entryListType = new TypeToken<List<TimeSheetEntry>>() {}.getType();

        Collection<Customer> customerCollection = getModelMapper().map(data.getCustomers(), customerListType);

        // Correct relation customer -> project
        customerCollection.forEach(c -> {
            c.getProjects().forEach(p -> {
                p.setCustomer(c);
            });
        });

        customerRepository.saveAll(customerCollection);

        Collection<TimeSheetEntry> timeSheetEntryCollection = getModelMapper().map(data.getEntries(), entryListType);

        timeSheetEntryRepository.saveAll(timeSheetEntryCollection);
    }

    private ObjectMapper getSerializeMapper() {
        return serializeMapper;
    }

    private ModelMapper getModelMapper() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            modelMapper.addConverter(customerConverter);
            modelMapper.addConverter(entryDTOConverter);
        }
        return modelMapper;
    }
}
