package com.rajat.user_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajat.user_api.entity.OAuthClient;

public interface OAuthClientRepository extends JpaRepository<OAuthClient, Long> {

    Optional<OAuthClient> findByClientId(String clientId);
}
