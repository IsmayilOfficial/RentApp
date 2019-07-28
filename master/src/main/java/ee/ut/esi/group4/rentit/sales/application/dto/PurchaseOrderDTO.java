package ee.ut.esi.group4.rentit.sales.application.dto;

import ee.ut.esi.group4.rentit.common.application.dto.BusinessPeriodDTO;
import ee.ut.esi.group4.rentit.common.rest.ResourceSupport;
import ee.ut.esi.group4.rentit.sales.domain.model.POStatus;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import lombok.Data;

@Data
public class PurchaseOrderDTO extends ResourceSupport {
    Long _id;
    BusinessPeriodDTO rentalPeriod;
    PlantInventoryEntryDTO plant;
    POStatus status;
}
