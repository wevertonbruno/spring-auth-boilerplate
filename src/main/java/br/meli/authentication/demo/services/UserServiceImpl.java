package br.meli.authentication.demo.services;

import br.meli.authentication.demo.entities.User;
import br.meli.authentication.demo.entities.Role;
import br.meli.authentication.demo.repositories.RoleRepository;
import br.meli.authentication.demo.repositories.UserRepository;
import br.meli.authentication.demo.services.dto.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user from repository by throwing a exception if user is not found.
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        //Create a list of granted authorities from the list of roles
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        // Returns the user from userdetails class
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }

    @Transactional
    @Override
    public User create(UserDTO userDTO) {
        User user = convertUserFrom(userDTO);
        //Encoding password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void addRole(String username, Long roleID) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Role role = roleRepository.findById(roleID).orElseThrow(() -> new IllegalStateException("Role not found"));
        user.getRoles().add(role);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private User convertUserFrom(UserDTO userDTO){
        return new User(null,
                userDTO.getName(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                new ArrayList<>()
        );
    }
}
