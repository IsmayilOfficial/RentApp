package ee.ut.esi.group4.rentit.inventory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin={"pretty","html:target/cucumber"},
        features="classpath:features/inventory",
        glue="ee.ut.esi.group4.rentit.inventory")
public class InventoryAcceptanceTestRunner {

}
