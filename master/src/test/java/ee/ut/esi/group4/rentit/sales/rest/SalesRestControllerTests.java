package ee.ut.esi.group4.rentit.sales.rest;

import antlr.collections.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ut.esi.group4.rentit.RentitApplication;
import ee.ut.esi.group4.rentit.common.application.dto.BusinessPeriodDTO;
import ee.ut.esi.group4.rentit.sales.application.dto.PurchaseOrderDTO;
import ee.ut.esi.group4.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import ee.ut.esi.group4.rentit.inventory.domain.repository.PlantInventoryEntryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.mockito.AdditionalMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RentitApplication.class) // Check if the name of this class is correct or not
@WebAppConfiguration
@DirtiesContext
public class SalesRestControllerTests {
    @Autowired
    PlantInventoryEntryRepository repo;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testGetAllPlants() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/sales/plants?name=Exc&startDate=2017-04-14&endDate=2017-04-25"))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        List<PlantInventoryEntryDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryEntryDTO>>() { });

        assertThat(plants.size()).isEqualTo(2);

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(1));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testPurchaseOrderAcceptance() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=Exc&startDate=2019-03-22&endDate=2019-03-24"))
                .andReturn();
        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() { });

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(2));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        result = mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", not(isEmptyOrNullString())))
                .andReturn();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);

        assertThat(order.get_xlink("accept")).isNotNull();

        mockMvc.perform(post(order.getLink("accept").getHref()))
                .andReturn();
    }
}