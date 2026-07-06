package com.rajat.user_api.security.authentication;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rajat.user_api.security.config.SecurityProperties;

@Component
public class AuthenticationStrategyFactory {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthenticationStrategyFactory.class);

    private final SecurityProperties properties;

    private final Map<SecurityMode, AuthenticationStrategy> strategyMap =
            new EnumMap<>(SecurityMode.class);

    public AuthenticationStrategyFactory(
            List<AuthenticationStrategy> strategies,
            SecurityProperties properties) {

        this.properties = properties;

        strategies.forEach(strategy -> {
            logger.info("Registering authentication strategy: {}", strategy.getMode());
            strategyMap.put(strategy.getMode(), strategy);
        });
    }

    public AuthenticationStrategy getStrategy() {

        SecurityMode mode = properties.getMode();

        logger.info("Using authentication mode: {}", mode);

        AuthenticationStrategy strategy = strategyMap.get(mode);

        if (strategy == null) {
            throw new IllegalStateException(
                    "No AuthenticationStrategy found for mode: " + mode
            );
        }

        return strategy;
    }
}