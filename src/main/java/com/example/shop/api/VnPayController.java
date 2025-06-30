package com.example.shop.api;

import com.example.shop.config.VnPayConfig;
import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Users;
import com.example.shop.repository.UsersRepository;
import com.example.shop.util.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay/")
@RequiredArgsConstructor
public class VnPayController {

    private final VnPayConfig config;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private HttpServletRequest request;

//    @PostMapping("/create-payment")
//    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> payload) {
//        long amount = Long.parseLong(payload.get("amount").toString()) * 100; // VND * 100
//
//        String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
//        String vnp_OrderInfo = "Thanh toan don hang";
//        String orderType = "other";
//        String vnp_IpAddr = "127.0.0.1";
//        String vnp_BankCode = "VNBANK";
//
//        Map<String, String> vnp_Params = new TreeMap<>();
//        vnp_Params.put("vnp_Version", "2.1.0");
//        vnp_Params.put("vnp_Command", "pay");
//        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//        vnp_Params.put("vnp_OrderType", orderType);
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//
//        String queryUrl = VnPayUtil.buildQueryString(vnp_Params, config.getHashSecret());
//        String redirectUrl = config.getPayUrl() + "?" + queryUrl;
//
//        return ResponseEntity.ok(Map.of("url", redirectUrl));
//    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment() throws Exception {
        // Lấy username từ context bảo mật
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByUsername(username);
        List<Cart_Item> cartItems = user.getCart_items();

        // Tính tổng tiền từ giỏ hàng
        long amount = 0;
        for (Cart_Item item : cartItems) {
            amount += item.getProduct().getPrice() * item.getNumber();
        }
        amount += 20000; // Phí vận chuyển
        long amountForVnPay = amount * 100; // Nhân 100 theo yêu cầu của VNPAY

        // Các thông tin cần thiết cho VNPAY
        String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String vnp_OrderInfo = "Thanh toan don hang";
        String orderType = "other";
//        String vnp_IpAddr = "127.0.0.1"; // Hoặc lấy từ request sau
        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_BankCode = "VNBANK";

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amountForVnPay));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
        vnp_Params.put("vnp_CreateDate", formatter.format(new Date()));
        vnp_Params.put("vnp_SecureHashType", "HmacSHA512");
        //
        vnp_Params.put("vnp_BankCode", vnp_BankCode);
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (hashData.length() > 0) {
                hashData.append("&");
            }
            hashData.append(entry.getKey()).append("=").append(entry.getValue());
        }
        String secureHash = alo(config.getHashSecret(), hashData.toString());
        vnp_Params.put("vnp_SecureHash", secureHash);


        Calendar expire = Calendar.getInstance();
        expire.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
        expire.add(Calendar.MINUTE, 15); // Hết hạn sau 15 phút

        String vnp_ExpireDate = formatter.format(expire.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo URL query + chữ ký
        String queryUrl = VnPayUtil.buildQueryString(vnp_Params, config.getHashSecret());
        String redirectUrl = config.getPayUrl() + "?" + queryUrl;
        System.out.println("HASH DATA: " + queryUrl); // debug
        System.out.println("FULL REDIRECT URL: " + redirectUrl);

        System.out.println("=== VNPAY REQUEST ===");
        System.out.println("TxnRef: " + vnp_TxnRef);
        System.out.println("Amount: " + amountForVnPay);
        System.out.println("CreateDate: " + vnp_Params.get("vnp_CreateDate"));
        System.out.println("ExpireDate: " + vnp_Params.get("vnp_ExpireDate"));
        System.out.println("Redirect URL: " + redirectUrl);
        System.out.println("=== END ===");


        return ResponseEntity.ok(Map.of("url", redirectUrl));
    }

    public static String alo(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hmacBytes).toUpperCase();
    }

}
