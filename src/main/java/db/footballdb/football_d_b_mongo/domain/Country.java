package db.footballdb.football_d_b_mongo.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Document
@Getter
@Setter
public class Country {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String cName;

    @Digits(integer = 10, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    @NotNull
    private BigDecimal cValue;

    @NotNull
    private Integer cPoint;

    @DocumentReference(lazy = true, lookup = "{ 'toCountry' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Teams> toTeams;

    @DocumentReference(lazy = true, lookup = "{ 'toCountryPlayers' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Players> toPlayersCountry;

    @DocumentReference(lazy = true, lookup = "{ 'countryLeague' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<League> leagueCountry;

    @DocumentReference(lazy = true)
    private Set<Competitions> toCompetitions;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
