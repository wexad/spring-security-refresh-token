package com.wexad.spring_security_refresh_token.controller;

import com.wexad.spring_security_refresh_token.dto.TokenRequestDTO;
import com.wexad.spring_security_refresh_token.dto.Tokens;
import com.wexad.spring_security_refresh_token.model.AuthUser;
import com.wexad.spring_security_refresh_token.repository.AuthUserRepository;
import com.wexad.spring_security_refresh_token.security.JwtTokenUtil;
import com.wexad.spring_security_refresh_token.service.CustomUserDetailsService;
import com.wexad.spring_security_refresh_token.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService, RefreshTokenService refreshTokenService, RefreshTokenService refreshTokenService1) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenService = refreshTokenService1;
    }

    @PostMapping("/token")
    public Tokens getToken(@RequestBody TokenRequestDTO tokenRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenRequestDTO.username(), tokenRequestDTO.password());
        authenticationManager.authenticate(authenticationToken);
        String refreshToken = jwtTokenUtil.generateRefreshToken(tokenRequestDTO.username());
        String accessToken = jwtTokenUtil.generateToken(tokenRequestDTO.username());
        refreshTokenService.save(refreshToken, tokenRequestDTO.username());
        return new Tokens(accessToken, refreshToken);
    }

    @PostMapping("/register")
    public TokenRequestDTO register(@RequestBody TokenRequestDTO user) {
        authUserRepository.save(AuthUser.builder()
                .username(user.password())
                .password(passwordEncoder.encode(user.password()))
                .role("USER")
                .build());
        return user;
    }

    @PostMapping("/refresh")
    public Tokens refresh(@RequestBody Tokens tokens) {
        String username = jwtTokenUtil.extractUsername(tokens.refreshToken());
        jwtTokenUtil.validateToken(tokens.refreshToken(),
                customUserDetailsService.loadUserByUsername(username));
        refreshTokenService.findByToken(tokens.refreshToken());
        return new Tokens(jwtTokenUtil.generateToken(username), null);
    }
}
