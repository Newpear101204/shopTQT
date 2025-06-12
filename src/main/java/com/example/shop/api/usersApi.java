package com.example.shop.api;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.dto.RegisterDTO;
import com.example.shop.model.response.ProductResponse;
import com.example.shop.service.ProductService;
import com.example.shop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopqtq/")
@CrossOrigin(origins = "http://localhost:3000")
public class usersApi {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductService productService;

    // dang nhap
//    @PostMapping("/login")
//    public String login ( @RequestBody LoginDTO loginDTO) {
//       return  usersService.login(loginDTO);
//    }

    @PostMapping("/login")
    public Map<String, Object> login ( @RequestBody LoginDTO loginDTO) {
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


    //create and update
    @PostMapping("/createproduct")
    public void createOrUpdateProduct( @RequestBody ProductDTO productDTO) {
        System.out.println("🟢 Đã vào controller: " + productDTO);
        productService.createOrUpdateProduct(productDTO);
    }


    // delete
    @DeleteMapping("/deleteproduct/{id}")
    public void deleteProduct( @PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }


    // tim kiem san pham
    @GetMapping(name="/searchproduct")
    public List<ProductResponse> getProductByName () {

        return null;
    }

    // ban acc
    @DeleteMapping(name ="/deleteuser/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUser(id);
    }


}
