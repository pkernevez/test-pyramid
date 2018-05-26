package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Test of the {@link ConnectionLookupClient}. Will fail if connection-lookup service is not deployed (we disable hystrix).
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "feign.hystrix.enabled=false"
})
public class ConnectionLookupClientIT {

    private static final String LAUSANNE = "Lausanne";
    private static final String ZURICH = "Zurich";

    @Autowired
    private ConnectionLookupClient client;

    @Test
    public void getConnections_shouldRetrieveConnections() {
        // given

        // when
        Connections connections = client.getConnections(LAUSANNE, ZURICH);

        // then
        assertThat(connections).isNotNull();
        assertThat(connections.getConnections()).isNotEmpty();
    }
}
