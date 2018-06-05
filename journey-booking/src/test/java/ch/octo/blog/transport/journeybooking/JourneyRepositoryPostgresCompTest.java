package ch.octo.blog.transport.journeybooking;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Component Test of the {@link JourneyRepository}. <br/>
 * This test will use a Postgresql database in Docker configured in application-testpg.yml started by TestContainer
 * @see JourneyRepositoryH2CompTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"test", "testpg"})
public class JourneyRepositoryPostgresCompTest extends JourneyRepositoryCompTestCommon {

}
