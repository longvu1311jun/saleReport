package org.report.reportstatusmesssale.model;

import lombok.Data;

@Data
public class SalesSummary {
  private String staffName;

  private long total;
  private long nhuCau;
  private long trung;
  private long rac;
  private long khongTuongTac;
  private long chotNong;
  private long chotCu;
  private long donHuy;
  private long donHoan;
}