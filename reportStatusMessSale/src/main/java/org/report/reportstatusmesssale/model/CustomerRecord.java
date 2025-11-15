package org.report.reportstatusmesssale.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CustomerRecord {
  private LocalDate createdDate;
  private String status; // Trạng thái mess
}