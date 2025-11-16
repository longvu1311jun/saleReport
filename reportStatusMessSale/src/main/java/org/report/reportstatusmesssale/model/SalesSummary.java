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

    private long tongMes;     // = nhuCau + trung + rac + khongTuongTac + chotNong + chotCu + donHuy
    private long tongDon;     // = chotNong + chotCu

    private double donMesNhuCau; // SUM(totalDon / (Nhu cầu + Chốt nóng + Chốt cũ + Đơn hủy))
    private double donMesTong;   // tongDon / tongMes
    private double tiLeHuy;      // donHuy / tongDon
}