package db.footballdb.football_d_b_mongo.mapper;

import db.footballdb.football_d_b_mongo.domain.User;
import db.footballdb.football_d_b_mongo.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final AuthorityMapper authorityMapper;

    public UserDTO userEntityToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .authorities(authorityMapper.authorityListEntityToDto(user.getAuthorities()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmailAddress())
                .build();
    }
    public List<UserDTO> userEntityListToDto(List<User> users) {
        return users.stream()
                .map(user -> userEntityToDto(user))
                .toList();
    }

    public User userDtoToEntity(UserDTO user, String password) {
        return User.builder()
                .id(user.id())
                .username(user.username())
                .password(password)
                .authorities(authorityMapper.authorityListDtoToEntity(user.authorities()))
                .accountNonExpired(user.accountNonExpired())
                .accountNonLocked(user.accountNonLocked())
                .credentialsNonExpired(user.credentialsNonExpired())
                .enabled(user.enabled())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .emailAddress(user.emailAddress())
                .build();
    }
    public List<User> userListDtoToEntity(List<UserDTO> users, String password) {
        return users.stream()
                .map(user -> userDtoToEntity(user, password))
                .toList();
    }
 }
