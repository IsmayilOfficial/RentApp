package ee.ut.esi.group4.rentit.sales.application.service;

import ee.ut.esi.group4.rentit.common.application.dto.BusinessPeriodDTO;
import ee.ut.esi.group4.rentit.sales.application.dto.PurchaseOrderDTO;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import ee.ut.esi.group4.rentit.inventory.application.service.PlantInventoryEntryAssembler;
import ee.ut.esi.group4.rentit.sales.rest.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(PurchaseOrder.class, PurchaseOrderDTO.class);
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrder po) {
        if (po == null) return null;

        PurchaseOrderDTO dto = createResourceWithId(po.getId(), po);
        dto.set_id(po.getId());
        dto.setStatus(po.getStatus());
        dto.setPlant(plantInventoryEntryAssembler.toResource(po.getPlant()));
        dto.setRentalPeriod(BusinessPeriodDTO.of(po.getPeriod().getStartDate(), po.getPeriod().getEndDate()));

        try {
            switch (po.getStatus()) {
                case PENDING:
                    dto.add(linkTo(methodOn(SalesRestController.class)
                        .acceptPurchaseOrder(dto.get_id())).withRel("accept"));
                    dto.add(linkTo(methodOn(SalesRestController.class)
                        .rejectPurchaseOrder(dto.get_id())).withRel("reject"));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {}

        return dto;
    }

    public List<PurchaseOrderDTO> toResources(List<PurchaseOrder> pos) {
        return pos.stream().map(po -> toResource(po)).collect(Collectors.toList());
    }
}
