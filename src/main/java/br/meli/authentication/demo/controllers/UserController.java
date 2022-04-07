package br.meli.authentication.demo.controllers;

import br.meli.authentication.demo.entities.User;
import br.meli.authentication.demo.services.UserService;
import br.meli.authentication.demo.services.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("v1/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserDTO userDTO, HttpServletRequest request){
        User appUser = userService.create(userDTO);
        URI uri = URI.create(request.getRequestURI() + "/" + appUser.getId());
        return ResponseEntity
                .created(uri)
                .body(appUser);
    }

    @PutMapping("/{username}/role/{roleId}")
    public ResponseEntity<?> addRole(
            @PathVariable String username,
            @PathVariable Long roleId
    ){
        userService.addRole(username, roleId);
        return ResponseEntity.ok().build();
    }
}
