package com.example.shop.api;

import com.example.shop.model.dto.ChatRequest;
import com.example.shop.model.dto.ConversationMemory;
import com.example.shop.service.impl.GeminiService;
import com.example.shop.service.impl.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopqtq/")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final GeminiService geminiService;
    private final QueryService queryService;
    private final ConversationMemory conversationMemory;

    public ChatController(GeminiService geminiService,
                          QueryService queryService,
                          ConversationMemory conversationMemory) {
        this.geminiService = geminiService;
        this.queryService = queryService;
        this.conversationMemory = conversationMemory;
    }

    @PostMapping("/chatbot")
    public ResponseEntity<String> chatWithDatabase(@RequestBody ChatRequest request) {
        try {
            String question = request.getMessage();
            conversationMemory.addMessage("user", question);

            String sql = geminiService.generateSQLFromQuestion(question);

            sql = sql.replaceAll("(?i)```sql", "")
                    .replaceAll("```", "")
                    .trim();

            if (!sql.toLowerCase().startsWith("select") || sql.contains("--") || sql.length() < 15) {
                String reply = geminiService.askWithHistory(conversationMemory.getHistory());
                conversationMemory.addMessage("model", reply);
                return ResponseEntity.ok(clean(reply));
            }

            List<Map<String, Object>> result = queryService.runSQLQuery(sql);

            String reply;
            if (result.isEmpty()) {
                reply = "Không có kết quả phù hợp với yêu cầu của bạn.";
            } else {
                String jsonResult = new ObjectMapper().writeValueAsString(result);
                reply = geminiService.askGeminiWithResult(question, jsonResult);
            }

            conversationMemory.addMessage("model", reply);
            return ResponseEntity.ok(clean(reply));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    private String clean(String text) {
        return text.replaceAll("\\*\\*", "");
    }
}
