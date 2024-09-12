package com.wexad.spring_security_refresh_token.service;

import com.wexad.spring_security_refresh_token.model.RefreshToken;
import com.wexad.spring_security_refresh_token.repository.AuthUserRepository;
import com.wexad.spring_security_refresh_token.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserRepository authUserRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService, AuthUserRepository authUserRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.authUserRepository = authUserRepository;
    }

    public void save(String refreshToken, String username) {
        refreshTokenRepository.save(
                RefreshToken.builder().
                        userId(getIdWithUsername(username)).
                        token(refreshToken).
                        build()
        );
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Could not find Token"));
    }

    public Long getIdWithUsername(String username) {
        return authUserRepository.getIdWithUsername(username);
    }
}
