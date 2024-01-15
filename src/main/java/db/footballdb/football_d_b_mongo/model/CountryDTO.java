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
public class CountryDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @JsonProperty("cName")
    private String cName;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("cValue")
    private BigDecimal cValue;

    @NotNull
    @JsonProperty("cPoint")
    private Integer cPoint;

    private List<Long> toCompetitions;

}
