package ee.ut.esi.group4.rentit.sales.domain.model;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PurchaseOrder {
    @Id @GeneratedValue
    Long id;

    @OneToMany
    List<PlantReservation> reservations;

    @ManyToOne
    PlantInventoryEntry plant;

    LocalDate issueData;
    LocalDate paymentSchedule;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    BusinessPeriod period;

    public static PurchaseOrder of(PlantInventoryEntry plant, BusinessPeriod period) {
        PurchaseOrder po = new PurchaseOrder();
        po.plant = plant;
        po.period = period;
        po.reservations = new ArrayList<>();
        po.issueData = LocalDate.now();
        po.status = POStatus.PENDING;

        return po;
    }

    public void setStatus(POStatus status) {
        this.status = status;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void addReservation(PlantReservation reservation) {
        this.reservations.add(reservation);
    }
}
