package db.footballdb.football_d_b_mongo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LeagueDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String leagueName;

    private Long countryLeague;

    private String ligHangiUlkede;

}
