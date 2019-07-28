package ee.ut.esi.group4.rentit.inventory.application.service;

import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {

    public PlantInventoryEntryAssembler() {
        super(PlantInventoryEntry.class, PlantInventoryEntryDTO.class);
    }

    @Override
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plant) {
        if (plant == null) return null;

        PlantInventoryEntryDTO dto = createResourceWithId(plant.getId(), plant);
        dto.set_id(plant.getId());
        dto.setName(plant.getName());
        dto.setDescription(plant.getDescription());
        dto.setPrice(plant.getPrice());

        return dto;
    }

    public List<PlantInventoryEntryDTO> toResources(List<PlantInventoryEntry> plants) {
        return plants.stream().map(p -> toResource(p)).collect(Collectors.toList());
    }
}
