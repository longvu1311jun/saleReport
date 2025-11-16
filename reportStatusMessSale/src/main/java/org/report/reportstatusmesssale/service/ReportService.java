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

    @Getter
    private volatile List<SalesSummary> cachedReport = new ArrayList<>();

    @PostConstruct
    public void initOnStartup() {
        log.info("Init report on startup...");
        refreshReport();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refreshReport() {
        try {
            log.info("Refreshing sales report from Lark Base...");

            List<SalesSummary> reportList = new ArrayList<>();

            for (SalesTablesConfig.SalesTable table : salesTablesConfig.getTables()) {

                List<CustomerRecord> records = larkBaseClient.fetchRecords(table);
                SalesSummary summary = buildSummary(table.getDisplayName(), records);
                reportList.add(summary);

                log.info("Fetched {} records for {}", records.size(), table.getDisplayName());
            }

            cachedReport = reportList;
            log.info("Report refreshed. Total staff = {}", reportList.size());

        } catch (Exception e) {
            log.error("Error refreshing report", e);
        }
    }

    /**
     * Build SalesSummary theo bảng KPI mới bro yêu cầu
     */
    private SalesSummary buildSummary(String staff, List<CustomerRecord> records) {

        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year  = now.getYear();

        SalesSummary s = new SalesSummary();
        s.setStaffName(staff);

        long nhuCau = 0;
        long trung = 0;
        long rac = 0;
        long ktt = 0;
        long chotNong = 0;
        long chotCu = 0;
        long donHuy = 0;

        for (CustomerRecord r : records) {

            // lọc theo tháng hiện tại
            if (r.getCreatedDate().getYear() != year ||
                    r.getCreatedDate().getMonthValue() != month) {
                continue;
            }

            String st = r.getStatus();
            if (st == null) continue;

            String lower = st.toLowerCase();

            if (lower.contains("nhu cầu")) nhuCau++;
            else if (lower.contains("trùng")) trung++;
            else if (lower.contains("rác")) rac++;
            else if (lower.contains("không tương tác")) ktt++;
            else if (lower.contains("chốt nóng")) chotNong++;
            else if (lower.contains("chốt cũ")) chotCu++;
            else if (lower.contains("đơn huỷ") || lower.contains("đơn hủy")) donHuy++;
        }

        // set vào summary
        s.setNhuCau(nhuCau);
        s.setTrung(trung);
        s.setRac(rac);
        s.setKhongTuongTac(ktt);
        s.setChotNong(chotNong);
        s.setChotCu(chotCu);
        s.setDonHuy(donHuy);

        long tongMes = nhuCau + trung + rac + ktt + chotNong + chotCu + donHuy;
        long tongDon = chotNong + chotCu + donHuy   ;

        s.setTongMes(tongMes);
        s.setTongDon(tongDon);

        // công thức t tính cho bro
        double donMesNhuCau = safeDiv(tongDon, (nhuCau + chotNong + chotCu + donHuy))*100;
        double donMesTong   = safeDiv(tongDon, tongMes)*100;
        double tiLeHuy      = safeDiv(donHuy,  tongDon)*100;

        s.setDonMesNhuCau(round2(donMesNhuCau));
        s.setDonMesTong(round2(donMesTong));
        s.setTiLeHuy(round2(tiLeHuy));

        return s;
    }

    private double safeDiv(long a, long b) {
        return b == 0 ? 0 : (double) a / b;
    }

    private double round2(double v) {
        return Math.round(v * 100.0)/100.0;
    }
}
