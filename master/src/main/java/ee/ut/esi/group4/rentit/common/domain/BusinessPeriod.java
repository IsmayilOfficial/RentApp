package ee.ut.esi.group4.rentit.common.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;
}
