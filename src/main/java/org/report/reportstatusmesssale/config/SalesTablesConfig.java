package org.report.reportstatusmesssale.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalesTablesConfig {

  @Getter
  @AllArgsConstructor
  public static class SalesTable {
    private String displayName; // Tên hiển thị trên report (Tư vấn viên)
    private String tableId;     // table_id của Bitable
    private String viewId;      // view_id (bạn tự add thêm ở đây)
  }

  // Chỉ fix name + tableId
  public List<SalesTable> getTables() {
    return List.of(
    new SalesTable("TuấnTA_Team1", "tbljRobaXZZJFT8f","vewE3Ope6x"),
    new SalesTable("LinhLTM_Team1", "tblCSPCrhqMNs6jS","vewE3Ope6x"),
    new SalesTable("AnhNP_Team1", "tblGUcy3cPbdZzVX","vewE3Ope6x"),
    new SalesTable("TrangHTT_Team1", "tblDmW6RE0Q3nogW","vewE3Ope6x"),
    new SalesTable("ThanhBP_Team1", "tblHmNjgZZNjCtuP","vewE3Ope6x"),
    new SalesTable("LinhLQ_Team1", "tblAiI4XFuAsRvCX","vewE3Ope6x"),
    new SalesTable("LongNT_Team1", "tblKHcDdL6uXvZtv","vewE3Ope6x"),
    new SalesTable("LinhDTT_Team2", "tbltvXRoN5eL1D11","vewE3Ope6x"),
    new SalesTable("QuânND_Team2", "tblBxLYmaAJBXaJO","vewE3Ope6x"),
    new SalesTable("LýBTT_Team2", "tbl35gdJGRs3GZGJ","vewE3Ope6x"),
    new SalesTable("YếnLH_Team2", "tbl3ig26TdAzwmTI","vewE3Ope6x"),
    new SalesTable("NgaDT_Team2", "tblbKuNFnOFhpBVf","vewE3Ope6x"),
    new SalesTable("TrangLTT_Team2", "tbl8gdEU158IHJLN","vewE3Ope6x"),
    new SalesTable("DiệpTB_Team2", "tblxwBOZh9ZAYsyE","vewE3Ope6x"),
    new SalesTable("TuấnLQ_Team3", "tbl9aE2DGe8jUuWa","vewE3Ope6x"),
    new SalesTable("QuânDM_Team3", "tbldHvIW8j9CGMwQ","vewE3Ope6x"),
    new SalesTable("DuyTT_Team3", "tblLsliZ4nxoZLIS","vewE3Ope6x"),
    new SalesTable("BăngNTT_Team3", "tblTXava5izCQif6","vewE3Ope6x"),
    new SalesTable("NhiNH_Team3", "tblbHpGAHTMB2w3F","vewE3Ope6x"),
    new SalesTable("LanPTH_Team3", "tblk9aRTpSWjhqt0","vewE3Ope6x"),
    new SalesTable("PhươngDTM_Team3", "tblJXl2F4m68R8z7","vewE3Ope6x"),
    new SalesTable("ThuHT_Team4", "tblG5SLbUMXddiKP","vewE3Ope6x"),
    new SalesTable("LựuNT_Team4", "tbljJv10UimVX0Ux","vewE3Ope6x"),
    new SalesTable("HiềnPT_Team4", "tblCi8sRbwwv8YMf","vewE3Ope6x"),
    new SalesTable("LênDD_Team5", "tblJH9kaxviTi6Zk","vewE3Ope6x"),
    new SalesTable("ĐiệpDTN_Team5", "tblJRqhoyZ4PpFgj","vewE3Ope6x"),
    new SalesTable("HườngPT_Team5", "tblozC0u4Kb0jjpR","vewE3Ope6x"),
    new SalesTable("ThuNT_TLS", "tblPGpQ0lPzMjTxO","vewE3Ope6x"),
    new SalesTable("LinhNK_TLS", "tblI9SMTxVgbL9qJ","vewE3Ope6x"),
    new SalesTable("NgọcDTT_TLS", "tbl91cJABDa1v79m","vewE3Ope6x"),
    new SalesTable("Vũ Quang Huy_NT_TEAM6", "tbl0AWlAUWLErTcl","vewE3Ope6x"),
    new SalesTable("Lê Thị Nhật Nguyệt_NT", "tblw9iwQvKgKEhno","vewE3Ope6x"),
    new SalesTable("Vũ Xuân Quy_NT", "tbljDg709q1IMAJi","vewE3Ope6x"),
    new SalesTable("DuyDA_NT_TLS", "tblePjRi4jSQzXtX","vewE3Ope6x"),
    new SalesTable("Hoàng Thị Quỳnh Nga_NT_TLS", "tblOacGlkXWzsQAE","vewE3Ope6x")
    );
  }
}