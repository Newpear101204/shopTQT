package com.example.shop.api;

import com.example.shop.config.Config;
import com.example.shop.config.VnPayConfig;
import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Orders;
import com.example.shop.entity.Orders_item;
import com.example.shop.entity.Users;
import com.example.shop.model.request.OrderRequest;
import com.example.shop.model.request.OrdersRequest;
import com.example.shop.repository.MemoriesRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UsersRepository;
import com.example.shop.util.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VnPayController {

    private final VnPayConfig config;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemoriesRepository memoriesRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody OrderRequest request, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
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

        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(httpServletRequest);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountForVnPay));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl ); // Custom ID nếu có
        String encodedAddress = URLEncoder.encode(request.getAdress(), StandardCharsets.UTF_8);
        String encodedUsername = URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8);
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl + "?address=" + encodedAddress + "&username=" + encodedUsername);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp & ký
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            if (i != fieldNames.size() - 1) {
                hashData.append('&');
                query.append('&');
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        String paymentUrl = Config.vnp_PayUrl + "?" + query;

        // Optionally: lưu order vào DB trạng thái chờ thanh toán

        Map<String, String> response = new HashMap<>();
        response.put("url", paymentUrl);

        System.out.println(">> Payment URL gửi tới FE:");
        System.out.println(paymentUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-return")
    public void vnpayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String address = params.get("address");

        if ("00".equals(vnp_ResponseCode)) {
            String username = params.get("username");
            Users user = usersRepository.findByUsername(username);

            Orders orders = new Orders();
            orders.setUsers(user);
            orders.setStatus("OK");
            orders.setShipFee(20000);
            orders.setShippingAdress(address != null ? address : "Không có địa chỉ");
            orders.setPaymentMethod("vnpay");

            int total = 0;
            List<Orders_item> orders_items = new ArrayList<>();
            for (Cart_Item cartItem : user.getCart_items()) {
                Orders_item item = new Orders_item();
                item.setOrders(orders);
                item.setProduct(cartItem.getProduct());
                item.setMemoriesId(memoriesRepository.findByCapacity(cartItem.getMemoriesId().toString()).getId());
                item.setQuantity(cartItem.getNumber());
                item.setPrice(cartItem.getProduct().getPrice());
                orders_items.add(item);
                total += item.getQuantity() * item.getPrice();
                productRepository.deleteCart(cartItem.getId());
            }

            orders.setTotal(total + 20000);
            orders.setOrders_items(orders_items);
            orderRepository.save(orders);

            response.sendRedirect("http://localhost:3000/vnpay-return?vnp_ResponseCode=00");
        } else {
            response.sendRedirect("http://localhost:3000/payment-failed");
        }
    }


}
