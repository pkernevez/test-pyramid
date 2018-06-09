package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.journeybooking.model.Journey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Integration Test of the {@link JourneyRepository}. Will fail if database is not started.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/setup_data_integration_test.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_data_integration_test.sql", executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class JourneyRepositoryIT {

    @Autowired
    private JourneyRepository repository;

    @Test
    public void findAll_shouldFindSomething() {
        // given

        // when
        List<Journey> journeys = repository.findAll();

        // then
        assertThat(journeys).isNotNull();
        assertThat(journeys.size()).isGreaterThanOrEqualTo(1);
    }
}
