package org.report.reportstatusmesssale.controller;

import lombok.RequiredArgsConstructor;
import org.report.reportstatusmesssale.config.LarkBaseProperties;
import org.report.reportstatusmesssale.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;
  private final LarkBaseProperties larkProps;

  @GetMapping("/report")
  public String showReport(Model model) {
    model.addAttribute("report", reportService.getCachedReport());
    model.addAttribute("hasToken",
        larkProps.getUserAccessToken() != null && !larkProps.getUserAccessToken().isBlank());
    return "report";
  }

  // nhận token từ form
  @GetMapping("/report/token")
  public String setToken(@RequestParam("userToken") String userToken) {

    // lưu token user nhập
    larkProps.setUserAccessToken(userToken.trim());

    // refresh lại báo cáo với token mới
    reportService.refreshReport();

    // quay lại trang /report
    return "redirect:/report";
  }
}
