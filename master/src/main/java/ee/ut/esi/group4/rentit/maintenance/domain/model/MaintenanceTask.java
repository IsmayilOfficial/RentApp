package ee.ut.esi.group4.rentit.maintenance.domain.model;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class MaintenanceTask {
    @Id @GeneratedValue
    Long id;

    String description;

    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;

    @Column(precision=8,scale=2)
    BigDecimal price;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    MaintenancePlan plan;

    @ManyToOne
    PlantReservation reservation;

}
