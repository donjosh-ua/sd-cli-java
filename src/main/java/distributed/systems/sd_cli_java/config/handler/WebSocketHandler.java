package distributed.systems.sd_cli_java.config.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Set<WebSocketSession>> planSessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {

        String email = getQueryParam(session, "email");
        String planId = getQueryParam(session, "planId");

        if (email != null) {
            userSessionMap.put(email, session);
            log.info("WebSocket connected for {}", email);
        } else {
            log.warn("WebSocket connected without email parameter");
        }

        if (planId != null)
            bindSessionToPlan(session, planId);
        else
            log.warn("WebSocket connected without planId parameter");

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {

        for (Set<WebSocketSession> sessions : planSessions.values())
            sessions.remove(session);

        userSessionMap.values().removeIf(s -> s.getId().equals(session.getId()));

        log.info("WebSocket connection closed: {}", status);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        log.info("Received message from client: {}", message.getPayload());
    }

    public void bindSessionToPlan(WebSocketSession session, String planId) {
        planSessions.computeIfAbsent(planId, k -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("Bound WebSocket session {} to plan {}", session.getId(), planId);
    }

    public WebSocketSession getSessionForUser(String email) {
        return userSessionMap.get(email);
    }

    public void sendMessageToPlan(String planId, String payload) {

        Set<WebSocketSession> sessions = planSessions.get(planId);

        if (sessions == null || sessions.isEmpty()) {
            log.warn("No WebSocket sessions for plan {}", planId);
            return;
        }

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(payload));
                } catch (IOException e) {
                    log.error("Error sending WebSocket message: {}", e.getMessage());
                }
            }
        }
    }

    private String getQueryParam(WebSocketSession session, String key) {

        URI uri = session.getUri();

        if (uri == null)
            return null;

        String query = uri.getQuery();

        if (query == null)
            return null;

        return Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2))
                .filter(p -> p.length == 2 && p[0].equals(key))
                .map(p -> URLDecoder.decode(p[1], StandardCharsets.UTF_8))
                .findFirst()
                .orElse(null);
    }
}
