package ee.ut.esi.group4.rentit.maintenance.domain.repository;

import ee.ut.esi.group4.rentit.maintenance.domain.model.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenancePlanRepository extends JpaRepository<MaintenancePlan, Long> {

}
