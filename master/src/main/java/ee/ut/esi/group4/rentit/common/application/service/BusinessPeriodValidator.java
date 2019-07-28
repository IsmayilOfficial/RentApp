package ee.ut.esi.group4.rentit.common.application.service;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class BusinessPeriodValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return BusinessPeriod.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BusinessPeriod period = (BusinessPeriod) o;

        if (period.getStartDate() == null) {
            errors.rejectValue("startDate", "start date cannot be null");
        }
        if (period.getEndDate() == null) {
            errors.rejectValue("endDate", "endDate cannot be null");
        }

        if (period.getStartDate() != null && period.getEndDate() != null) {
            if (period.getStartDate().isAfter(period.getEndDate())) {
                errors.rejectValue("startDate", "startDate cannot be after endDate");
            }
            if (period.getStartDate().isBefore(LocalDate.now())) {
                errors.rejectValue("startDate", "startDate cannot be in the past");
            }
            if (period.getEndDate().isBefore(LocalDate.now())) {
                errors.rejectValue("endDate", "endDate cannot be in the past");
            }
        }
    }
}
