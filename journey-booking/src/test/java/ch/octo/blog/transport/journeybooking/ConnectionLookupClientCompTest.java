package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Component Testing of the {@link ConnectionLookupClient}. <br />
 * Use Wiremock to be isolated from the real service, thus this is not a real integration test.<br />
 * See also {@link ConnectionLookupClientContractTest} which provides an equivalent test but with contract verification.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionLookupClientCompTest {

    private static final String LAUSANNE = "Lausanne";
    private static final String ZURICH = "Zurich";

    @Autowired
    private ConnectionLookupClient client;

    @Autowired
    private ObjectMapper mapper;

    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            wireMockConfig().dynamicPort());

    @Test
    public void getConnections_shouldRetrieveConnections() throws IOException {
        // given
        wiremock.stubFor(get(urlPathEqualTo("/connections"))
                .withQueryParam("from", equalTo(LAUSANNE))
                .withQueryParam("to", equalTo(ZURICH))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withBodyFile("clients/connections.json")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)));

        // when
        Connections connections = client.getConnections(LAUSANNE, ZURICH);

        // then
        Connections expectedConnections = mapper.readValue(getClass().getResourceAsStream("/__files/clients/connections.json"), Connections.class);
        assertThat(connections).isEqualToComparingFieldByFieldRecursively(expectedConnections);
    }

    @Test
    public void getConnections_shouldUseFallback_WhenSLookupServiceTimeout() {
        // given
        wiremock.stubFor(get(urlPathEqualTo("/connections"))
                .willReturn(aResponse().withFixedDelay(60000)));

        // when
        Connections connections = client.getConnections(LAUSANNE, ZURICH);

        // then
        assertNotNull(connections);
        assertThat(connections.getConnections()).isEmpty();
    }

    @TestConfiguration
    public static class ServerConfiguration {
        @Bean
        public ServerList<Server> staticServerList() {
            return new StaticServerList<>(new Server("localhost", wiremock.port()));
        }
    }
}
