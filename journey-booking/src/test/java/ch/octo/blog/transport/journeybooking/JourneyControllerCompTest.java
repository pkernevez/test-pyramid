package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Checkpoint;
import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.dto.Location;
import ch.octo.blog.transport.journeybooking.model.Journey;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Date;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Component Testing of the {@link JourneyController}. As we use {@link MockMvc}, this is not a real integration test.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = JourneyController.class)
@AutoConfigureJsonTesters
public class JourneyControllerCompTest {

    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";
    private static final long DEPARTURE = 1524753519000L;
    private static final long ARRIVAL = 1524753619000L;

    @Autowired
    private JacksonTester<Connection> connectionJacksonTester;

    @Autowired
    private JacksonTester<Journey> journeyJacksonTester;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JourneyService service;

    @Test
    public void searchConnections_shouldReturnOKAndJourneys_WhenServiceReturnJourneys() throws Exception {
        // given
        Connections connections = new Connections();
        connections.add(new Connection());
        connections.add(new Connection());
        given(service.searchConnections(LAUSANNE, GENEVE)).willReturn(connections);

        // when
        ResultActions resultActions = mockMvc.perform(get("/journeys/search?from=" + LAUSANNE + "&to=" + GENEVE));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("connections.*", hasSize(2)));
    }

    @Test
    public void getAllJourneys_shouldReturnOKAndJourneys_WhenServiceReturnJourneys() throws Exception {
        // given
        given(service.getAllJourneys()).willReturn(Arrays.asList(new Journey(), new Journey()));

        // when
        ResultActions resultActions = mockMvc.perform(get("/journeys/"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("journeys.*", hasSize(2)));
    }

    @Test
    public void getJourney_shouldReturnOKAndJourney_WhenServiceReturnJourney() throws Exception {
        // given
        long id = 42L;
        Journey journey = createJourney(id);
        given(service.getJourney(id)).willReturn(journey);

        // when
        ResultActions resultActions = mockMvc.perform(get("/journeys/" + id));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(journeyJacksonTester.write(journey).getJson()));
    }

    @Test
    public void getJourney_shouldReturnNotFound_WhenServiceThrowsException() throws Exception {
        // given
        long id = 42L;
        String notFound = "Not Found";
        given(service.getJourney(id)).willThrow(new JourneyNotFoundException(notFound));

        // when
        ResultActions resultActions = mockMvc.perform(get("/journeys/" + id));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().string(notFound));
    }

    @Test
    public void bookJourney_shouldReturnCreated_WhenPassingValidConnection() throws Exception {
        // given
        Checkpoint from = Checkpoint.builder().station(new Location(LAUSANNE)).departure(MILLISECONDS.toSeconds(DEPARTURE)).build();
        Checkpoint to = Checkpoint.builder().station(new Location(GENEVE)).arrival(MILLISECONDS.toSeconds(ARRIVAL)).build();
        Connection connection = new Connection(from, to);

        Journey journey = createJourney(42L);

        given(service.bookJourney(connection)).willReturn(journey);

        // when
        ResultActions resultActions = mockMvc.perform(post("/journeys/")
                .content(connectionJacksonTester.write(connection).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(journeyJacksonTester.write(journey).getJson()));
    }

    @Test
    public void bookJourney_shouldReturnBadRequest_WhenPassingInvalidConnection() throws Exception {
        // given
        Connection invalidConnection = new Connection();

        // when
        ResultActions resultActions = mockMvc.perform(post("/journeys/")
                .content(connectionJacksonTester.write(invalidConnection).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void updateJourney_shouldReturnOKAndUpdatedJourney_WhenPassingValidConnection() throws Exception {
        // given
        long id = 42L;
        Checkpoint from = Checkpoint.builder().station(new Location(LAUSANNE)).departure(MILLISECONDS.toSeconds(DEPARTURE)).build();
        Checkpoint to = Checkpoint.builder().station(new Location(GENEVE)).arrival(MILLISECONDS.toSeconds(ARRIVAL)).build();
        Connection connection = new Connection(from, to);

        Journey journey = createJourney(id);

        given(service.updateJourney(id, connection)).willReturn(journey);

        // when
        ResultActions resultActions = mockMvc.perform(put("/journeys/" + id)
                .content(connectionJacksonTester.write(connection).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(journeyJacksonTester.write(journey).getJson()));
    }

    @Test
    public void updateJourney_shouldReturnBadRequest_WhenPassingInvalidConnection() throws Exception {
        // given
        Connection invalidConnection = new Connection();

        // when
        ResultActions resultActions = mockMvc.perform(put("/journeys/42")
                .content(connectionJacksonTester.write(invalidConnection).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void updateJourney_shouldReturnNotFound_WhenServiceThrowsException() throws Exception {
        // given
        long id = 42L;
        Checkpoint from = Checkpoint.builder().station(new Location(LAUSANNE)).departure(MILLISECONDS.toSeconds(DEPARTURE)).build();
        Checkpoint to = Checkpoint.builder().station(new Location(GENEVE)).arrival(MILLISECONDS.toSeconds(ARRIVAL)).build();
        Connection connection = new Connection(from, to);
        String notFound = "Not Found";
        given(service.updateJourney(id, connection)).willThrow(new JourneyNotFoundException(notFound));

        // when
        ResultActions resultActions = mockMvc.perform(put("/journeys/" + id)
                .content(connectionJacksonTester.write(connection).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().string(notFound));
    }

    @Test
    public void deleteJourney_shouldReturnNoContent_WhenServiceDeleteJourney() throws Exception {
        // given
        long id = 42L;
        given(service.deleteJourney(id)).willReturn(any(Journey.class));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/journeys/" + id));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteJourney_shouldReturnNotFound_WhenServiceThrowsException() throws Exception {
        // given
        long id = 42L;
        String notFound = "Not Found";
        given(service.deleteJourney(id)).willThrow(new JourneyNotFoundException(notFound));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/journeys/" + id));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().string(notFound));
    }

    @NotNull
    private Journey createJourney(long id) {
        Journey journey = new Journey();
        journey.setId(id);
        journey.setFrom(LAUSANNE);
        journey.setTo(GENEVE);
        journey.setDeparture(new Date(DEPARTURE));
        journey.setArrival(new Date(ARRIVAL));
        return journey;
    }
}
