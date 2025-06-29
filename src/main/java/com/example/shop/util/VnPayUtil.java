package com.example.shop.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnPayUtil {

//    public static String buildQueryString(Map<String, String> params, String secretKey) {
//        // Sắp xếp theo tên tham số
//        SortedMap<String, String> sortedParams = new TreeMap<>(params);
//
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//
//        int index = 0;
//        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//
//            hashData.append(key).append('=').append(value);  // ❌ KHÔNG encode
//            query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII))  // ✅ CÓ encode
//                    .append('=')
//                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
//
//            if (++index < sortedParams.size()) {
//                hashData.append('&');
//                query.append('&');
//            }
//        }
//
//        String secureHash = hmacSHA512(secretKey, hashData.toString());
////        query.append("&vnp_SecureHashType=HmacSHA512");
//        query.append("&vnp_SecureHash=").append(secureHash);
//
//        // Debug log
//        System.out.println("HASH DATA (raw): " + hashData.toString());
//        System.out.println("HASH: " + secureHash);
//        System.out.println("QUERY STRING: " + query.toString());
//
//        return query.toString();
//    }


public static String buildQueryString(Map<String, String> params, String secretKey) {
    SortedMap<String, String> sortedParams = new TreeMap<>(params);

    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();

    int index = 0;
    for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();

        // ❌ Bỏ qua các trường không được dùng trong hash
        if ("vnp_SecureHash".equals(key) || "vnp_SecureHashType".equals(key)) {
            continue;
        }

        hashData.append(key).append('=').append(value); // KHÔNG encode
        query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII)) // CÓ encode
                .append('=')
                .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

        if (++index < sortedParams.size()) {
            hashData.append('&');
            query.append('&');
        }
    }

    // ✅ Tạo chữ ký từ hashData
    String secureHash = hmacSHA512(secretKey, hashData.toString());

    // ✅ Thêm vào query cuối cùng
    query.append("&vnp_SecureHashType=HmacSHA512");
    query.append("&vnp_SecureHash=").append(secureHash);

    // Debug log
    System.out.println("HASH DATA (raw): " + hashData.toString());
    System.out.println("HASH: " + secureHash);
    System.out.println("QUERY STRING: " + query.toString());

    return query.toString();
}





    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secret);
            return Hex.encodeHexString(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new RuntimeException("HMAC SHA512 error", ex);
        }
    }
}

