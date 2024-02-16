package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.mapper.AuthorityMapper;
import db.footballdb.football_d_b_mongo.model.AuthorityDTO;
import db.footballdb.football_d_b_mongo.repos.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityDTO getAuthorityById(Long id) {
        return authorityMapper.authorityEntityToDto(authorityRepository.findById(id));
    }

    public List<AuthorityDTO> getAllAuthorities() {
        return authorityMapper.authorityListEntityToDto(authorityRepository.findAll());
    }

}
