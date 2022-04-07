package br.meli.authentication.demo.services;

import br.meli.authentication.demo.entities.Role;
import br.meli.authentication.demo.repositories.RoleRepository;
import br.meli.authentication.demo.services.dto.RoleDTO;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(RoleDTO roleDTO) {
        Role role = new Role(null, roleDTO.getName());
        return roleRepository.save(role);
    }
}
