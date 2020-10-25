package se.alipsa.renjinboot.restapp.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.StringArrayVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import se.alipsa.renjin.client.datautils.Table;
import se.alipsa.renjinboot.restapp.model.BudgetReport;
import se.alipsa.renjinboot.restapp.model.LineItem;

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

  public BudgetReport runBudgetReport(List<LineItem> lineItems) throws IOException, ScriptException {
    String script = fileContentOf("reports/BudgetReport.R");
    StringArrayVector result = (StringArrayVector)analysisEngine.runScript(script, Collections.singletonMap("items", lineItems));
    String plotFilePath = result.asString();
    File plotFile = new File(plotFilePath);
    if (!plotFile.exists()) {
      throw new FileNotFoundException(plotFilePath + " does not exist");
    }
    ListVector summaryDf = (ListVector)analysisEngine.getVariable("summaryTable");
    Table summaryTable = new Table(summaryDf);
    BudgetReport report = new BudgetReport(contentAsBase64(plotFilePath), summaryTable);

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

  private String contentAsBase64(String fullFileName) throws IOException {
    byte[] content = FileUtils.readFileToByteArray(new File(fullFileName));
    return "data:image/png;base64, " + Base64.getEncoder().encodeToString(content);
  }
}
