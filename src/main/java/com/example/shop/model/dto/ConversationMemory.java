package com.example.shop.model.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConversationMemory {
    private final List<Map<String, String>> history = new ArrayList<>();

    public void addMessage(String role, String content) {
        history.add(Map.of("role", role, "content", content));
    }

    public List<Map<String, String>> getHistory() {
        return history;
    }

    public void clear() {
        history.clear();
    }
}
