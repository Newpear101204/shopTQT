package com.example.shop.service.impl;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GeminiService {

    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyCg80n1nlVUkYsWda3QJJ0tRtKdvATzqHg";

    public String generateSQLFromQuestion(String userQuestion) throws IOException {
        String schema = """
            Dưới đây là thông tin chi tiết về các bảng trong cơ sở dữ liệu:
            
            Bảng `product`:
            - id (int): mã sản phẩm (PK)
            - name (varchar): tên sản phẩm
            - price (float): giá bán
            - describes (varchar): mô tả sản phẩm
            - brands_id (int): khóa ngoại tới brand.id
            - categories_id (int): khóa ngoại tới categories.id
            - bestseller (tinyint): 1 nếu là sản phẩm bán chạy
            - created_date, created_by, modified_date, modified_by
            
            Bảng `categories`:
            - id (int), name (varchar): ví dụ 'LAPTOP', 'CAMERA'
            - code (varchar): mã rút gọn
            → Mỗi sản phẩm thuộc một danh mục (product.categories_id = categories.id)
            
            Bảng `brand`:
            - id (int), name (varchar): ví dụ 'DELL', 'SONY', 'FUJIFILM'
            - code (varchar), describes (varchar): mô tả về thương hiệu
            → Mỗi sản phẩm thuộc một thương hiệu (product.brands_id = brand.id)
            
            Bảng `product_image`:
            - id (int), url (varchar): đường dẫn ảnh
            - product_id (int): khóa ngoại tới product.id
            → Một sản phẩm có thể có nhiều ảnh
            
            Bảng `memories`:
              - id (int)
              - capacity (int): dung lượng bộ nhớ, đơn vị là GB
              → Ví dụ:
                - 128 đại diện cho 128GB
                - 256 đại diện cho 256GB
                - 512 đại diện cho 512GB
                - 1 đại diện cho 1TB (tức là 1024GB)
            
            Bảng `product_memories`:
            - id (int)
            - product_id (int): khóa ngoại tới product.id
            - memories_id (int): khóa ngoại tới memories.id
            → Một sản phẩm có thể có nhiều phiên bản bộ nhớ
            
            Bảng `users`:
            - id (int), username, password, email, phone
            - roles (varchar): 'admin' hoặc 'cus'
            - status (int): 1 là hoạt động, 0 là bị khóa
            - created_date, created_by, modified_date, modified_by
            
            Bảng `cart_item`:
            - id (int)
            - users_id (int): khóa ngoại tới users.id
            - products_id (int): khóa ngoại tới product.id
            - created_date, created_by, modified_date, modified_by
            
            Bảng `orders`:
            - id (int)
            - users_id (int): khóa ngoại tới users.id
            - status (varchar): ví dụ 'PENDING', 'SHIPPED'
            - total (int): tổng tiền
            - shipping_adress (varchar): địa chỉ giao hàng
            - created_date, created_by, modified_date, modified_by
            
            Bảng `orders_item`:
            - id (int)
            - orders_id (int): khóa ngoại tới orders.id
            - products_id (int): khóa ngoại tới product.id
            - quantity (int), price (int): giá tại thời điểm đặt hàng
            - created_date, created_by, modified_date, modified_by
            
            Bảng `contact`:
            - id (int)
            - phone, email, adress
            - created_date, created_by, modified_date, modified_by
            
            ❗Hướng dẫn tạo SQL:
            - Nếu người dùng hỏi về loại sản phẩm như laptop, camera → dùng JOIN với bảng `categories`
            - Nếu hỏi theo hãng như SONY, DELL → dùng JOIN với bảng `brand`
            - Nếu hỏi sản phẩm theo bộ nhớ như "128GB", "256GB", "1TB":
                 + JOIN với bảng `product_memories` và `memories`
                 + Dùng WHERE memories.capacity = <giá trị GB>, ví dụ:
                   - '128GB' → WHERE capacity = 128
                   - '1TB' → WHERE capacity = 1024 (nếu bạn quy đổi)
                   - Nếu để 1 đại diện cho 1TB thì WHERE capacity = 1
                   
            - Nếu hỏi ảnh sản phẩm → JOIN với `product_image`
            - Nếu hỏi sản phẩm bán chạy → WHERE bestseller = 1
            
            ❗Yêu cầu đầu ra:
            - KHÔNG giải thích
            - KHÔNG sử dụng định dạng markdown (tức là KHÔNG ```sql hoặc ``` gì cả)
            - Chỉ trả về một câu SQL thuần chính xác và ngắn gọn
            """;

        String prompt = schema + "\n\nCâu hỏi: " + userQuestion;
        return ask(prompt);
    }

    public String askGeminiWithResult(String question, String sqlResult) throws IOException {
        String prompt = """
            Người dùng hỏi: %s
            Kết quả trả về từ database: %s
            Hãy trả lời người dùng bằng giọng tự nhiên, dễ hiểu.
        """.formatted(question, sqlResult);

        return ask(prompt);
    }

    public String askWithHistory(List<Map<String, String>> messages) throws IOException {
        StringBuilder parts = new StringBuilder();
        for (Map<String, String> msg : messages) {
            parts.append("{\"role\": \"").append(msg.get("role")).append("\", ")
                    .append("\"parts\": [{\"text\": \"").append(msg.get("content")).append("\"}]},");

        }
        String jsonBody = String.format("{\"contents\": [%s]}", parts.substring(0, parts.length() - 1));
        return sendRequest(jsonBody);
    }

    public String ask(String prompt) throws IOException {
        String jsonBody = """
            {
              "contents": [
                {
                  "parts": [{ "text": "%s" }]
                }
              ]
            }
        """.formatted(prompt.replace("\"", "\\\""));

        return sendRequest(jsonBody);
    }

    private String sendRequest(String jsonBody) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(ENDPOINT).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(jsonBody.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) response.append(line);
        in.close();

        Matcher m = Pattern.compile("\"text\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL).matcher(response.toString());
        return m.find() ? StringEscapeUtils.unescapeJava(m.group(1)) : "Không có phản hồi từ AI.";
    }
}
