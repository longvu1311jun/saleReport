package org.report.reportstatusmesssale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReportStatusMessSaleApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReportStatusMessSaleApplication.class, args);
  }

}
