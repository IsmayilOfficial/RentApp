package ee.ut.esi.group4.rentit.inventory.domain.repository;

import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.domain.query.MaintenanceCostResult;
import ee.ut.esi.group4.rentit.inventory.domain.query.RentalTaskResult;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryItem;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryRepositoryImpl implements CustomInventoryRepository {
    @Autowired
    EntityManager em;

    public List<PlantInventoryItem> findAvailableItems(String name, BusinessPeriod period) {
        return em.createQuery(
                "select pi from PlantInventoryItem pi where pi.plantInfo.name like ?1 and " +
                        "pi not in (select pr.plant from PlantReservation pr where ?2 < pr.schedule.endDate and " +
                        "?3 > pr.schedule.startDate)", PlantInventoryItem.class)
                .setParameter(1, name)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }

    public List<PlantInventoryItem> findApplicablePlants(Long entry_id, BusinessPeriod period) {
        return em.createQuery(
                "select pi from PlantInventoryItem pi where pi.plantInfo.id = ?1 and " +
                        "pi not in (select pr.plant from PlantReservation pr where ?2 < pr.schedule.endDate and " +
                        "?3 > pr.schedule.startDate)", PlantInventoryItem.class)
                .setParameter(1, entry_id)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }

    public List<Pair<PlantInventoryEntry, Long>> findAvailablePlants(String name, BusinessPeriod businessPeriod) {
        return em.createQuery(
                "select pe, count(pi.id) from PlantInventoryItem pi " +
                        "left join MaintenancePlan mp on pi.id = mp.plant.id " +
                        "left join MaintenanceTask mt on mt.plan.id = mp.id " +
                        "left join PlantInventoryEntry pe on pi.plantInfo.id = pe.id " +
                        "where Lower(pe.name) like concat('%', Lower(:name), '%') " +
                        "and pi.equipmentCondition <> 'UNSERVICEABLECONDEMNED' and " +
                        "(pi.equipmentCondition = 'SERVICEABLE' or " +
                        "((datediff(DAY, CURRENT_DATE(), :startDate) >= 21) and " +
                        "(datediff(DAY, mt.schedule.startDate, :startDate) >= 7))) " +
                        "group by pe", Object[].class)
                .setParameter("name", name)
                .setParameter("startDate", businessPeriod.getStartDate())
                .getResultList()
                .stream()
                .map(o -> Pair.of((PlantInventoryEntry) o[0], (Long) o[1]))
                .collect(Collectors.toList());
    }

    public List<PlantInventoryItem> findPlantsUnmaintainedFor(Integer days) {
        return em.createQuery(
                "select pi from PlantInventoryItem pi " +
                        "left join MaintenancePlan mp on mp.plant.id = pi.id " +
                        "left join MaintenanceTask mt on mp.id = mt.plan.id " +
                        "where datediff(day, mt.schedule.endDate, CURRENT_DATE()) >= :days",
                PlantInventoryItem.class)
                .setParameter("days", days)
                .getResultList();
    }

    public List<MaintenanceCostResult> findMaintenanceCostPerYearForPast(Integer years) {
        return em.createQuery(
                "select new ee.ut.esi.group4.rentit.inventory.domain.query.MaintenanceCostResult(mp.yearOfAction, sum(mt.price)) from MaintenanceTask mt " +
                        "left join MaintenancePlan mp on mt.plan.id = mp.id " +
                        "where mt.typeOfWork = 'PREVENTIVE' and " +
                        "datediff(year, mt.schedule.startDate, CURRENT_DATE()) <= :years " +
                        "group by mp.yearOfAction order by mp.yearOfAction asc", MaintenanceCostResult.class)
                .setParameter("years", years)
                .getResultList();
    }

    public List<RentalTaskResult> findPlantsByPeriodByRentalsAndTasks(BusinessPeriod period) {
        return em.createQuery(
                "select new ee.ut.esi.group4.rentit.inventory.domain.query.RentalTaskResult(pi, count(pr.id), count(mt.id)) " +
                        "from PlantInventoryItem pi " +
                        "left join MaintenancePlan mp on mp.plant.id = pi.id " +
                        "left join MaintenanceTask mt on mt.plan.id = mp.id " +
                        "left join PlantReservation pr on pr.plant.id = pi.id " +
                        "where mt.typeOfWork = 'CORRECTIVE' and " +
                        "pr.schedule.startDate >= :startDate and " +
                        "pr.schedule.endDate <= :endDate " +
                        "group by pi order by count(pr.id), count(mt.id)", RentalTaskResult.class)
                .setParameter("startDate", period.getStartDate())
                .setParameter("endDate", period.getEndDate())
                .getResultList();
    }
}
