package ee.ut.esi.group4.rentit.inventory.web;

import ee.ut.esi.group4.rentit.sales.application.dto.PurchaseOrderDTO;
import ee.ut.esi.group4.rentit.sales.application.service.SalesService;
import ee.ut.esi.group4.rentit.inventory.application.dto.CatalogQueryDTO;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import ee.ut.esi.group4.rentit.inventory.application.service.InventoryService;
import ee.ut.esi.group4.rentit.inventory.domain.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    SalesService salesService;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @PostMapping("/catalog/query")
    public String postQueryForm(CatalogQueryDTO query, Model model) throws Exception {
        List<PlantInventoryEntryDTO> plants = inventoryService.findAvailablePlants(
                query.getName(),
                query.getRentalPeriod().getStartDate(),
                query.getRentalPeriod().getEndDate()
        );

        model.addAttribute("plants", plants);

        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setRentalPeriod(query.getRentalPeriod());

        if (!plants.isEmpty()) {
            po.setPlant(plants.get(0));
            salesService.createPO(po);
        }

        model.addAttribute("po", po);

        return "dashboard/catalog/query-result";
    }
}
