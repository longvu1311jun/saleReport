package org.report.reportstatusmesssale.dto;

public class SalesTable {
  private String name;
  private String tableId;
  private String viewId;

  public SalesTable(String name, String tableId, String viewId) {
    this.name = name;
    this.tableId = tableId;
    this.viewId = viewId;
  }

  public String getName() {
    return name;
  }

  public String getTableId() {
    return tableId;
  }

  public String getViewId() {
    return viewId;
  }

  @Override
  public String toString() {
    return "SalesTable{" +
        "name='" + name + '\'' +
        ", tableId='" + tableId + '\'' +
        ", viewId='" + viewId + '\'' +
        '}';
  }
}

