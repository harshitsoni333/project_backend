package com.soni.usermanagement.controller;

import com.soni.usermanagement.dto.AuthenticationRequest;
import com.soni.usermanagement.dto.AuthenticationResponse;
import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.PasswordNotValid;
import com.soni.usermanagement.methods.EmailMessage;
import com.soni.usermanagement.methods.PasswordGenerator;
import com.soni.usermanagement.security.JwtTokenUtil;
import com.soni.usermanagement.services.EmailService;
import com.soni.usermanagement.services.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloResource
 */
@RestController
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private EmailService emailService;

    static String softToken;
    static String jwt;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping("/hello")
    public String hello() { return "hello world"; }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) 
    throws Exception{

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUserName());
        
        try {
             
            if(!encoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Incorrect password");
            }
            // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            //     authenticationRequest.getUsername(), 
            //     authenticationRequest.getPassword()));     

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);            
        }

        jwt = jwtTokenUtil.generateToken(userDetails);

        // sending a soft token
        softToken = PasswordGenerator.generatePassword();
        emailService.sendMail(
            userDetails.getUsername(), 
            EmailMessage.makeSubjectFor("soft token", userDetails.getUsername()), 
            EmailMessage.makePasswordMessageFor(
                "soft token", 
                softToken, 
                userDetails.getUsername(),
                userDetails.getPassword()));

        return ResponseEntity.ok(new ResponseMessage("An OTP has been sent to your email ID"));
    }
    
    @PostMapping("/authenticateOTP")
    public ResponseEntity<?> verifyAuthenticationToken(@RequestBody String enteredSoftToken) {

        if(!softToken.equals(enteredSoftToken)) {
            throw new PasswordNotValid("OTP is wrong: " + enteredSoftToken);
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}