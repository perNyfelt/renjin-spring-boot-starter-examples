package se.alipsa.renjinboot.restapp.model;

import java.util.ArrayList;
import java.util.List;

public class LineItem {

  private String name;
  private final List<Integer> monthlyCosts = new ArrayList<>();

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

  public void addMonthlyCost(Integer monthlyCost) {
    monthlyCosts.add(monthlyCost);
  }

  public void addMonthlyCosts(List<Integer> monthlyCosts) {
    this.monthlyCosts.addAll(monthlyCosts);
  }
}
