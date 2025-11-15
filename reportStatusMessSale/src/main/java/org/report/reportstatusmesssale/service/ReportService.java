package org.report.reportstatusmesssale.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.report.reportstatusmesssale.config.SalesTablesConfig;
import org.report.reportstatusmesssale.lark.LarkBaseClient;
import org.report.reportstatusmesssale.model.CustomerRecord;
import org.report.reportstatusmesssale.model.SalesSummary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

  private final LarkBaseClient larkBaseClient;
  private final SalesTablesConfig salesTablesConfig;

  // cache kết quả để user vào trang load nhanh
  @Getter
  private volatile List<SalesSummary> cachedReport = new ArrayList<>();

  // chạy 1 lần khi app khởi động để có dữ liệu ngay
  @PostConstruct
  public void initOnStartup() {
    log.info("Init report on startup...");
    refreshReport();
  }

  // Mỗi giờ update 1 lần: 0 phút, 0 giây mỗi giờ
  @Scheduled(cron = "0 0 * * * *")
  public void refreshReport() {
    try {
      log.info("Refreshing sales report from Lark Base...");

      List<SalesSummary> newReport = new ArrayList<>();

      for (SalesTablesConfig.SalesTable table : salesTablesConfig.getTables()) {
        log.info("Fetching records for table: {} ({})",
            table.getDisplayName(), table.getTableId());

        List<CustomerRecord> records = larkBaseClient.fetchRecords(table);

        log.info("Fetched {} records for {}", records.size(), table.getDisplayName());

        SalesSummary summary = buildSummary(table.getDisplayName(), records);

        log.info(
            "Summary for {}: total={}, nhuCau={}, trung={}, rac={}, khongTuongTac={}, " +
                "chotNong={}, chotCu={}, donHuy={}, donHoan={}",
            summary.getStaffName(),
            summary.getTotal(),
            summary.getNhuCau(),
            summary.getTrung(),
            summary.getRac(),
            summary.getKhongTuongTac(),
            summary.getChotNong(),
            summary.getChotCu(),
            summary.getDonHuy(),
            summary.getDonHoan()
        );

        newReport.add(summary);
      }

      this.cachedReport = newReport;
      log.info("Sales report refreshed. Total staff: {}", newReport.size());
    } catch (Exception e) {
      log.error("Error while refreshing report", e);
    }
  }

  private SalesSummary buildSummary(String staffName, List<CustomerRecord> records) {
    SalesSummary s = new SalesSummary();
    s.setStaffName(staffName);

    LocalDate now = LocalDate.now();
    int currentMonth = now.getMonthValue();
    int currentYear = now.getYear();

    for (CustomerRecord r : records) {
      // chỉ tính record tháng hiện tại
      if (r.getCreatedDate().getYear() != currentYear
          || r.getCreatedDate().getMonthValue() != currentMonth) {
        continue;
      }

      s.setTotal(s.getTotal() + 1);

      String st = r.getStatus();
      if (st == null) continue;

      // chuẩn hoá tí cho chắc
      String stLower = st.toLowerCase();

      if ("nhu cầu".equalsIgnoreCase(st)) {
        s.setNhuCau(s.getNhuCau() + 1);
      }

      if ("trùng".equalsIgnoreCase(st)) {
        s.setTrung(s.getTrung() + 1);
      }

      if (st.contains("Rác")) {
        s.setRac(s.getRac() + 1);
      }

      if (st.contains("Không tương tác")) {
        s.setKhongTuongTac(s.getKhongTuongTac() + 1);
      }

      if ("chốt nóng".equalsIgnoreCase(st)) {
        s.setChotNong(s.getChotNong() + 1);
      }

      if ("chốt cũ".equalsIgnoreCase(st)) {
        s.setChotCu(s.getChotCu() + 1);
      }

      // đơn huỷ / đơn hủy (bắt cả 2)
      if (stLower.contains("đơn huỷ") || stLower.contains("đơn hủy")) {
        s.setDonHuy(s.getDonHuy() + 1);
      }

      if (stLower.contains("đơn hoàn")) {
        s.setDonHoan(s.getDonHoan() + 1);
      }
    }

    return s;
  }
}
