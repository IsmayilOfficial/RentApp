package ee.ut.esi.group4.rentit.sales.rest;

import ee.ut.esi.group4.rentit.inventory.application.service.InventoryService;
import ee.ut.esi.group4.rentit.sales.application.dto.PurchaseOrderDTO;
import ee.ut.esi.group4.rentit.sales.application.service.PurchaseOrderAssembler;
import ee.ut.esi.group4.rentit.sales.application.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/po")
public class PurchaseOrderRestController {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    SalesService salesService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @GetMapping("/pendingpos")
    public List<PurchaseOrderDTO> findPendingPurchaseOrder() {
        return salesService.findpendingPOs();
    }

    @GetMapping("/findpreallocatedpos")
    public List<PurchaseOrderDTO> findPreAllocatedPurchaseOrder() {
        return salesService.findpreallocatedPOs();
    }

    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") Long id) {
        return salesService.fetchPurchaseOrder(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws Exception {
        PurchaseOrderDTO newPODTO = salesService.createPO(partialPODTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(newPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(newPODTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<PurchaseOrderDTO> acceptPurchaseOrder(@PathVariable Long id) throws Exception {
        PurchaseOrderDTO acceptedPODTO = salesService.acceptPO(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(acceptedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(acceptedPODTO, headers, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}/reject")
    public ResponseEntity<PurchaseOrderDTO> rejectPurchaseOrder(@PathVariable Long id) throws Exception {
        PurchaseOrderDTO acceptedPODTO = salesService.rejectPendingPO(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(acceptedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(acceptedPODTO, headers, HttpStatus.CONFLICT);
    }

    @PostMapping("/order/{id}/confirmpreallocate")
    public ResponseEntity<PurchaseOrderDTO> confirmPreallocate(@PathVariable Long id) throws Exception {

        PurchaseOrderDTO confirmedPODTO = salesService.confirmPreAllocation(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(confirmedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(confirmedPODTO, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/order/{id}/rejectpreallocate")
    public ResponseEntity<PurchaseOrderDTO> rejectPreallocate(@PathVariable Long id) throws Exception {

        PurchaseOrderDTO confirmedPODTO = salesService.rejectPreAllocation(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(confirmedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(confirmedPODTO, headers, HttpStatus.CONFLICT);
    }

    @PostMapping("/order/{id}/preallocate")
    public ResponseEntity<PurchaseOrderDTO> preallocate(@PathVariable Long id) throws Exception {

        PurchaseOrderDTO confirmedPODTO = salesService.preallocateplantPO(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(confirmedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(confirmedPODTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/order/{po_id}/allocateItem/{item_id}")
    public ResponseEntity<PurchaseOrderDTO> directPlantAllocation(@PathVariable Long po_id, @PathVariable Long item_id) throws Exception {

        PurchaseOrderDTO confirmedPODTO = salesService.directPlantAllocation(po_id, item_id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(confirmedPODTO.getId().getHref()));

        return new ResponseEntity<PurchaseOrderDTO>(confirmedPODTO, headers, HttpStatus.CREATED);
    }
}