package nl.wilcomenge.timekeeper.cli.dto.export;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class UserProfileDTO {
    private BigDecimal fulltimeFactor;

    private String currency;

    private BigDecimal hourlyRate;
}
