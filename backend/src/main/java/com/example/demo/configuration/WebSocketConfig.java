package com.example.demo.configuration;

import com.example.demo.handler.HandShakeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import java.net.Inet4Address;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final HandShakeInterceptor handShakeInterceptor;

    @SneakyThrows
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String ip = Inet4Address.getLocalHost().getHostAddress();
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins(ip)
                .addInterceptors(handShakeInterceptor)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}