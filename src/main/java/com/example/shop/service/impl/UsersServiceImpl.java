package com.example.shop.service.impl;

import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Product;
import com.example.shop.entity.Users;
import com.example.shop.exception.customException.AccountExist;
import com.example.shop.exception.customException.DataNotFoundException;
import com.example.shop.exception.customException.DuplicatedUsername;
import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;
import com.example.shop.model.request.ProductToCartRequest;
import com.example.shop.model.response.LoginResponse;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UsersRepository;
import com.example.shop.service.UsersService;
import com.example.shop.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;




    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        LoginResponse loginResponse = new LoginResponse();
        Users users = usersRepository.findByUsername(loginDTO.getUsername());
        if (users == null) {
            throw new DataNotFoundException("Username Or PassWord exists");
        }
        if(!passwordEncoder.matches(loginDTO.getPassword(), users.getPassword())) {
            throw new BadCredentialsException("Wrong phone number or password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword(),
                users.getAuthorities()
        );
     //   authenticationManager.authenticate(authenticationToken);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication); // << Quan trọng
        } catch (AuthenticationException
                ex) {
            // Log hoặc throw lại lỗi để chắc chắn không bị swallow
            throw new BadCredentialsException("Authentication failed", ex);
        }
        try {
            loginResponse.setRole(users.getRoles());
            loginResponse.setUsername(users.getUsername());
            loginResponse.setToken(jwtTokenUtil.generateToken(users));
            return loginResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        Users users = modelMapper.map(registerDTO, Users.class);
        if(usersRepository.findByUsername(users.getUsername()) != null){
            throw new DuplicatedUsername("Username is already in use");
        }
        if(usersRepository.existsByPhoneOrEmail(registerDTO.getPhone(), registerDTO.getEmail())){
            throw new AccountExist("Phone or Email is already in use");
        }
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        users.setPassword(encodedPassword);
        users.setRoles(registerDTO.getRole());
        users.setStatus(1);
        usersRepository.save(users);
    }

    @Override
    public void deleteUser(Long id) {
        Users users = usersRepository.findById(id).get();
        users.setStatus(0);
        usersRepository.save(users);
    }

    @Override
    public void ChooseProduct(ProductToCartRequest productToCartRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username);
        Product product = productRepository.findById(productToCartRequest.getProductId()).get();
        Cart_Item cartItem = new Cart_Item();
        cartItem.setUsers(users);
        cartItem.setProduct(product);
        cartItem.setNumber(productToCartRequest.getNumber());
        cartItem.setMemoriesId(productToCartRequest.getMemoriesId());
        cartItemRepository.save(cartItem);
    }
}
