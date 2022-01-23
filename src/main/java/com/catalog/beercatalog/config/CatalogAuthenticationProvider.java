package com.catalog.beercatalog.config;

import java.util.ArrayList;
import java.util.List;

import com.catalog.beercatalog.entity.Authority;
import com.catalog.beercatalog.entity.Provider;
import com.catalog.beercatalog.repository.ProviderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CatalogAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ProviderRepository providerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Provider provider = providerRepo.findByEmail(username);
        if (provider != null) {
            if (passwordEncoder.matches(pwd, provider.getPwd())) {
                return new UsernamePasswordAuthenticationToken(provider, pwd, getGrantedAuthorities(provider.getAuthorities()));
            } else {
                throw new BadCredentialsException("Password is not valid.");
            }
        } else {
            throw new BadCredentialsException("No user found with those credentials.");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Authority> authorities) {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        for (Authority auth : authorities) {
            grantedAuths.add(new SimpleGrantedAuthority(auth.getName()));
        }
        return grantedAuths;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
