package db.footballdb.football_d_b_mongo.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Document
@Getter
@Setter
public class Players {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String pName;

    @NotNull
    @Size(max = 255)
    private String pSurname;

    private String resimYolu;

    @NotNull
    @Size(max = 255)
    private String pCountry;

    @NotNull
    private Integer pWeight;

    @NotNull
    private Integer pHeight;

    @NotNull
    @Size(max = 255)
    private String pPosition;

    @NotNull
    @Size(max = 255)
    private String pPlayerAge;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal pValue;

    @Size(max = 255)
    private String pFoot;

    @DocumentReference(lazy = true)
    private Teams toTeams;

    @DocumentReference(lazy = true)
    private Country toCountryPlayers;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
