package db.footballdb.football_d_b_mongo.rest;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/home", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class HomeResource {
    private final MongoTemplate mongoTemplate;

    public HomeResource(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping()
    public long[] index() {
        //Günlük girilen veri miktarını saydıran kod
        OffsetDateTime today = OffsetDateTime.now();
        MongoDatabase database = mongoTemplate.getDb(); // mongoTemplate, MongoTemplate türünde bir nesne olmalıdır.
        Set<String> collectionNames = database.listCollectionNames().into(new HashSet<>());
        long totalDocumentsCreatedToday = 0;
        for (String collectionName : collectionNames) {
            Query todayQuery = new Query(Criteria.where("dateCreated")
                    .gte(today.truncatedTo(ChronoUnit.DAYS))
                    .lt(today.plusDays(1).truncatedTo(ChronoUnit.DAYS)));

            long documentsCreatedToday = mongoTemplate.count(todayQuery, collectionName);
            totalDocumentsCreatedToday += documentsCreatedToday;
        }

        // MongoDB'deki tüm koleksiyon adlarını al
        Iterable<String> tumKoleksiyonlar = mongoTemplate.getCollectionNames();

        // primarySequence koleksiyonunu hariç tut - otomatik son id tutma koleksiyonu
        Iterable<String> koleksiyonFiltrele = StreamSupport.stream(tumKoleksiyonlar.spliterator(), false)
                .filter(collectionName -> !collectionName.equals("primarySequence"))
                .collect(Collectors.toList());

        // Her koleksiyon için belge sayısını hesapla
        Map<String, Long> collectionDocumentCounts = StreamSupport.stream(koleksiyonFiltrele.spliterator(), false)
                .collect(Collectors.toMap(
                        collectionName -> collectionName,
                        collectionName -> mongoTemplate.count(new Query(), collectionName)
                ));
        // Toplam koleksiyon sayısını hesapla
        long totalDocumentCount = collectionDocumentCounts.values().stream().mapToLong(Long::longValue).sum();

        return new long[]{totalDocumentCount, totalDocumentsCreatedToday};
    }
}