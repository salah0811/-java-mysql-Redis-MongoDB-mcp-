🌟 Spring AI MCP Server（Java 实现）

    一个基于 Java 的 Model Context Protocol (MCP) 服务实现，整合了 Redis、MySQL、MongoDB 等多种数据存储方式，适用于构建智能模型上下文交互平台。

📦 项目背景

随着大模型的快速发展，Model Context Protocol（MCP）成为连接工具与模型的重要桥梁。然而，目前市面上使用 Java 构建 MCP 服务的资料和项目较少，且缺乏系统性整合。

本项目旨在提供一个开箱即用的 Java 版 MCP 服务端实现，便于开发者快速接入、扩展并集成到实际业务中。

🔧 技术栈

    Spring Boot 2.7.18）
    Spring AI
    MCP 协议支持
    Redis：用于缓存模型上下文
    MySQL：持久化用户、会话等信息
    MongoDB：非结构化数据存储（如日志、历史记录等）

    🧩 核心功能
功能	描述
✅ MCP 协议服务	提供标准的 MCP 接口，支持工具注册、模型上下文管理
✅ 上下文持久化	使用 MySQL 持久化会话与用户信息
✅ 缓存加速	使用 Redis 缓存高频访问的上下文数据
✅ 日志与历史	使用 MongoDB 存储完整的历史操作日志
✅ 可扩展性强	支持自定义工具插件、协议扩展

❤️ 致谢

感谢所有 Star 和贡献者！让我们一起推动 Java 在 AI 工程生态中的发展！
