package ee.ut.esi.group4.rentit.inventory.domain.model;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntry {
    @Id @GeneratedValue
    Long id;

    String name;
    String description;

    @Column(precision=8, scale=2)
    BigDecimal price;
}
