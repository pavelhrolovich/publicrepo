package com.gmail.phrolovich.security;

import com.amazonaws.auth.AWSSessionCredentials;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class STSAuthentication extends AbstractAuthenticationToken {
    private AWSSessionCredentials awsSessionCredentials;

    public STSAuthentication(AWSSessionCredentials awsSessionCredentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.awsSessionCredentials = awsSessionCredentials;
    }

    @Override
    public Object getCredentials() {
        return awsSessionCredentials;
    }

    @Override
    public Object getPrincipal() {
        return awsSessionCredentials.getAWSAccessKeyId();
    }
}


