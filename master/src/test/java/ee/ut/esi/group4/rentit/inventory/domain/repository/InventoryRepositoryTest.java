package ee.ut.esi.group4.rentit.inventory.domain.repository;

import ee.ut.esi.group4.rentit.RentitApplication;
import ee.ut.esi.group4.rentit.common.domain.BusinessPeriod;
import ee.ut.esi.group4.rentit.inventory.domain.query.MaintenanceCostResult;
import ee.ut.esi.group4.rentit.inventory.domain.query.RentalTaskResult;
import ee.ut.esi.group4.rentit.inventory.domain.model.PlantInventoryEntry;
import ee.ut.esi.group4.rentit.maintenance.domain.repository.MaintenancePlanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RentitApplication.class)
@Sql(scripts= "/plants-dataset.sql")
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTest {
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    MaintenancePlanRepository maintenancePlanRepository;

    @Test
    public void queryPlantCatalog() {
        assertThat(inventoryRepository.count()).isEqualTo(14l);
    }

    @Test
    public void queryByName() {
        assertThat(inventoryRepository.findByNameContaining("Mini").size()).isEqualTo(2);
    }

    @Test
    public void findAvailablePlant() {
        List<Pair<PlantInventoryEntry, Long>> res = inventoryRepository.findAvailablePlants("Mini",  BusinessPeriod.of(LocalDate.of(2017, 2, 20), LocalDate.of(2017, 2, 20)));
        assertThat(res).size().isEqualTo(2);
        assertThat(res.get(0).getSecond()).isEqualTo(3);
    }

    @Test
    public void findPlantsUnmaintainedFor() {
        assertThat(inventoryRepository.findPlantsUnmaintainedFor(365).size()).isEqualTo(1);
    }

    @Test
    public void findMaintenanceCostPerYear() {
        List<MaintenanceCostResult> res = inventoryRepository.findMaintenanceCostPerYearForPast(5);
        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getYear()).isEqualTo(2017);
        assertTrue(res.get(0).getCost().compareTo(new BigDecimal(60.00)) == 0);
        assertThat(res.get(1).getYear()).isEqualTo(2018);
        assertTrue(res.get(1).getCost().compareTo(new BigDecimal(100.00)) == 0);
    }

    @Test
    public void findPlantsByPeriodByRentalsAndTasks() {
        List<RentalTaskResult> res =
                inventoryRepository
                    .findPlantsByPeriodByRentalsAndTasks(BusinessPeriod.of(LocalDate.of(2017, 1, 1), LocalDate.of(2019, 1, 1)));
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.get(0).getPlantInventoryItem().getPlantInfo().getDescription()).contains("1.5 Tonne Mini");
        assertThat(res.get(0).getRentals()).isEqualTo(1l);
        assertThat(res.get(0).getTasks()).isEqualTo(1l);
    }
}
