package com.example.shop.service.impl;

import com.example.shop.entity.Users;
import com.example.shop.exception.customException.AccountExist;
import com.example.shop.exception.customException.DataNotFoundException;
import com.example.shop.exception.customException.DuplicatedUsername;
import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;
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

import java.util.HashMap;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;




//    @Override
//    public String login(LoginDTO loginDTO) {
//        Users users = usersRepository.findByUsername(loginDTO.getUsername());
//        if (users == null) {
//            throw new DataNotFoundException("Username Or PassWord exists");
//        }
//        if(!passwordEncoder.matches(loginDTO.getPassword(), users.getPassword())) {
//            throw new BadCredentialsException("Wrong phone number or password");
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                loginDTO.getUsername(), loginDTO.getPassword(),
//                users.getAuthorities()
//        );
//     //   authenticationManager.authenticate(authenticationToken);
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication); // << Quan trọng
//        } catch (AuthenticationException
//                ex) {
//            // Log hoặc throw lại lỗi để chắc chắn không bị swallow
//            throw new BadCredentialsException("Authentication failed", ex);
//        }
//        try {
//            return jwtTokenUtil.generateToken(users);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        Users users = usersRepository.findByUsername(loginDTO.getUsername());
        if (users == null) {
            throw new DataNotFoundException("Username or Password is incorrect");
        }

        // So sánh password
        if (!passwordEncoder.matches(loginDTO.getPassword(), users.getPassword())) {
            throw new BadCredentialsException("Wrong username or password");
        }

        // Xác thực người dùng
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(), loginDTO.getPassword(), users.getAuthorities());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Authentication failed", ex);
        }

        try {
            // Tạo JWT token
            String token = jwtTokenUtil.generateToken(users);

            // Trả về JSON object
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", users.getUsername());
            response.put("role", users.getRoles()); // hoặc name, tùy bạn đặt trong entity

            return response;
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
}
