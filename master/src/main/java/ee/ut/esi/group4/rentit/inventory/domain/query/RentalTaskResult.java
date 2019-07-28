package ee.ut.esi.group4.rentit.inventory.domain.query;

import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import lombok.Value;

@Value
public class RentalTaskResult {
    PlantInventoryItem plantInventoryItem;
    Long rentals;
    Long tasks;
}

