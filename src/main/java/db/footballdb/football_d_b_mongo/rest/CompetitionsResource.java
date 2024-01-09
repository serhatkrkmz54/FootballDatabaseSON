package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.CompetitionsDTO;
import db.footballdb.football_d_b_mongo.service.CompetitionsService;
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
@RequestMapping(value = "/api/competitionss", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompetitionsResource {

    private final CompetitionsService competitionsService;

    public CompetitionsResource(final CompetitionsService competitionsService) {
        this.competitionsService = competitionsService;
    }

    @GetMapping
    public ResponseEntity<List<CompetitionsDTO>> getAllCompetitionss() {
        return ResponseEntity.ok(competitionsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionsDTO> getCompetitions(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(competitionsService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCompetitions(
            @RequestBody @Valid final CompetitionsDTO competitionsDTO) {
        final Long createdId = competitionsService.create(competitionsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCompetitions(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CompetitionsDTO competitionsDTO) {
        competitionsService.update(id, competitionsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetitions(@PathVariable(name = "id") final Long id) {
        competitionsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
