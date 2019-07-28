package ee.ut.esi.group4.rentit.inventory.application.dto;

import ee.ut.esi.group4.rentit.common.rest.ResourceSupport;
import ee.ut.esi.group4.rentit.maintenance.domain.model.EquipmentCondition;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
public class PlantInventoryItemDTO extends ResourceSupport {
    Long _id;
    String name;
    String description;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;

    Long plantEntry_Id;
}
