package db.footballdb.football_d_b_mongo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamsDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @JsonProperty("tName")
    private String tName;

    @NotNull
    @Size(max = 255)
    @JsonProperty("tCountry")
    private String tCountry;

    @NotNull
    @Size(max = 255)
    @JsonProperty("tLeague")
    private String tLeague;

    @NotNull
    @JsonProperty("tPoint")
    private Integer tPoint;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("tValue")
    private BigDecimal tValue;

    private Long toCountry;

    private String takimHangiUlkede;

    private List<Long> leaguesss;

    private List<String> ulkedekiLigler;

    private List<Long> toTeamstoCompetitions;

    private List<String> takiminKatildigiMusabakalar;

}
