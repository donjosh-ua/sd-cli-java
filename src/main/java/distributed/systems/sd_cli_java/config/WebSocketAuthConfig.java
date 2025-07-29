package distributed.systems.sd_cli_java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import distributed.systems.sd_cli_java.common.TokenChannelInterceptor;

@Configuration
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
        registration.interceptors(new TokenChannelInterceptor());
    }
}
