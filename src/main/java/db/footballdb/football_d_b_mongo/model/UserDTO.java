package db.footballdb.football_d_b_mongo.model;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDTO(Long id,
                      String username,
                      List<AuthorityDTO> authorities,
                      Boolean accountNonExpired,
                      Boolean accountNonLocked,
                      Boolean credentialsNonExpired,
                      Boolean enabled,
                      String firstName,
                      String lastName,
                      String emailAddress) {
}
