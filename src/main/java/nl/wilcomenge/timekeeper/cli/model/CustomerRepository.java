package nl.wilcomenge.timekeeper.cli.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByName(String name);

}