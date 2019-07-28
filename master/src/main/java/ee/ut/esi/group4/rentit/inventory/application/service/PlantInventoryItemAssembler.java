package ee.ut.esi.group4.rentit.inventory.application.service;

import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryItemDTO;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantInventoryItemAssembler extends ResourceAssemblerSupport<PlantInventoryItem, PlantInventoryItemDTO> {

    public PlantInventoryItemAssembler() {
        super(PlantInventoryEntry.class, PlantInventoryItemDTO.class);
    }

    @Override
    public PlantInventoryItemDTO toResource(PlantInventoryItem item) {
        if (item == null) return null;

        PlantInventoryItemDTO dto = createResourceWithId(item.getId(), item);
        dto.set_id(item.getId());
        dto.setName(item.getPlantInfo().getName());
        dto.setDescription(item.getPlantInfo().getDescription());
        dto.setPrice(item.getPlantInfo().getPrice());
        dto.setSerialNumber(item.getSerialNumber());
        dto.setEquipmentCondition(item.getEquipmentCondition());
        dto.setPlantEntry_Id(item.getPlantInfo().getId());

        return dto;
    }

    public List<PlantInventoryItemDTO> toResources(List<PlantInventoryItem> plants) {
        return plants.stream().map(p -> toResource(p)).collect(Collectors.toList());
    }
}
