package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.LeagueDTO;
import db.footballdb.football_d_b_mongo.service.LeagueService;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/api/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
public class LeagueResource {

    private final LeagueService leagueService;

    public LeagueResource(final LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public ResponseEntity<List<LeagueDTO>> getAllLeagues() {
        return ResponseEntity.ok(leagueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueDTO> getLeague(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(leagueService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createLeague(@RequestBody @Valid final LeagueDTO leagueDTO) {
        final Long createdId = leagueService.create(leagueDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLeague(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LeagueDTO leagueDTO) {
        leagueService.update(id, leagueDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable(name = "id") final Long id) {
        leagueService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
