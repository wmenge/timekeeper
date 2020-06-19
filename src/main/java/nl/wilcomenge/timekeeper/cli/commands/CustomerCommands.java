package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.CustomerRepository;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
public class CustomerCommands {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private State state;

    @ShellMethod("Add a customer.")
    public String customerAdd(@NonNull String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customerRepository.save(customer);
        return customer.getId().toString();
    }

    @ShellMethod("Change a customers name.")
    public String customerChangeName(@NonNull Long id, @NonNull String name) {
        Customer customer = customerRepository.findById(id).get();
        customer.setName(name);
        customerRepository.save(customer);
        return customer.getId().toString();
    }

    @ShellMethod("Select a customer")
    public String selectCustomer(@NonNull Long id) {
        state.setSelectedCustomer(customerRepository.findById(id).get());
        return String.format("Selected customer %d", state.getSelectedCustomer().getId());
    }

    @ShellMethod("List customers.")
    public String customerList() {

        List<Customer> customerList = customerRepository.findAll();

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");

        TableModel model = new BeanListTableModel<>(customerList, headers);
        TableBuilder tableBuilder = new TableBuilder(model);

        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        return tableBuilder.build().render(80);
    }

}
