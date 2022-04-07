package br.meli.authentication.demo.controllers;

import br.meli.authentication.demo.entities.Role;
import br.meli.authentication.demo.services.RoleService;
import br.meli.authentication.demo.services.dto.RoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("v1/api/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody RoleDTO roleDTO, HttpServletRequest request){
        Role role = roleService.create(roleDTO);
        URI uri = URI.create(request.getRequestURI() + "/" + role.getId());
        return ResponseEntity
                .created(uri)
                .body(role);
    }
}
