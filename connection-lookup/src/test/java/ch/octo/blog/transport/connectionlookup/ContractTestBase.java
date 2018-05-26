package ch.octo.blog.transport.connectionlookup;

import ch.octo.blog.transport.dto.Connections;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Base class used for generated contract tests (see target/generated-test-sources)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureJsonTesters
public class ContractTestBase {

    @Autowired
    private ConnectionsController controller;

    @MockBean
    private ConnectionsService service;

    @Autowired
    JacksonTester<Connections> connectionsJacksonTester;

    @Before
    public void setup() throws Exception {
        given(service.retrieveConnections(anyString(), anyString()))
                .willReturn(connectionsJacksonTester.readObject(getClass().getResourceAsStream("/test_connections_response.json")));

        RestAssuredMockMvc.standaloneSetup(controller);
    }

    @Test
    public void emptyTest() {
        // to avoid initializationError
    }
}


