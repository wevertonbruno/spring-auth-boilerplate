package br.meli.authentication.demo;

import br.meli.authentication.demo.entities.Role;
import br.meli.authentication.demo.services.RoleService;
import br.meli.authentication.demo.services.UserService;
import br.meli.authentication.demo.services.dto.RoleDTO;
import br.meli.authentication.demo.services.dto.UserDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService){
		return args -> {
			Role userRole = roleService.create(new RoleDTO("ROLE_USER"));
			Role userManager = roleService.create(new RoleDTO("ROLE_MANAGER"));
			Role userAdmin = roleService.create(new RoleDTO("ROLE_ADMIN"));

			userService.create(new UserDTO("Weverton User", "wevuser", "123456"));
			userService.create(new UserDTO("Weverton Admin", "wevadmin", "123456"));

			userService.addRole("wevuser", userRole.getId());
			userService.addRole("wevuser", userManager.getId());
			userService.addRole("wevadmin", userAdmin.getId());
		};
	}
}
