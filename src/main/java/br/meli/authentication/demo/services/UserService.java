package br.meli.authentication.demo.services;

import br.meli.authentication.demo.entities.User;
import br.meli.authentication.demo.services.dto.UserDTO;

import java.util.List;

public interface UserService {
    User create(UserDTO user);
    void addRole(String username, Long roleID);
    User findByUsername(String username);
    List<User> findAll();
}
