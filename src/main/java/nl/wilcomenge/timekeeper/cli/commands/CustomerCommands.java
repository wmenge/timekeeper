package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.CustomerRepository;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView.MessageType;
import org.jline.utils.AttributedString;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import java.util.List;

@ShellComponent
public class CustomerCommands {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private State state;

    @ShellMethod("Add a customer.")
    public AttributedString customerAdd(@NonNull String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customerRepository.save(customer);
        state.setSelectedCustomer(customer);
        return ResultView.build(MessageType.INFO, "Created customer", customer).render(Customer.class);
    }

    @ShellMethod("Change a customers name.")
    public AttributedString customerChangeName(@NonNull Long id, @NonNull String name) {
        Customer customer = customerRepository.findById(id).get();
        customer.setName(name);
        customerRepository.save(customer);
        return ResultView.build(MessageType.INFO, "Changed customer name", customer).render(Customer.class);
    }

    @ShellMethod("Select a customer")
    public AttributedString customerSelect(@NonNull Long id) {
        state.setSelectedCustomer(customerRepository.findById(id).get());
        return ResultView.build(MessageType.INFO, "Selected customer:", state.getSelectedCustomer()).render(Customer.class);
    }

    @ShellMethod("List customers.")
    public AttributedString customerList() {
        List<Customer> customerList = customerRepository.findAll();
        return ResultView.build(MessageType.INFO, "Showing customers:", customerList).render(Customer.class);
    }

}
