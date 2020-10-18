package se.alipsa.renjinboot.webapp.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.StringArrayVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import se.alipsa.renjin.client.datautils.Table;
import se.alipsa.renjinboot.webapp.model.BudgetReport;
import se.alipsa.renjinboot.webapp.model.LineItem;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class ReportService {

  private final AnalysisEngine analysisEngine;
  ResourceLoader resourceLoader;


  @Autowired
  public ReportService(AnalysisEngine analysisEngine, ResourceLoader resourceLoader) {
    this.analysisEngine = analysisEngine;
    this.resourceLoader = resourceLoader;
  }

  /**
   * There are two ways to retrieve data from an R script:
   * 1. The result from running the script (the eval method of the scriptEngine)
   * 2. Any variables residing in the topLevel context
   * Below both ways are used.
   *
   * This method will execute the R code in resource/reports/BudgetReport.R
   * @param lineItems the individual line items that makes up the budget
   * @return A BudgetReport object used in the budgetReportOutput.html
   * @throws IOException if some problem reading the script occurs
   * @throws ScriptException if something is wrong in the R script
   */
  public BudgetReport runBudgetReport(List<LineItem> lineItems) throws IOException, ScriptException {
    String script = fileContentOf("reports/BudgetReport.R");
    // The result of the R script is the path to the temp file created
    StringArrayVector result = (StringArrayVector)analysisEngine.runScript(script, Collections.singletonMap("items", lineItems));
    String plotFilePath = result.asString();
    File plotFile = new File(plotFilePath);
    if (!plotFile.exists()) {
      throw new FileNotFoundException(plotFilePath + " does not exist");
    }
    // Get the variable assigned in the R script
    ListVector summaryDf = (ListVector)analysisEngine.getVariable("summaryTable");
    Table summaryTable = new Table(summaryDf);
    BudgetReport report = new BudgetReport(contentAsBase64Image(plotFilePath), summaryTable);

    // Cleanup
    analysisEngine.clearEnvironment();
    if (!plotFile.delete()) {
      // If we were more through we should schedule an executor to try again later before fallback to deleteOnExit
      plotFile.deleteOnExit();
    }

    return report;
  }

  private String fileContentOf(String resourceName, Charset... charsetOpt) throws IOException {
    Charset charset = charsetOpt.length > 0 ? charsetOpt[0] : StandardCharsets.UTF_8;
    return IOUtils.toString(resourceLoader.getResource("classpath:" + resourceName).getURL(), charset);
  }

  /**
   * Convert the image file to a base64 encoded data string that can be used directly in an img tag
   */
  private String contentAsBase64Image(String fullFileName) throws IOException {
    byte[] content = FileUtils.readFileToByteArray(new File(fullFileName));
    return "data:image/png;base64, " + Base64.getEncoder().encodeToString(content);
  }
}
