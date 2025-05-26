package com.hans.aggreation.webadmin.core.service;

import com.hans.aggreation.webadmin.core.model.UserModel;
import com.hans.aggreation.webadmin.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel getUser(String username) {
        UserModel query = new UserModel();
        query.setUsername(username);
        Optional<UserModel> one = userRepository.findOne(Example.of(query));
        return one.orElse(null);
    }
}
