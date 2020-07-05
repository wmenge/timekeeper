package nl.wilcomenge.timekeeper.cli.commands;

import nl.wilcomenge.timekeeper.cli.application.State;
import nl.wilcomenge.timekeeper.cli.model.Customer;
import nl.wilcomenge.timekeeper.cli.model.CustomerRepository;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView;
import nl.wilcomenge.timekeeper.cli.ui.view.ResultView.MessageType;
import org.jline.utils.AttributedString;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@ShellComponent
public class CustomerCommands {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private State state;

    @ShellMethod("Add a customer.")
    public AttributedString addCustomer(@NonNull String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customerRepository.save(customer);
        state.setSelectedCustomer(customer);
        return ResultView.build(MessageType.INFO, "Created customer", customer).render(Customer.class);
    }

    @ShellMethod("Change a customers name.")
    public AttributedString changeCustomer(@NonNull Long id, @NonNull String name) {
        Customer customer = customerRepository.findById(id).get();
        customer.setName(name);
        customerRepository.save(customer);
        return ResultView.build(MessageType.INFO, "Changed customer name", customer).render(Customer.class);
    }

    @ShellMethod("Select a customer")
    public void selectCustomer(@NonNull Long id) {
        state.setSelectedCustomer(customerRepository.findById(id).get());
    }

    @ShellMethod("List customers.")
    public AttributedString listCustomer() {
        List<Customer> customerList = customerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return ResultView.build(MessageType.INFO, "Showing customers:", customerList).render(Customer.class);
    }

}
