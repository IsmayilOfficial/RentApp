package ee.ut.esi.group4.rentit.inventory.domain.model;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import ee.ut.esi.group4.rentit.maintenance.domain.model.MaintenancePlan;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PlantReservation {
    @Id @GeneratedValue
    Long id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    PlantInventoryItem plant;

    @OneToOne(optional = true)
    PurchaseOrder rental;

    @OneToOne(optional = true)
    MaintenancePlan maintPlan;
}
