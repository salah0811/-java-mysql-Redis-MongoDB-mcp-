# ✨ Spring AI MCP 服务

<div align="center">

[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-007396?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)

</div>

<p align="center">
  <b>企业级多控制平台解决方案</b><br>
  <span>整合 Spring AI 与主流数据库的一站式服务</span>
</p>

## 🌟 核心特性

<div align="center">

| 类别           | 功能亮点                                                                 |
|----------------|--------------------------------------------------------------------------|
| **AI 核心**    | 🤖 Spring AI 深度整合 • 🧠 多模型支持 • 🔄 异步处理                      |
| **数据存储**   | 🗃️ MySQL 关系型存储 • 🍃 MongoDB 文档存储 • 🔥 Redis 高速缓存           |
| **运维监控**   | 🐳 容器化部署 • 📈 Prometheus 指标监控 • 🪵 Grafana 可视化               |
| **安全认证**   | 🔐 JWT 鉴权 • 🛡️ 角色权限控制 • 📛 请求限流                             |
| **开发支持**   | 📝 Swagger 文档 • ✉️ 事件驱动架构 • � CI/CD 就绪                        |

</div>

## 🚀 快速开始

<div align="center">

### 前置要求
- JDK 17+
- Maven 3.6+
- Docker 20.10+
- MySQL 8.0+
- MongoDB 4.4+
- Redis 6.0+
</div>
```bash
# 1. 克隆项目
git clone https://github.com/yourusername/spring-ai-mcp.git
cd spring-ai-mcp

# 2. 启动依赖服务 (Docker方式)
docker-compose -f docker/dependencies.yml up -d

# 3. 构建项目
mvn clean package

# 4. 运行服务
java -jar target/spring-ai-mcp.jar

📂 项目结构
<div align="center">
text

spring-ai-mcp/
├── src
│   ├── main
│   │   ├── java/com/example/mcp
│   │   │   ├── config       # 配置中心
│   │   │   ├── controller   # REST接口
│   │   │   ├── service      # 业务逻辑
│   │   │   ├── repository   # 数据访问
│   │   │   └── model        # 数据实体
│   │   └── resources
│   │       ├── application.yml    # 主配置
│   │       └── application-dev.yml # 开发配置
├── docker                    # 容器化配置
├── docs                      # 文档资源
└── scripts                   # 部署脚本

</div>
🔍 接口示例
<div align="center">
java

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI服务", description = "提供智能交互能力")
public class AiController {

    @Autowired
    private ChatClient chatClient;
    
    @Operation(summary = "对话接口")
    @PostMapping("/chat")
    public Response<String> chat(
        @Parameter(description = "输入消息") @RequestParam String message
    ) {
        return Response.success(chatClient.call(message));
    }
}

</div>
📊 数据库整合
<div align="center">
Redis 配置示例
yaml

spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0

MySQL 配置示例
yaml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mcp_db
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

</div>
🤝 参与贡献
<div align="center">

我们欢迎所有形式的贡献！请遵循以下流程：

    Fork 项目仓库

    创建特性分支 (git checkout -b feature/新功能)

    提交代码 (git commit -m '添加很棒的功能')

    推送分支 (git push origin feature/新功能)

    创建 Pull Request

</div>
📜 开源协议
<div align="center">

本项目采用 MIT 开源协议
</div>