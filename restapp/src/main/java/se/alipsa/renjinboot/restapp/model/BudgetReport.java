package se.alipsa.renjinboot.restapp.model;

import se.alipsa.renjin.client.datautils.Table;

public class BudgetReport {

  private final String costSummaryImage;
  private final Table summaryTable;

  public BudgetReport(String costSummaryImage, Table summaryTable) {
    this.costSummaryImage = costSummaryImage;
    this.summaryTable = summaryTable;
  }

  public String getCostSummaryImage() {
    return costSummaryImage;
  }

  public Table getSummaryTable() {
    return summaryTable;
  }

}
