package org.report.reportstatusmesssale.lark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.report.reportstatusmesssale.config.LarkBaseProperties;
import org.report.reportstatusmesssale.config.SalesTablesConfig;
import org.report.reportstatusmesssale.model.CustomerRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LarkBaseClient {

  private final LarkBaseProperties props;

  // tạo sẵn webClient + objectMapper, không inject từ ngoài
  private final WebClient webClient = WebClient.builder().build();
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Lấy user access token do bạn nhập ở UI (u-....)
   */
  private String getAccessTokenForBitable() {
    String token = props.getUserAccessToken();
    if (token == null || token.isBlank()) {
      throw new IllegalStateException(
          "User access token is not set. Please input token on the report page.");
    }
    token = token.trim();
    log.info(">>> USING USER ACCESS TOKEN FROM UI: {}", token.substring(0, 6) + "...");
    return token;
  }

  /**
   * Gọi Bitable records/search cho 1 bảng (table + viewId đã fix trong SalesTablesConfig)
   */
  public List<CustomerRecord> fetchRecords(SalesTablesConfig.SalesTable table) {
    log.info(">>> ===== FETCH RECORDS =====");
    log.info(">>> STAFF       = {}", table.getDisplayName());
    log.info(">>> TABLE_ID    = {}", table.getTableId());
    log.info(">>> VIEW_ID     = {}", table.getViewId());

    String userToken = getAccessTokenForBitable();

    List<CustomerRecord> result = new ArrayList<>();
    String pageToken = null;

    do {
      String url = props.getBaseUrl()
          + "/open-apis/bitable/v1/apps/"
          + props.getAppToken()
          + "/tables/"
          + table.getTableId()
          + "/records/search?page_size=500";

      if (pageToken != null && !pageToken.isEmpty()) {
        url += "&page_token=" + pageToken;
      }

      log.info(">>> [RECORD] CALL SEARCH URL = {}", url);

      // body giống CURL bạn test trên API explorer
      ObjectNode body = objectMapper.createObjectNode();
      body.put("automatic_fields", false);
      ArrayNode fieldNames = body.putArray("field_names");
      fieldNames.add("Ngày tạo");
      fieldNames.add("Trạng thái mess");
      body.put("view_id", table.getViewId());

      String rawJson = webClient.post()
          .uri(url)
          .header("Authorization", "Bearer " + userToken)
          .header("Content-Type", "application/json")
          .bodyValue(body)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      log.info(">>> [RECORD] RAW RESPONSE = {}", rawJson);

      JsonNode root;
      try {
        root = objectMapper.readTree(rawJson);
      } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
        log.error(">>> [RECORD] JSON PARSE ERROR, RAW = {}", rawJson, e);
        throw new RuntimeException("Cannot parse JSON from bitable", e);
      }

      int code = root.path("code").asInt(-1);
      if (code != 0) {
        log.warn(">>> [RECORD] ERROR CODE = {}, BODY = {}", code, rawJson);
        throw new RuntimeException("Error calling records/search, code = " + code);
      }

      JsonNode dataNode = root.path("data");
      JsonNode itemsNode = dataNode.path("items");

      if (itemsNode.isArray()) {
        for (JsonNode item : itemsNode) {
          JsonNode fields = item.path("fields");
          if (fields.isMissingNode()) continue;

          // Ngày tạo: epoch milli -> LocalDate
          JsonNode createdNode = fields.path("Ngày tạo");
          if (!createdNode.isNumber()) continue;
          long createdMillis = createdNode.asLong();
          LocalDate createdDate = Instant.ofEpochMilli(createdMillis)
              .atZone(ZoneId.systemDefault())
              .toLocalDate();

          // Trạng thái mess: array -> lấy phần tử đầu tiên
          JsonNode statusNode = fields.path("Trạng thái mess");
          String status = null;
          if (statusNode.isArray() && statusNode.size() > 0) {
            status = statusNode.get(0).asText();
          }

          CustomerRecord cr = new CustomerRecord(createdDate, status);
          result.add(cr);

          result.add(cr);
        }
      }

      boolean hasMore = dataNode.path("has_more").asBoolean(false);
      String nextToken = dataNode.path("page_token").asText(null);

      if (hasMore && nextToken != null && !nextToken.isEmpty()) {
        pageToken = nextToken;
        log.info(">>> [RECORD] HAS MORE, PAGE_TOKEN = {}", pageToken);
      } else {
        pageToken = null;
      }

    } while (pageToken != null);

    log.info(">>> [RECORD] TOTAL RECORDS FETCHED = {}", result.size());
    log.info(">>> ===== END FETCH =====");
    return result;
  }
}
