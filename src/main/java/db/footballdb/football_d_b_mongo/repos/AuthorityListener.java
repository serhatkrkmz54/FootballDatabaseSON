package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.User;
import db.footballdb.football_d_b_mongo.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class AuthorityListener extends AbstractMongoEventListener<User> {
    private final PrimarySequenceService primarySequenceService;

    public AuthorityListener(final PrimarySequenceService primarySequenceService){
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<User> event) {
        if(event.getSource().getId() == null ) {
            event.getSource().setId(primarySequenceService.getNextValue());
        }
    }
}
