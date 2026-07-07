package com.rajat.user_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCredentialsRequest {

    private String clientId;
    private String clientSecret;
}