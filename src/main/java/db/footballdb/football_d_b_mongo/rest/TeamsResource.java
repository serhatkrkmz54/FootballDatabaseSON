package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.service.TeamsService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/teamss", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsResource {

    private final TeamsService teamsService;

    public TeamsResource(final TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @GetMapping
    public ResponseEntity<List<TeamsDTO>> getAllTeamss() {
        return ResponseEntity.ok(teamsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamsDTO> getTeams(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(teamsService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createTeams(@RequestBody @Valid final TeamsDTO teamsDTO) throws IOException {
        final Long createdId = teamsService.create(teamsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTeams(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TeamsDTO teamsDTO) {
        teamsService.update(id, teamsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeams(@PathVariable(name = "id") final Long id) {
        teamsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
