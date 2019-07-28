package ee.ut.esi.group4.rentit.inventory.application.dto;

import ee.ut.esi.group4.rentit.common.application.dto.BusinessPeriodDTO;
import ee.ut.esi.group4.rentit.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    Long _id;
    String name;
    String description;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
