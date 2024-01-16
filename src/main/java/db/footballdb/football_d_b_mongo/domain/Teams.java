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
public class Teams {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String tName;

    @NotNull
    private Integer tPoint;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal tValue;

    @DocumentReference(lazy = true, lookup = "{ 'toTeams' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Players> toPlayers;

    @DocumentReference(lazy = true)
    private Country toCountry;

    @DocumentReference(lazy = true)
    private League leaguesss;

    @DocumentReference(lazy = true)
    private Set<Competitions> toTeamstoCompetitions;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
