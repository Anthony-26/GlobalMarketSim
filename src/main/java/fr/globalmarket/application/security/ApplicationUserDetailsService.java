package fr.globalmarket.application.security;

import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import fr.globalmarket.adapter.outbound.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return new User(
                userEntity.getEmail(),
                userEntity.getPasswordHash(),
                userEntity.isEnabled(),
                true,
                true,
                true,
                getAuthorities(userEntity)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
