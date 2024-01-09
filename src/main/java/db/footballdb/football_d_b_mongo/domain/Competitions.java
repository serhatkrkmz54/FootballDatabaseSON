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
public class Competitions {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String competitionName;

    @DocumentReference(lazy = true, lookup = "{ 'toCompetitions' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Country> toCountryCompetitions;

    @DocumentReference(lazy = true, lookup = "{ 'toTeamstoCompetitions' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Teams> toCompetitionstoTeams;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
