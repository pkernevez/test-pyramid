package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connections;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contract Testing with Spring Cloud Contract.<br />
 * See also {@link ConnectionLookupClientCompTest} which is very similar except we don't manage Wiremock here, as it is managed by Spring and @{@link AutoConfigureStubRunner}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "feign.hystrix.enabled=false"
})
@AutoConfigureStubRunner(ids = {"ch.octo.blog:connection-lookup:+:stubs:8090"}, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ConnectionLookupClientContractTest {

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
        assertNotNull(connections);
        assertEquals(3, connections.size());
    }

    @TestConfiguration
    public static class ServerConfiguration {
        @Bean
        public ServerList<Server> staticServerList() {
            return new StaticServerList<>(new Server("localhost", 8090));
        }
    }
}
