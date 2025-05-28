package com.hans.aggreation.webadmin.core.pojo;

import com.hans.aggreation.webadmin.core.model.UserModel;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails {
    private UserModel model;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return model.getPassword();
    }

    @Override
    public String getUsername() {
        return model.getUsername();
    }
}
