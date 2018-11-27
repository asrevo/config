package org.revo.Service;

import org.revo.Domain.User;

import java.util.Optional;

public interface UserService {
    void save(User user);

    Optional<User> findByEmail(String email);
}
