package com.gmail.phrolovich.security;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.model.AWSSecurityTokenServiceException;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.gmail.phrolovich.integration.AWSServicesFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Slf4j
@AllArgsConstructor
public class STSAuthenticationManager implements AuthenticationManager {
    private final AWSServicesFactory factory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AWSCredentials credentials = new BasicAWSCredentials(authentication.getName(), authentication.getCredentials().toString());

        AWSSecurityTokenService stsClient = factory.tokenService(credentials);

        GetSessionTokenRequest tokenRequest = new GetSessionTokenRequest();
        tokenRequest.setDurationSeconds(900);

        GetSessionTokenResult sessionToken = null;
        try {
            sessionToken = stsClient.getSessionToken(tokenRequest);
        } catch (AWSSecurityTokenServiceException ex) {
            log.warn("Credentials are not valid - access denied");
            throw new BadCredentialsException("Credentials are not valid");
        }

        Credentials sessionCredentialsResult = sessionToken.getCredentials();

        AWSSessionCredentials sessionCredentials = new BasicSessionCredentials(
                sessionCredentialsResult.getAccessKeyId(),
                sessionCredentialsResult.getSecretAccessKey(),
                sessionCredentialsResult.getSessionToken()
        );

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("read");
        STSAuthentication stsAuthentication = new STSAuthentication(sessionCredentials, Collections.singletonList(simpleGrantedAuthority));
        stsAuthentication.setAuthenticated(true);
        return stsAuthentication;
    }
}
