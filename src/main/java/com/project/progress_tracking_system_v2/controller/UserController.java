package com.project.progress_tracking_system_v2.controller;

import com.project.progress_tracking_system_v2.entity.User;
import com.project.progress_tracking_system_v2.security.JwtService;
import com.project.progress_tracking_system_v2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user)  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword())
        );
        if (authentication.isAuthenticated()) { return new ResponseEntity<>(jwtService.generateToken(user.getName()), HttpStatus.OK); }
        throw new AuthenticationCredentialsNotFoundException("User couldn't be authenticated");
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> csrf(HttpServletRequest request) {
        return new ResponseEntity<>(userService.loadAllUsers(), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("my_info")
    public ResponseEntity<UserDetails> getUserInfo() throws Exception {
        UserDetails userDetails = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }


}
