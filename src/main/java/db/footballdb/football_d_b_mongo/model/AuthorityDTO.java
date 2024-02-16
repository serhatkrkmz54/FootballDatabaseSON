package db.footballdb.football_d_b_mongo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record AuthorityDTO(Long id,
                           String authority) {



}
