package com.hans.aggreation.webadmin.core.service;

import com.hans.aggreation.webadmin.core.model.UserModel;
import com.hans.aggreation.webadmin.core.pojo.CustomUserDetails;
import com.hans.aggreation.webadmin.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Security 集成简单登录
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel query = new UserModel();
        query.setUsername(username);
        Optional<UserModel> one = userRepository.findOne(Example.of(query));
        if (!one.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserModel userModel = one.get();
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setModel(userModel);
        return customUserDetails;
    }
}
