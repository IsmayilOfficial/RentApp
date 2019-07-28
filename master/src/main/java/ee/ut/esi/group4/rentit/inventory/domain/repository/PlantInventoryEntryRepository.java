package ee.ut.esi.group4.rentit.inventory.domain.repository;

import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantInventoryEntryRepository extends JpaRepository<PlantInventoryEntry, Long> {

}
