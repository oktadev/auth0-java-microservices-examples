package com.example.apigateway.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HomeController {

    @GetMapping("/")
    public String howdy(@AuthenticationPrincipal OidcUser user) {
        return "Hello, " + user.getFullName();
    }

    @GetMapping("/print-token")
    public String printAccessToken(@RegisteredOAuth2AuthorizedClient("okta")
                                   OAuth2AuthorizedClient authorizedClient) {

        var accessToken = authorizedClient.getAccessToken();

        System.out.println("Access Token Value: " + accessToken.getTokenValue());
        System.out.println("Token Type: " + accessToken.getTokenType().getValue());
        System.out.println("Expires At: " + accessToken.getExpiresAt());

        return "Access token printed";
    }
}
