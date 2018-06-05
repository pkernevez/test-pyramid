package ch.octo.blog.transport.journeybooking;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Component Test of the {@link JourneyRepository}.<br/>
 * As we use {@link DataJpaTest} annotation, the tests will use an in-memory database (H2). Thus this is not a real integration test.
 * @see JourneyRepositoryPostgresCompTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class JourneyRepositoryH2CompTest extends JourneyRepositoryCompTestCommon {


}
