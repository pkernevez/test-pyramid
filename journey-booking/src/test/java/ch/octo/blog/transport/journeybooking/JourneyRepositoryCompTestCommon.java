package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.journeybooking.model.Journey;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Common source code (containing exact same tests) for {@link JourneyRepositoryH2CompTest} and {@link JourneyRepositoryPostgresCompTest}.<br/>
 * {@link Transactional} annotation is needed for the setup (insert in DB with entityManager). <br/>
 * <i>We also could have used {@link org.springframework.test.context.jdbc.Sql} annotation to setup data in DB.</i>
 */
@Transactional
public abstract class JourneyRepositoryCompTestCommon {
    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";
    private static final long DEPARTURE = 1524753519000L;
    private static final long ARRIVAL = 1524753619000L;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JourneyRepository repository;

    @Before
    public void setup() {
        Journey journey = new Journey();
        journey.setFrom(LAUSANNE);
        journey.setTo(GENEVE);
        journey.setDeparture(new Date(DEPARTURE));
        journey.setArrival(new Date(ARRIVAL));
        entityManager.persist(journey);
        entityManager.flush();
    }

    @Test
    public void findByFromAndTo_ShouldReturnOneElement() {
        // given

        // when
        List<Journey> journeys = repository.findByFromAndTo(LAUSANNE, GENEVE);

        // then
        assertThat(journeys).hasSize(1);
        Journey journeyInDB = journeys.get(0);
        assertJourneyInDB(journeyInDB);
    }

    @Test
    public void save_ShouldAddJourneyInDB() {
        // given
        Journey journey = new Journey();
        journey.setTo(LAUSANNE);
        journey.setFrom(GENEVE);
        journey.setDeparture(new Date(DEPARTURE));
        journey.setArrival(new Date(ARRIVAL));

        // when
        Journey journey1 = repository.save(journey);

        // then
        assertThat(journey1.getId()).isNotNull();
        assertThat(countRowsInTable()).isEqualTo(2);
    }

    private void assertJourneyInDB(Journey journey) {
        assertThat(journey.getFrom()).isEqualTo(LAUSANNE);
        assertThat(journey.getTo()).isEqualTo(GENEVE);
        assertThat(journey.getDeparture().getTime()).isEqualTo(DEPARTURE);
        assertThat(journey.getArrival().getTime()).isEqualTo(ARRIVAL);
    }

    private int countRowsInTable() {
        return DataAccessUtils.intResult(entityManager.createNativeQuery("SELECT count(*) FROM Journey").getResultList());
    }
}
