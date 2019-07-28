package ee.ut.esi.group4.rentit.sales.application.service;

import ee.ut.esi.group4.rentit.common.application.service.BusinessPeriodValidator;
import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.application.service.PlantInventoryEntryAssembler;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantReservation;
import ee.ut.esi.group4.rentit.inventory.domain.repository.InventoryRepository;
import ee.ut.esi.group4.rentit.inventory.domain.repository.PlantInventoryEntryRepository;
import ee.ut.esi.group4.rentit.inventory.domain.repository.PlantInventoryItemRepository;
import ee.ut.esi.group4.rentit.inventory.domain.repository.PlantReservationRepository;
import ee.ut.esi.group4.rentit.sales.application.dto.PurchaseOrderDTO;
import ee.ut.esi.group4.rentit.sales.domain.model.POStatus;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import ee.ut.esi.group4.rentit.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    PlantReservationRepository plantReservationRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    public PurchaseOrderDTO fetchPurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);

        if (po == null) {
            return null;
        }

        PurchaseOrderDTO pod = purchaseOrderAssembler.toResource(po);

        return pod;
    }

    public PurchaseOrderDTO createPO(PurchaseOrderDTO poDTO) throws Exception {
        BusinessPeriod period = BusinessPeriod.of(poDTO.getRentalPeriod().getStartDate(), poDTO.getRentalPeriod().getEndDate());

        DataBinder binder = new DataBinder(period);
        binder.addValidators(new BusinessPeriodValidator());
        binder.validate();

        if (binder.getBindingResult().hasErrors()) {
            throw new Exception("invalid period");
        }

        if (poDTO.getPlant() == null) {
            throw new Exception("invalid plant");
        }

        PlantInventoryEntry plant = plantInventoryEntryRepository.findById(poDTO.getPlant().get_id()).orElse(null);

        if (plant == null) {
            throw new Exception("plant not found");
        }

        PurchaseOrder po =  PurchaseOrder.of(plant, period);
        allocatePlantToPO(po);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }


    public PurchaseOrderDTO preallocateplantPO(Long id) throws Exception {
        PurchaseOrder po = checkPO(id, POStatus.PENDING, "can't preallocate non-pending purchase order");

        allocatePlantToPO(po);

        po.setStatus(POStatus.PREALLOCATED);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }

    public List<PurchaseOrderDTO> findpendingPOs(){
        List<PurchaseOrder> pos = purchaseOrderRepository.findByStatus(POStatus.PENDING);

        return purchaseOrderAssembler.toResources(pos);
    }

    public List<PurchaseOrderDTO> findpreallocatedPOs(){
        List<PurchaseOrder> pos = purchaseOrderRepository.findByStatus(POStatus.PREALLOCATED);

        return purchaseOrderAssembler.toResources(pos);
    }

    public PurchaseOrderDTO acceptPO(Long id) throws Exception {
        PurchaseOrder po = checkPO(id, POStatus.PENDING,"can't accept non-pending purchase order");

        po.setStatus(POStatus.OPEN);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }

    public PurchaseOrderDTO rejectPreAllocation(Long id) throws Exception {
        PurchaseOrder po = checkPO(id, POStatus.PREALLOCATED,  "Can't reject non-preallocated purchase order");

        return rejectPO(po);
    }

    public PurchaseOrderDTO confirmPreAllocation(Long id) throws Exception {
        PurchaseOrder po = checkPO(id, POStatus.PREALLOCATED,  "Can't reject non-preallocated purchase order");

        po.setStatus(POStatus.OPEN);
        po.setTotal(computePOPrice(po));
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }

    public PurchaseOrderDTO rejectPendingPO(Long id) throws Exception {
        PurchaseOrder po = checkPO(id, POStatus.PENDING,  "Can't reject non-pending purchase order");

        return rejectPO(po);
    }

    private PurchaseOrderDTO rejectPO(PurchaseOrder po) {
        while (!po.getReservations().isEmpty()) {
            plantReservationRepository.delete(po.getReservations().remove(0));
        }

        po.setStatus(POStatus.REJECTED);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }


    private PurchaseOrder checkPO(Long id, POStatus status, String invalidStateExceptionText) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);

        if (po == null) {
            throw new Exception("invalid purchase order");
        }

        if(status != null){
            if (po.getStatus() != status) {
                throw new Exception(invalidStateExceptionText);
            }
        }

        return po;

    }

    private void allocatePlantToPO(PurchaseOrder po) throws Exception {
        List<PlantInventoryItem> availableItems = inventoryRepository.findAvailableItems(
                po.getPlant().getName(),
                po.getPeriod());

        if (availableItems.size() == 0) {
            po.setStatus(POStatus.REJECTED);
            throw new Exception("no available items");
        }

        PlantReservation reservation = new PlantReservation();
        reservation.setSchedule(po.getPeriod());
        reservation.setPlant(availableItems.get(0));
        po.getReservations().add(reservation);

        plantReservationRepository.save(reservation);
    }

    public PurchaseOrderDTO directPlantAllocation(Long po_id, Long item_id) throws Exception {
        PurchaseOrder po = checkPO(po_id, POStatus.PENDING, "Can't reject non-pending purchase order");

        PlantInventoryItem piItem = plantInventoryItemRepository.findById(item_id).orElse(null);

        if (piItem == null) {
            throw new Exception("Invalid Plant Inventory Item");
        }

        PlantReservation reservation = new PlantReservation();
        reservation.setSchedule(po.getPeriod());
        reservation.setPlant(piItem);
        po.getReservations().add(reservation);
        po.setTotal(computePOPrice(po));
        plantReservationRepository.save(reservation);

        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toResource(po);
    }
    
    private BigDecimal computePOPrice(PurchaseOrder po) {
        List<PlantReservation> reservations = po.getReservations();

        BigDecimal total = new BigDecimal(0);;
        reservations.stream().map(c -> c.getPlant().getPlantInfo().getPrice().add(total));
        return  total;
    }
}
