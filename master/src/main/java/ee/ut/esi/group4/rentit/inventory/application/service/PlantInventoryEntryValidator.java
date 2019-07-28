package ee.ut.esi.group4.rentit.inventory.application.service;

import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlantInventoryEntryValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PlantInventoryEntry.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PlantInventoryEntry entry = (PlantInventoryEntry) o;


        if (entry.getName() == null) {
            errors.rejectValue("name", "name cannot be null");
        }
        if (entry.getDescription() == null) {
            errors.rejectValue("description", "description cannot be null");
        }

        if (entry.getPrice() == null) {
            errors.rejectValue("price", "price cannot be null");
        }

        if (entry.getPrice().floatValue() <= 0) {
            errors.rejectValue("price", "price cannot be equal to or less than 0");
        }
    }
}