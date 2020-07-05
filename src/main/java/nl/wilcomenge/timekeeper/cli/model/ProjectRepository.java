package nl.wilcomenge.timekeeper.cli.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long>  {

    List<Project> findByCustomer(Customer customer);

    Project findByCustomerAndName(Customer customer, String name);
}
