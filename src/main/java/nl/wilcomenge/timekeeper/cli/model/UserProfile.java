package nl.wilcomenge.timekeeper.cli.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Data
public class UserProfile {

    public static final String EUR_CURRENCY_CODE = "EUR";

    public UserProfile() {

    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private BigDecimal fulltimeFactor;

    private Currency currency = Currency.getInstance(EUR_CURRENCY_CODE);

    private BigDecimal hourlyRate;

}
