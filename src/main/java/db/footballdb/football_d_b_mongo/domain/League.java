package db.footballdb.football_d_b_mongo.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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


@Document
@Getter
@Setter
public class League {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String leagueName;

    @DocumentReference(lazy = true, lookup = "{ 'leaguesss' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Teams> teamsss;

    @DocumentReference(lazy = true)
    private Country countryLeague;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
