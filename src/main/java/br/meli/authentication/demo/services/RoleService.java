package br.meli.authentication.demo.services;

import br.meli.authentication.demo.entities.Role;
import br.meli.authentication.demo.services.dto.RoleDTO;

public interface RoleService {
    Role create(RoleDTO roleDTO);
}
