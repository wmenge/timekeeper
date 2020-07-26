package nl.wilcomenge.timekeeper.cli.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import nl.wilcomenge.timekeeper.cli.dto.export.*;
import nl.wilcomenge.timekeeper.cli.model.*;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class ImportExportService {

    public enum Format {
        JSON,
        YAML
    }

    @Resource
    private UserProfileService userProfileService;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private TimeSheetEntryRepository timeSheetEntryRepository;

    @Resource
    private HolidayRepository holidayRepository;

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

    private Converter<String, LocalDate> localDateConverter = new Converter<>() {
        @Override
        public LocalDate convert(MappingContext<String, LocalDate> context) {
            return LocalDate.parse(context.getSource());
        }
    };

    public Format getFormat(File file) {
        return Format.valueOf(FilenameUtils.getExtension(file.getName()).toUpperCase());
    }

    public String exportData(Format format) throws JsonProcessingException {
        return getSerializeMapper(format).writeValueAsString(getExportData());
    }

    private ExportDataDTO getExportData() {
        Type holidayListType = new TypeToken<List<HolidayDTO>>() {}.getType();
        Type customerListType = new TypeToken<List<CustomerDTO>>() {}.getType();
        Type timeSheetListType = new TypeToken<List<TimeSheetEntryDTO>>() {}.getType();

        ExportDataDTO export = new ExportDataDTO();

        export.setUserProfile(getModelMapper().map(userProfileService.getProfile(), UserProfileDTO.class));
        export.setHolidays(getModelMapper().map(holidayRepository.findAll(), holidayListType));
        export.setCustomers(getModelMapper().map(customerRepository.findAll(), customerListType));
        export.setEntries(getModelMapper().map(timeSheetEntryRepository.findAll(), timeSheetListType));

        return export;
    }

    @Transactional
    public void importData(String importContent, Format format) throws JsonProcessingException {
        // TODO: read stream instead of string
        // TODO: Validate that export structure contains (valid) data
        ExportDataDTO export = getSerializeMapper(format).readValue(importContent, ExportDataDTO.class);

        removeData();
        recreateData(export);
    }

    /***
     * removes all data, does not reset id generators
     */
    private void removeData() {
        holidayRepository.deleteAllInBatch();
        timeSheetEntryRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    private void recreateData(ExportDataDTO data) {

        Type holidayListType = new TypeToken<List<Holiday>>() {}.getType();
        Type customerListType = new TypeToken<List<Customer>>() {}.getType();
        Type entryListType = new TypeToken<List<TimeSheetEntry>>() {}.getType();

        UserProfile userProfile = userProfileService.getProfile();
        getModelMapper().map(data.getUserProfile(), userProfile);
        userProfileService.save(userProfile);

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

        Collection<Holiday> holidayCollection = getModelMapper().map(data.getHolidays(), holidayListType);
        holidayRepository.saveAll(holidayCollection);
    }

    public String exportHolidayData(Format format) throws JsonProcessingException {
        Type holidayListType = new TypeToken<List<HolidayDTO>>() {}.getType();
        return getSerializeMapper(format).writeValueAsString(getModelMapper().map(holidayRepository.findAll(),holidayListType));
    }

    @Transactional
    public void importHolidayData(String importContent, Format format) throws JsonProcessingException {
        // TODO: read stream instead of string
        // TODO: Validate that export structure contains (valid) data
        Type holidayListType = new TypeToken<List<HolidayDTO>>() {}.getType();
        List<HolidayDTO> export = getSerializeMapper(format).readValue(importContent, List.class);
        recreateHolidayData(export);
    }

    private void recreateHolidayData(List<HolidayDTO> data) {
        Type holidayListType = new TypeToken<List<Holiday>>() {}.getType();
        Collection<Holiday> holidayCollection = getModelMapper().map(data, holidayListType);
        holidayRepository.saveAll(holidayCollection);
    }

    private ObjectMapper getSerializeMapper(Format format) {
        switch (format) {
            case JSON:
                return new ObjectMapper(new JsonFactory()).enable(SerializationFeature.INDENT_OUTPUT);
            case YAML:
                return new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        }

        return null;
    }

    private ModelMapper getModelMapper() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            modelMapper.addConverter(customerConverter);
            modelMapper.addConverter(entryDTOConverter);
            modelMapper.addConverter(localDateConverter);
        }
        return modelMapper;
    }
}
