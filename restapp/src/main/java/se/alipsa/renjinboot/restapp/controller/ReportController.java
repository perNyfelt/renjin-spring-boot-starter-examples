package se.alipsa.renjinboot.restapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import se.alipsa.renjinboot.restapp.model.BudgetReport;
import se.alipsa.renjinboot.restapp.model.LineItem;
import se.alipsa.renjinboot.restapp.service.ReportService;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

@Controller
public class ReportController {

  private final ReportService reportService;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @PostMapping(path = "/reports/budget", consumes = MediaType.APPLICATION_JSON_VALUE)
  public BudgetReport createBudgetReport(@RequestBody String json) {
    try {
      System.out.println("Got " + json);
      ObjectMapper mapper = new ObjectMapper();
      List<LineItem> lineItems = mapper.readValue(json, new TypeReference<List<LineItem>>() {});
      BudgetReport report = reportService.runBudgetReport(lineItems);
      return report;
    } catch (IOException | ScriptException e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
