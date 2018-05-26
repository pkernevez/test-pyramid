package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Checkpoint;
import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.dto.Location;
import ch.octo.blog.transport.journeybooking.dto.Journeys;
import ch.octo.blog.transport.journeybooking.model.Journey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit Test of {@link JourneyController}. We only use JUnit and Mockito here.<br/>
 * We test the 'business' code.
 */
public class JourneyControllerTest {
    @Mock
    private JourneyService service;

    @InjectMocks
    private JourneyController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchConnections_shouldReturnJourneys_WhenServiceReturnJourneys() {
        // given
        String from = "from";
        String to = "to";
        Connections connections = new Connections();
        connections.add(new Connection());
        connections.add(new Connection());
        given(service.searchConnections(from, to)).willReturn(connections);

        // when
        controller.searchConnections(from, to);

        // then
        verify(service).searchConnections(from, to);
        assertThat(connections.getConnections()).hasSize(2);
    }

    @Test
    public void searchConnections_shouldReturnEmptyJourneys_WhenServiceReturnNoJourneys() {
        // given
        String from = "from";
        String to = "to";
        given(service.searchConnections(from, to)).willReturn(new Connections());

        // when
        Connections connections = controller.searchConnections(from, to);

        // then
        verify(service).searchConnections(from, to);
        assertThat(connections.getConnections()).isEmpty();
    }

    @Test
    public void getAllJourneys_shouldReturnJourneys_WhenServiceReturnJourneys() {
        // given
        given(service.getAllJourneys()).willReturn(Arrays.asList(new Journey(), new Journey()));

        // when
        Journeys journeys = controller.getAllJourneys();

        // then
        verify(service).getAllJourneys();
        assertThat(journeys.getJourneys()).hasSize(2);
    }

    @Test
    public void getAllJourneys_shouldReturnEmptyJourneys_WhenServiceReturnNoJourneys() {
        // given
        given(service.getAllJourneys()).willReturn(Collections.emptyList());

        // when
        Journeys journeys = controller.getAllJourneys();

        // then
        verify(service).getAllJourneys();
        assertThat(journeys.getJourneys()).isEmpty();
    }

    @Test
    public void getJourney_shouldReturnJourney_WhenServiceReturnJourney() {
        // given
        long id = 42L;
        Journey journey = new Journey();
        journey.setId(id);
        journey.setFrom("from");
        journey.setTo("to");
        given(service.getJourney(id)).willReturn(journey);

        // when
        Journey journey1 = controller.getJourney(id);

        // then
        verify(service).getJourney(id);
        assertThat(journey1).isEqualTo(journey);
    }

    @Test
    public void bookJourney_shouldReturnJourney_WhenPassingValidConnection() {
        // given
        Checkpoint from = Checkpoint.builder().station(new Location("from")).departure(1524753519L).build();
        Checkpoint to = Checkpoint.builder().station(new Location("to")).arrival(15247535619L).build();
        Connection connection = new Connection(from, to);

        Journey journey = new Journey();
        journey.setId(42L);
        journey.setFrom(from.getStation().getName());
        journey.setTo(to.getStation().getName());
        journey.setDeparture(new Date(1524753519000L));
        journey.setArrival(new Date(15247535619000L));

        given(service.bookJourney(connection)).willReturn(journey);

        // when
        Journey journey1 = controller.bookJourney(connection);

        // then
        verify(service).bookJourney(connection);
        assertThat(journey1).isEqualTo(journey);
    }

    @Test
    public void updateJourney_shouldReturnUpdatedJourney_WhenPassingValidConnection() {
        // given
        long id = 42L;
        Checkpoint from = Checkpoint.builder().station(new Location("from")).departure(1524753519L).build();
        Checkpoint to = Checkpoint.builder().station(new Location("to")).arrival(1524753619L).build();
        Connection connection = new Connection(from, to);

        Journey journey = new Journey();
        journey.setId(42L);
        journey.setFrom(from.getStation().getName());
        journey.setTo(to.getStation().getName());
        journey.setDeparture(new Date(1524753519000L));
        journey.setArrival(new Date(1524753619000L));

        given(service.updateJourney(id, connection)).willReturn(journey);

        // when
        Journey journey1 = controller.updateJourney(id, connection);

        // then
        verify(service).updateJourney(id, connection);
        assertThat(journey1).isEqualTo(journey);
    }

    @Test
    public void deleteJourney_shouldCallServiceDelete() {
        // given
        long id = 42L;

        // when
        controller.deleteJourney(id);

        // then
        verify(service).deleteJourney(id);
    }

}
