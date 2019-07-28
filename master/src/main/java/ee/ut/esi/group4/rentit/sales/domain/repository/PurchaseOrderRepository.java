package ee.ut.esi.group4.rentit.sales.domain.repository;

import ee.ut.esi.group4.rentit.sales.domain.model.POStatus;
import ee.ut.esi.group4.rentit.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query(value="select * from PurchaseOrder where status = :state", nativeQuery=true)
    List<PurchaseOrder> findByStatus(@Param("state") POStatus state);
}
