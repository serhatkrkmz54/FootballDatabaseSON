package db.footballdb.football_d_b_mongo.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Document
@Getter
@Setter
public class TransferGecmisi {

    private Long fromTeam;
    private Long toTeam;
    private BigDecimal transferFee;
    private String transferDate;
    private String transferType;

}
