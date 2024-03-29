package db.footballdb.football_d_b_mongo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import db.footballdb.football_d_b_mongo.domain.TransferGecmisi;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class PlayersDTO {

    private Long id;

    @JsonProperty("formaNo")
    private Integer formaNo;

    @NotNull
    @Size(max = 255)
    @JsonProperty("pName")
    private String pName;

    @NotNull
    @Size(max = 255)
    @JsonProperty("pSurname")
    private String pSurname;

    @NotNull
    @Size(max = 255)
    @JsonProperty("pCountry")
    private String pCountry;

    @NotNull
    @JsonProperty("pWeight")
    private Integer pWeight;

    @NotNull
    @JsonProperty("pHeight")
    private Integer pHeight;

    @NotNull
    @Size(max = 255)
    @JsonProperty("pPosition")
    private String pPosition;

    @NotNull
    @Size(max = 255)
    @JsonProperty("pPlayerAge")
    private String pPlayerAge;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("pValue")
    private BigDecimal pValue;

    @Size(max = 255)
    @JsonProperty("pFoot")
    private String pFoot;

    private MultipartFile pathFile;

    private String filePath;

    private Long toTeams;

    private Long toCountryPlayers;

    private String oyuncuHangiTakimda;

    private String oyuncuHangiUlkede;

    private ArrayList<TransferGecmisi> transferGecmisi;
}
