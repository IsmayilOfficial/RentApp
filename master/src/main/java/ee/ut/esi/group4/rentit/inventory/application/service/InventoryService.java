package ee.ut.esi.group4.rentit.inventory.application.service;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryItemDTO;
import ee.ut.esi.group4.rentit.inventory.domain.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    public List<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return plantInventoryEntryAssembler.toResources(
                inventoryRepository
                .findAvailablePlants(
                        name,
                        BusinessPeriod.of(startDate, endDate)
                )
                .stream()
                .map(p -> p.getFirst())
                .collect(Collectors.toList())
        );
    }

    public List<PlantInventoryItemDTO> findApplicablePlants(Long entry_id, LocalDate startDate, LocalDate endDate ){
        return plantInventoryItemAssembler.toResources(
                inventoryRepository
                .findApplicablePlants(
                       entry_id,
                       BusinessPeriod.of(startDate, endDate)
                )
        );
    }
}
