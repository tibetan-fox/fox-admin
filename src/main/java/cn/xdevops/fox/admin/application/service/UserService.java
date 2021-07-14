package cn.xdevops.fox.admin.application.service;

import cn.xdevops.fox.admin.infrastructure.entity.Role;
import cn.xdevops.fox.admin.infrastructure.entity.User;
import cn.xdevops.fox.admin.infrastructure.repository.UserRepository;
import cn.xdevops.fox.admin.web.dto.RegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        User user = userRepository.findUserByLoginName(loginName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getLoginName(),
                user.getPassword(),
                mapRoleToAuthorities(user.getRoles()));
    }

    private List<? extends GrantedAuthority> mapRoleToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    public Optional<User> findUserByLoginName(String loginName) {
        return Optional.ofNullable(userRepository.findUserByLoginName(loginName));
    }

    public Optional<User> save(RegistrationDto registrationDto) {
        User user = new User();
        user.setLoginName(registrationDto.getLoginName());
        user.setUserName(registrationDto.getUserName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(Arrays.asList(new Role("User", "ROLE_USER")));
        return Optional.of(userRepository.save(user));
    }
}
