package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.model.AuthorityDTO;
import db.footballdb.football_d_b_mongo.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping("/authorities")
    public String getauthorities(Model model) {
        List<AuthorityDTO> authorities = authorityService.getAll
    }
}
