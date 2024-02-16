package db.footballdb.football_d_b_mongo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "authorities")
public class Authority {
    @Id
    private Long id ;

    @NonNull
    private String authority;
}
