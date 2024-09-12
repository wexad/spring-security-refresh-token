package com.wexad.spring_security_refresh_token.repository;

import com.wexad.spring_security_refresh_token.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);

    @Query("SELECT au.id FROM AuthUser au WHERE au.username = :username")
    Long getIdWithUsername(@Param("username") String username);
}