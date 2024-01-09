package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CompetitionsListener extends AbstractMongoEventListener<Competitions> {

    private final PrimarySequenceService primarySequenceService;

    public CompetitionsListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Competitions> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(primarySequenceService.getNextValue());
        }
    }

}
