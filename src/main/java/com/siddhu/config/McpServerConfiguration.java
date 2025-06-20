package com.siddhu.config;

import com.siddhu.service.MCPServerRedisServiceClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siddhu.service.MCPServerMongoServiceClient;

@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(
            MCPServerMongoServiceClient mongoServiceClient,
            MCPServerRedisServiceClient redisServiceClient) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(mongoServiceClient, redisServiceClient)
                .build();
    }
}
