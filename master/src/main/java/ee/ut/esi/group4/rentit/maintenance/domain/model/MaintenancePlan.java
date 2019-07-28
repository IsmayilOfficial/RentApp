package ee.ut.esi.group4.rentit.maintenance.domain.model;

import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class MaintenancePlan {
    @Id @GeneratedValue
    Long id;

    Integer yearOfAction;

    @ManyToOne
    PlantInventoryItem plant;

    @OneToMany(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
    Set<MaintenanceTask> tasks;
}
