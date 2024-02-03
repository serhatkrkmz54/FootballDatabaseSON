package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.service.PlayersService;
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
@RequestMapping(value = "/api/playerss", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayersResource {

    private final PlayersService playersService;

    public PlayersResource(final PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping
    public ResponseEntity<List<PlayersDTO>> getAllPlayerss() {
        return ResponseEntity.ok(playersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayersDTO> getPlayers(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(playersService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createPlayers(@RequestBody @Valid final PlayersDTO playersDTO) throws IOException {
        final Long createdId = playersService.create(playersDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePlayers(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PlayersDTO playersDTO) {
        playersService.update(id, playersDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayers(@PathVariable(name = "id") final Long id) {
        playersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
