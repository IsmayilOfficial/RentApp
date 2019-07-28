package ee.ut.esi.group4.rentit.inventory.domain.repository;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import ee.ut.esi.group4.rentit.inventory.domain.query.MaintenanceCostResult;
import ee.ut.esi.group4.rentit.inventory.domain.query.RentalTaskResult;
import org.springframework.data.util.Pair;

import java.util.List;

public interface CustomInventoryRepository {
    List<PlantInventoryItem> findAvailableItems(String name, BusinessPeriod period);
    List<Pair<PlantInventoryEntry, Long>> findAvailablePlants(String name, BusinessPeriod businessPeriod);
    List<PlantInventoryItem> findApplicablePlants(Long entry_id, BusinessPeriod businessPeriod);
    List<PlantInventoryItem> findPlantsUnmaintainedFor(Integer days);
    List<MaintenanceCostResult> findMaintenanceCostPerYearForPast(Integer years);
    List<RentalTaskResult> findPlantsByPeriodByRentalsAndTasks(BusinessPeriod period);
}
