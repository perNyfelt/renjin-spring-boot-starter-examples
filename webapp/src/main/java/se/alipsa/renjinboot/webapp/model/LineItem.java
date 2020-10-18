package se.alipsa.renjinboot.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class LineItem {

  private String name;
  private List<Integer> monthlyCosts = new ArrayList<>();

  public LineItem() {
    // default ctor
  }

  public LineItem(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Integer> getMonthlyCosts() {
    return monthlyCosts;
  }

  public void addMonthlyCosts(Integer monthlyCost) {
    monthlyCosts.add(monthlyCost);
  }

  @Override
  public String toString() {
    return "LineItem{" +
        "name='" + name + '\'' +
        ", monthlyCosts=" + monthlyCosts +
        '}';
  }
}
