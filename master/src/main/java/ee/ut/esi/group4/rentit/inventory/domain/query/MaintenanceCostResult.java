package ee.ut.esi.group4.rentit.inventory.domain.query;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class MaintenanceCostResult {
    Integer year;
    BigDecimal cost;
}

