package com.siddhu.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MCPServerMongoServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MCPServerMongoServiceClient.class);
    private final MongoClient mongoClient;

    /**
     * 使用 MongoDB URI 初始化客户端
     * @param mongoUri 格式示例：mongodb://username:password@host:port
     */
    public MCPServerMongoServiceClient(@Value("${spring.data.mongodb.uri}") String mongoUri) {
        logger.info("正在初始化 MongoDB 客户端，连接URI: {}", mongoUri);
        this.mongoClient = MongoClients.create(mongoUri);
    }

    /**
     * 列出 MongoDB 中所有数据库
     * @return 数据库名称列表
     */
    @Tool(description = "获取 MongoDB 中所有数据库的列表")
    public List<String> listDatabases() {
        logger.info("正在获取数据库列表...");
        List<String> databaseNames = new ArrayList<>();
        for (Document db : mongoClient.listDatabases()) {
            databaseNames.add(db.getString("name"));
        }
        logger.info("当前数据库列表: {}", databaseNames);
        return databaseNames;
    }

    /**
     * 列出指定数据库中的所有集合
     * @param dbName 数据库名称
     * @return 集合名称列表
     */
    @Tool(description = "获取指定数据库中所有集合的列表")
    public List<String> listCollections(String dbName) {
        logger.info("正在获取数据库 '{}' 中的集合列表...", dbName);
        List<String> collectionNames = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        for (String name : database.listCollectionNames()) {
            collectionNames.add(name);
        }
        logger.info("数据库 '{}' 中的集合列表: {}", dbName, collectionNames);
        return collectionNames;
    }

    /**
     * 列出指定集合的所有索引
     * @param dbName 数据库名称
     * @param collectionName 集合名称
     * @return 索引文档列表
     */
    @Tool(description = "获取指定集合的所有索引信息")
    public List<Document> listIndexes(String dbName, String collectionName) {
        logger.info("正在获取 {}.{} 的索引列表...", dbName, collectionName);
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
        List<Document> indexes = new ArrayList<>();
        collection.listIndexes().into(indexes);
        logger.info("索引列表: {}", indexes);
        return indexes;
    }

    /**
     * 在指定数据库中创建新集合
     * @param dbName 数据库名称
     * @param collectionName 集合名称
     * @return 操作结果消息
     */
    @Tool(description = "在指定数据库中创建新集合")
    public String createCollection(String dbName, String collectionName) {
        logger.info("正在在数据库 '{}' 中创建集合 '{}'...", dbName, collectionName);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.createCollection(collectionName);
        logger.info("集合 '{}' 创建成功", collectionName);
        return String.format("集合 '%s' 已在数据库 '%s' 中创建成功", collectionName, dbName);
    }

    @Tool(description = "根据查询条件获取指定集合中的文档")
    public List<Document> queryDocuments(String dbName, String collectionName, String filterJson) {
        logger.info("正在执行查询 {}.{}，条件: {}, 限制: {}", dbName, collectionName);

        Document filter = Document.parse(filterJson);
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
        List<Document> results = new ArrayList<>();
        collection.find(filter).into(results);
        logger.info("原始查询结果数量: {}", results.size());

        // 使用 Map 缓存主数据：key = date_desc
        Map<String, Document> mainDataMap = new HashMap<>(results.size());
        Set<String> processedKeys = new HashSet<>(results.size());

        // 一次性遍历所有数据，区分主数据与非主数据
        for (Document doc : results) {
            Object powerDetail = doc.get("powerDetail");

            if (powerDetail instanceof List<?>) {
                // 是主数据 → 存入 map
                String key = generateKey(doc);
                mainDataMap.put(key, doc);
                processedKeys.add(key);  // 标记为主数据键
            } else {
                // 是非主数据 → 尝试匹配主数据
                String key = generateKey(doc);
                if (processedKeys.contains(key)) {
                    // 主数据已存在，直接合并
                    Document mainDoc = mainDataMap.get(key);
                    mergePowerData(mainDoc, doc);
                }
            }
        }

        // 删除所有主数据中的 powerDetail 字段
        for (Document mainDoc : mainDataMap.values()) {
            mainDoc.remove("powerDetail");
        }

        // 返回最终主数据列表（保持顺序）
        return new ArrayList<>(mainDataMap.values());
    }

    // 使用 StringBuilder 避免字符串拼接性能损耗
    private static final String KEY_SEPARATOR = "_";

    private String generateKey(Document doc) {
        return doc.getString("date") + KEY_SEPARATOR + doc.getString("desc");
    }

    // 定义要保留的电量字段（静态常量，避免重复创建）
    private static final Set<String> POWER_FIELDS = Set.of(
            "ia", "ib", "ic", "p", "ep", "ua", "ub", "uc"
    );

    // 合并次数据到主数据，只保留电量相关字段
    private void mergePowerData(Document mainDoc, Document subDoc) {
        for (String field : POWER_FIELDS) {
            Object value = subDoc.get(field);
            if (value != null) {
                mainDoc.put(field, value);  // 覆盖或新增字段
            }
        }
    }

    @Tool(description = "获取指定集合中第一条记录，用于展示字段名")
    public Document getFirstDocument(String dbName, String collectionName) {
        logger.info("正在获取 {}.{} 中的第一条记录", dbName, collectionName);
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
        return collection.find().first();
    }

    @Tool(description = "获取北自公司最帅的人")
    public String getshuai() {

        return "张海涛";
    }
}