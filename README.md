```markdown
# 🧠 Spring Boot AI - Model Context Protocol (MCP) Server

> 基于 Java 和 Spring AI 构建的 Model Context Protocol（MCP）服务，支持与 LLM 工具集成，并整合 Redis、MySQL、MongoDB 等主流数据存储组件。

---

## 📌 项目简介

本项目是一个使用 **Spring Boot + Spring AI** 实现的 **Model Context Protocol (MCP)** 服务端实现，旨在为开发者提供一个可扩展、易部署、功能齐全的本地化模型调用协议服务框架。

该项目解决了当前市面上 Java 领域中对 MCP 协议支持少、文档不完整、缺乏系统性整合的问题，集成了以下核心能力：

- ✅ 支持标准 [Model Context Protocol](https://github.com/Scope-AI/model-context-protocol) 协议通信
- ✅ 支持 SSE（Server-Sent Events）流式响应
- ✅ 可对接多种数据库：Redis、MySQL、MongoDB
- ✅ 提供统一的数据抽象层，便于扩展其他数据源
- ✅ 提供详细的日志和监控指标支持
- ✅ 支持 Docker 快速部署

---

## 🧩 功能特性

| 特性 | 描述 |
|------|------|
| 🔧 协议支持 | 完整实现 Model Context Protocol v0.1+ 标准 |
| 🧱 多数据库集成 | 内置 Redis 缓存、MySQL 持久化、MongoDB 文档存储 |
| 📊 指标监控 | 集成 Actuator + Micrometer，支持 Prometheus 监控 |
| 🚀 流式响应 | 使用 SSE（Server-Sent Events）实现实时响应推送 |
| 📦 容器化部署 | 提供 Dockerfile 和 docker-compose 示例 |
| 🧪 开发友好 | 提供本地调试配置、单元测试及 API 文档（Swagger/OpenAPI） |
| 📁 文件管理 | 支持文件上传、下载、检索等基础操作 |

---

## 📦 技术栈

| 技术 | 说明 |
|------|------|
| Java | JDK 17+ |
| Spring Boot | 2.x / 3.x 兼容（请根据依赖选择） |
| Spring Web MVC | 提供 RESTful 接口与 SSE 支持 |
| Spring Data | 整合 MongoDB、JPA（MySQL） |
| Spring Cache | 集成 Redis 缓存 |
| Spring AI | 提供 MCP 协议底层支持 |
| Maven | 项目构建工具 |
| Docker | 容器化部署支持 |
| Swagger UI | 接口文档可视化 |

---

## 📥 安装部署

### 1. 本地开发运行

```bash
git clone https://github.com/yourname/spring-boot-ai-mcp-server.git
cd spring-boot-ai-mcp-server
mvn clean package
java -jar target/spring-boot-ai-mcp-server.jar
```

默认访问地址：[http://localhost:8080](http://localhost:8080)

### 2. Docker 运行

```bash
docker build -t mcp-server .
docker run -d -p 8080:8080 mcp-server
```

或使用 `docker-compose.yml` 启动整个环境（含数据库）。

---

## 🛠️ 配置说明

在 `application.yml` 中配置数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mcp_db
    username: root
    password: root
  data:
    mongodb:
      uri: mongodb://localhost:27017/mcp_mongo
  redis:
    host: localhost
    port: 6379
```

---

## 📚 API 文档

访问接口文档：

```
http://localhost:8080/swagger-ui.html
```

或 OpenAPI JSON 地址：

```
http://localhost:8080/v3/api-docs
```

---

## 📈 监控指标

提供如下监控接口：

```
GET /actuator/health
GET /actuator/metrics
GET /actuator/prometheus
```

---

## 🧪 单元测试

执行测试：

```bash
mvn test
```

或查看覆盖率报告（需配置 JaCoCo）：

```bash
mvn jacoco:report
```

---

## 🤝 贡献指南

欢迎贡献代码、提交 Issue 或 PR！

1. Fork 本仓库
2. 创建新分支 (`git checkout -b feature/new-feature`)
3. 提交更改 (`git commit -m 'Add new feature'`)
4. Push 到远程分支 (`git push origin feature/new-feature`)
5. 创建 Pull Request

---

## 📜 许可证

本项目采用 MIT License，详情见 [LICENSE](LICENSE) 文件。

---

## 📣 致谢

感谢以下项目的支持：

- [Spring AI](https://github.com/spring-projects/spring-ai)
- [Model Context Protocol](https://github.com/Scope-AI/model-context-protocol)
- [OpenAI](https://openai.com)

---

## 📬 联系方式

如有问题，请提交 Issue 或联系作者邮箱：youremail@example.com
```
