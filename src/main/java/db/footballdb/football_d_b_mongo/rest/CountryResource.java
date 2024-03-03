package db.footballdb.football_d_b_mongo.rest;

import db.footballdb.football_d_b_mongo.model.CountryDTO;
import db.footballdb.football_d_b_mongo.service.CountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class CountryResource {

    private final CountryService countryService;

    public CountryResource(final CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountry(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(countryService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCountry(@RequestBody @Valid final CountryDTO countryDTO) {
        final Long createdId = countryService.create(countryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCountry(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CountryDTO countryDTO) {
        countryService.update(id, countryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable(name = "id") final Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
