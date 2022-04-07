package br.meli.authentication.demo.services.dto;


import br.meli.authentication.demo.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;


@Data
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String username;
    private String password;
}

