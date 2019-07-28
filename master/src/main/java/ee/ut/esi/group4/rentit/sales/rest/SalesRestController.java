package ee.ut.esi.group4.rentit.sales.rest;

import ee.ut.esi.group4.rentit.common.application.exception.PlantNotFoundException;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryItemDTO;
import ee.ut.esi.group4.rentit.inventory.application.service.InventoryService;
import ee.ut.esi.group4.rentit.sales.application.service.PurchaseOrderAssembler;
import ee.ut.esi.group4.rentit.sales.application.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    SalesService salesService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
        @RequestParam(name = "name") String plantName,
        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return inventoryService.findAvailablePlants(plantName, startDate, endDate);
    }

    @GetMapping("/findapplicableplants")
    public List<PlantInventoryItemDTO> findApplicablePlants(
            @RequestParam(name = "entryId") Long entry_id,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return inventoryService.findApplicablePlants(entry_id, startDate, endDate);
    }





    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(PlantNotFoundException ex) {
    }
}

