package ee.ut.esi.group4.rentit.inventory.domain.repository;

import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantInventoryItemRepository extends JpaRepository<PlantInventoryItem, Long> {
}
