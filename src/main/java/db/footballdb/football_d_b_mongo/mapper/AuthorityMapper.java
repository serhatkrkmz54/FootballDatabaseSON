package db.footballdb.football_d_b_mongo.mapper;

import db.footballdb.football_d_b_mongo.domain.Authority;
import db.footballdb.football_d_b_mongo.model.AuthorityDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorityMapper {
    public AuthorityDTO authorityEntityToDto(Authority authority) {
        return AuthorityDTO.builder()
                .id(authority.getId())
                .authority(authority.getAuthority())
                .build();
    }

    public List<AuthorityDTO> authorityListEntityToDto(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> authorityEntityToDto(authority))
                .toList();
    }

    public Authority authorityDtoToEntity(AuthorityDTO authority)  {
        return Authority.builder()
                .id(authority.id())
                .authority(authority.authority())
                .build();
    }

    public List<Authority> authorityListDtoToEntity(List<AuthorityDTO> authorities) {
        return authorities.stream()
                .map(authority -> authorityDtoToEntity(authority))
                .toList();
    }
}
