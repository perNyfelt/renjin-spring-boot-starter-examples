package se.alipsa.renjinboot.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.alipsa.renjinboot.webapp.model.BudgetReport;
import se.alipsa.renjinboot.webapp.model.LineItem;
import se.alipsa.renjinboot.webapp.service.ReportService;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

  private final ReportService reportService;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping(path = "/")
  public String home() {
    return "index";
  }

  @GetMapping(path="/reports/budget")
  public String inputBudget() {
    return "budgetReportInput";
  }

  // TODO: could not get automatic form mapping to work so receive the form as MultiValueMap and transform it ourselves
  @PostMapping(path = "/reports/budget", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String showBudgetReport(@RequestBody MultiValueMap<String, String> formData, Model model) {
    try {
      BudgetReport report = reportService.runBudgetReport(toLineItems(formData));
      model.addAttribute("report", report);
      return "budgetReportOutput";
    } catch (IOException | ScriptException e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Transform the request form into a list of LineItem
   * @param formData the request form data
   * @return a list of LineItems
   */
  private List<LineItem> toLineItems(MultiValueMap<String, String> formData) {
    List<LineItem> lineItems = new ArrayList<>();
    String objectName = null;
    String curName;
    LineItem lineItem = new LineItem();
    for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
      String k = entry.getKey();
      String v = entry.getValue().get(0);
      curName = k.substring(0, k.indexOf('.'));
      if (objectName == null) {
        objectName = curName;
      }
      if (!objectName.equals(curName)) {
        lineItems.add(lineItem);
        lineItem = new LineItem();
        objectName = curName;
      }
      if(".name".equals(k.substring(k.lastIndexOf('.')))) {
        lineItem.setName(v);
      } else {
        lineItem.addMonthlyCosts(Integer.parseInt(v));
      }
    }
    // add the last one as we only detect a change in the loop
    lineItems.add(lineItem);
    return lineItems;
  }
}
