package test.alipsa.renjinboot.webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.alipsa.renjinboot.restapp.RestApplication;
import se.alipsa.renjinboot.restapp.model.BudgetReport;
import se.alipsa.renjinboot.restapp.model.LineItem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {"se.alipsa.renjinboot.restapp"})
public class BudgetReportBackendTest {

  @Autowired
  TestRestTemplate restTemplate;

  @LocalServerPort
  String port;

  @Test
  public void testBudgetReport() throws JsonProcessingException {
    //[{"name":"Roof","monthlyCosts":[3,3,2,2,5,5,4,4,3,3,2,2]}]
    LineItem li1 = new LineItem();
    li1.setName("Roof");
    li1.addMonthlyCosts(Lists.list(3,3,2,2,5,5,4,4,3,3,2,2));

    List<LineItem> lineItems = new ArrayList<>();
    lineItems.add(li1);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ObjectMapper mapper = new ObjectMapper();
    HttpEntity<String> request =
        new HttpEntity<String>(mapper.writeValueAsString(lineItems), headers);


    BudgetReport report = restTemplate.postForObject("http://localhost:" + port + "/reports/budget", request, BudgetReport.class);
    assertNotNull(report);
    assertEquals(lineItems.size() + 1, report.getSummaryTable().getRowList().size(), "Should be number of line items plus the total row");

  }



}
