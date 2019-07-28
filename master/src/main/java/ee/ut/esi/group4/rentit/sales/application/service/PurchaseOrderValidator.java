package ee.ut.esi.group4.rentit.sales.application.service;

import ee.ut.esi.group4.rentit.common.application.service.BusinessPeriodValidator;
import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.application.service.PlantInventoryEntryValidator;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantReservation;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class PurchaseOrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PurchaseOrder.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PurchaseOrder order = (PurchaseOrder) o;

        DataBinder period_binder = new DataBinder( BusinessPeriod.of(order.getPeriod().getStartDate(), order.getPeriod().getEndDate()));
        period_binder.addValidators(new BusinessPeriodValidator());
        period_binder.validate();

        PlantInventoryEntry entry = PlantInventoryEntry.of(order.getPlant().getId(), order.getPlant().getName(), order.getPlant().getDescription(), order.getPlant().getPrice());

        DataBinder entry_binder = new DataBinder(entry);
        entry_binder.addValidators(new PlantInventoryEntryValidator());
        entry_binder.validate();

        if (order.getId() != null) {
            if(!(order.getId() instanceof Long)){
                errors.rejectValue("id", "Purchase Order id must be integer");
            }
        }

        if (order.getStatus() == null) {
            errors.rejectValue("status", "status cannot be null");
        }


        if(order.getIssueData() != null && order.getPaymentSchedule() != null){
            if (order.getIssueData().isBefore(order.getPaymentSchedule())){
                errors.rejectValue("issueDate", "Payment must be done before issuing");
            }
        }

        if(order.getReservations() != null && order.getReservations().size() > 0){
            List<PlantReservation> reservations = order.getReservations();
            boolean equalPeriod = true;
            for (PlantReservation reservation: reservations) {
                equalPeriod = equalPeriod && reservation.getSchedule().equals(order.getPeriod());
            }

            if(!equalPeriod){
                errors.rejectValue("Issue Date", "Payment must be done before issuing");
            }
        }

        if (order.getTotal() != null) {
            if(order.getTotal().doubleValue() < 0 )
            errors.rejectValue("Total", "Total cannot be negative");
        }


    }
}
