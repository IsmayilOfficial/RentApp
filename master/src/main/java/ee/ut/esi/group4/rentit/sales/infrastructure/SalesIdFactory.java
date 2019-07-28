package ee.ut.esi.group4.rentit.sales.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesIdFactory {
    public String nextPurchaseOrderID() {
        return UUID.randomUUID().toString();
    }
}
