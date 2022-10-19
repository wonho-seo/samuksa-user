package com.samuksa.user.security.oauth2.service;

import com.samuksa.user.security.oauth2.entity.OAuthAttributes;
import com.samuksa.user.db.table.samuksa_user_db.entity.AuthEmail;
import com.samuksa.user.db.table.samuksa_user_db.repository.AuthEmailRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final CustUserRepository custUserRepository;
    private final AuthEmailRepository authEmailRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        if (!custUserRepository.existsByEmail(oAuthAttributes.getEmail())){
            AuthEmail authEmail = authEmailRepository.findByauthEmail(oAuthAttributes.getEmail()).orElse(AuthEmail.builder()
                    .authEmail(oAuthAttributes.getEmail())
                    .authKey("")
                    .build());
            authEmail.setAuth(1);
            authEmailRepository.save(authEmail);
        }
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), oAuthAttributes.getAttributes(), oAuthAttributes.getNameAttributeKey());
    }
}
