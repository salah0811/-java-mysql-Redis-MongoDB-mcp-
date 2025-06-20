package com.siddhu.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MCPServerRedisServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MCPServerMongoServiceClient.class);

    // 默认参数列表
    private static final List<String> DEFAULT_PARAMETERS = Arrays.asList("uab", "ubc", "ia", "ib", "ic", "ep", "p", "q");

    // 字段映射表
    private static final Map<String, String> PARAMETERSTEXT_MAP = new HashMap<>();

    static {
        PARAMETERSTEXT_MAP.put("uab", "uab(AB线电压)");
        PARAMETERSTEXT_MAP.put("ubc", "ubc(BC线电压)");
        PARAMETERSTEXT_MAP.put("ia", "ia(A相电流)");
        PARAMETERSTEXT_MAP.put("ib", "ib(B相电流)");
        PARAMETERSTEXT_MAP.put("ic", "ic(C相电流)");
        PARAMETERSTEXT_MAP.put("ep", "ep(实际功率)");
        PARAMETERSTEXT_MAP.put("p", "p(有功功率)");
        PARAMETERSTEXT_MAP.put("q", "q(无功功率)");
    }

    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate redisTemplate;

    public MCPServerRedisServiceClient(JdbcTemplate jdbcTemplate, StringRedisTemplate redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询实时电量数据（对应 Python 中 /power/query 接口）
     */
    @Tool(description = "根据关键词和参数查询实时电量数据")
    public String queryRealTimePowerData(String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            return "# 错误\n\n查询关键词不能为空";
        }

        String query = desc.trim();

        // 解析查询参数
        List<String> parameters = DEFAULT_PARAMETERS;
        String queryPart = query;

        if (parameters.isEmpty()) {
            return "# 错误\n\n没有有效的查询参数";
        }

        logger.info("解析后的查询参数 - 设备查询: '{}', 电量参数: {}", queryPart, parameters);

        // 查询 MySQL 获取设备信息
        String sql = "SELECT em.name, dpu.tg, em.alias_name FROM dpu INNER JOIN elec_meter em ON dpu.asset_number = em.gateway_id WHERE em.alias_name LIKE ? AND em.project_id = 1";
        List<Map<String, Object>> mysqlResults = jdbcTemplate.queryForList(sql, "%" + queryPart + "%");

        if (mysqlResults.isEmpty()) {
            return "# 结果\n\n未找到匹配的设备信息";
        }

        logger.info("从MySQL获取了 {} 个设备信息，准备查询Redis电量数据", mysqlResults.size());

        // 构造结果
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultsList = new ArrayList<>();

        for (Map<String, Object> item : mysqlResults) {
            String tg = (String) item.get("tg");
            String name = (String) item.get("name");
            String aliasName = (String) item.get("alias_name");

            String key = tg + ":" + name;

            Map<String, Object> deviceResult = new HashMap<>();
            deviceResult.put("name", aliasName);
            Map<String, Object> values = new HashMap<>();

            for (String param : parameters) {
                String fullKey = key + ":" + param;
                try {
                    String value = redisTemplate.opsForValue().get(fullKey);
                    if (value == null) {
                        values.put(param, "N/A");
                    } else {
                        values.put(param, extractValFromJson(value));
                    }
                } catch (Exception e) {
                    values.put(param, "查询错误");
                    logger.error("查询参数 {} 出错: {}", fullKey, e.getMessage());
                }
            }

            deviceResult.put("values", values);
            resultsList.add(deviceResult);
        }

        result.put("results", resultsList);

        // 转换为 Markdown 表格返回
        return convertToMarkdown(result, parameters);
    }

    /**
     * 从 JSON 字符串中提取 val 字段
     */
    private Object extractValFromJson(String jsonStr) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(jsonStr, Map.class);
            // 使用 Jackson ObjectMapper 更安全，这里简化处理
            // 可替换为：ObjectMapper.readValue(jsonStr, Map.class)
            return map.get("val");
        } catch (Exception e) {
            return jsonStr; // 如果不是 JSON，返回原始值
        }
    }

    /**
     * 将电量数据转换为 Markdown 表格
     */
    private String convertToMarkdown(Map<String, Object> data, List<String> parameters) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");

        if (results == null || results.isEmpty()) {
            return "# 电量查询结果\n\n没有找到匹配的电量数据";
        }

        StringBuilder markdown = new StringBuilder("# 电量查询结果\n\n");

        // 构建表头
        markdown.append("| 设备名称 | ");
        for (String param : parameters) {
            markdown.append(PARAMETERSTEXT_MAP.getOrDefault(param, param)).append(" | ");
        }
        markdown.append("\n");

        // 分隔行
        markdown.append("| --- ").append(String.join(" | ", Collections.nCopies(parameters.size(), "---"))).append(" |\n");

        // 数据行
        for (Map<String, Object> result : results) {
            Map<String, Object> values = (Map<String, Object>) result.get("values");
            markdown.append("| ").append(result.get("name")).append(" | ");

            for (String param : parameters) {
                Object val = values.get(param);
                markdown.append(val != null ? val.toString() : "N/A").append(" | ");
            }

            markdown.append("\n");
        }

        return markdown.toString();
    }
}