package com.example.shop.api;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.dto.RegisterDTO;
import com.example.shop.model.request.ProductRequest;
import com.example.shop.model.response.LoginResponse;
import com.example.shop.model.response.ProductResponse;
import com.example.shop.service.ProductService;
import com.example.shop.service.UsersService;

import com.example.shop.service.impl.CloudinaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/shopqtq/")
@CrossOrigin(origins = "http://localhost:3000")
public class usersApi {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;


    // dang nhap
    @PostMapping("/login")
    public LoginResponse login (@RequestBody LoginDTO loginDTO) {
       return  usersService.login(loginDTO);
    }

    // dang ky
    @PostMapping("/register")
    public void register (@RequestBody RegisterDTO registerDTO) {
        usersService.register(registerDTO);
    }

    // get all
    @GetMapping("/allproducts")
    public List<ProductResponse> getAll (){
        return productService.getAllProducts();
    }

   // create and update
//    @PostMapping("/createproduct")
//    public void createOrUpdateProduct( @RequestBody ProductDTO productDTO) {
//        productService.createOrUpdateProduct(productDTO);
//    }

    @PostMapping(value = "/createproduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createOrUpdateProduct(
            @RequestPart("product") String productJson,
            @RequestPart("images") MultipartFile[] images
    ) {
        try {
            // Ép charset UTF-8 từ raw byte
            String utf8Json = new String(productJson.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // Dùng Jackson để parse JSON thủ công
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(utf8Json, ProductDTO.class);

            List<String> imageUrls = cloudinaryService.uploadMultipleFiles(images);
            productDTO.setImages(imageUrls);
            productService.createOrUpdateProduct(productDTO);

            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo sản phẩm: " + e.getMessage());
        }
    }

    // delete
    @DeleteMapping("/deleteproduct/{id}")
    public void deleteProduct( @PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

    // tim kiem san pham
    @GetMapping("/searchproducts")
    public List<ProductResponse> getProductByName (@RequestBody ProductRequest productRequest) {
        return productService.searchProduct(productRequest) ;
    }

    // ban acc
    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUser(id);
    }

    // xóa ảnh
    @DeleteMapping("/deleteproductimage")
    public ResponseEntity<?> deleteProductImage(
            @RequestParam Long productId,
            @RequestParam String url
    ) {
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        productService.deleteImages(productId, decodedUrl);
        return ResponseEntity.ok("Deleted");
    }

}
