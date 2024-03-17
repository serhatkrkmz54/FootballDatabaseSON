package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.service.PlayersService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/playerss", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class PlayersResource {

    private final PlayersService playersService;

    public PlayersResource(final PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping
    public ResponseEntity<List<PlayersDTO>> getAllPlayerss() {
        return ResponseEntity.ok(playersService.findAll());
    }

    @GetMapping("/oyuncuToplamDeger")
    public ResponseEntity<BigDecimal> getOyuncuDegeri() {
        return ResponseEntity.ok(playersService.getOyuncuDegeri());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayersDTO> getPlayers(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(playersService.get(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Long> createPlayers(@ModelAttribute @Valid final PlayersDTO playersDTO) throws IOException {
        final Long createdId = playersService.create(playersDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping(consumes = "multipart/form-data", value = "/{id}")
    public ResponseEntity<Long> updatePlayers(@PathVariable(name = "id") @Valid final Long id, @ModelAttribute @Valid final PlayersDTO playersDTO) {
        playersService.update(id, playersDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayers(@PathVariable(name = "id") final Long id) {
        playersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
