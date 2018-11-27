package org.revo.Service.Impl;

import org.revo.Domain.User;
import org.revo.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream().filter(it -> it.getEmail().equalsIgnoreCase(email)).findAny();
    }
}
