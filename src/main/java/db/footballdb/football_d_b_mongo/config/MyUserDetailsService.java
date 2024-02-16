package db.footballdb.football_d_b_mongo.config;

import db.footballdb.football_d_b_mongo.domain.User;
import db.footballdb.football_d_b_mongo.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if(!userByUsername.isPresent()) {
            log.error("Bu Kullanıcı bulunamadı:  {}",username);
            throw new UsernameNotFoundException("Invalid Credentials!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getAuthorities().stream().forEach(authority ->
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority)));
        return new MySecurityUser(user.getUsername(),user.getPassword(),true,true,true,true,grantedAuthorities,
                user.getFirstName(),user.getLastName(),user.getEmailAddress());

    }
}
