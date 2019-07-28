package ee.ut.esi.group4.rentit.inventory.application.dto;

import ee.ut.esi.group4.rentit.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}
