package com.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.config.JwtUtil;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.service.impl.UserDetailsServiceImpl;

@RestController
public class AuthenticateController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	//generate token
	@PostMapping("/generate-token")
	public ResponseEntity<?>generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		try {
			authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		}catch(UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("User not found ");
		}
		//client authenticated
		UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception ex) {
        return ResponseEntity.ok(ex.getMessage());
    }
	
	private void authenticate(String username, String password) throws Exception {
		
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		}catch(DisabledException e) {
			throw new Exception("User Disabled "+e.getMessage());
		}catch(BadCredentialsException e) {
			throw new Exception("Invalid Credentials  "+e.getMessage());
		}
		
	}
}
