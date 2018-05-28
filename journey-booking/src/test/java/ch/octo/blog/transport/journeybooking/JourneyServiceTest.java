package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Checkpoint;
import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.dto.Location;
import ch.octo.blog.transport.journeybooking.model.Journey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ch.octo.blog.transport.journeybooking.JourneyNotFoundException.MSG_JOURNEY_NOT_FOUND;
import static ch.octo.blog.transport.journeybooking.model.Journey.fromConnection;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

/**
 * Unit Test for {@link JourneyService}. We only use JUnit and Mockito here.<br/>
 * We test the business code.
 */
public class JourneyServiceTest {

    private static final long DEPARTURE = 1524753519L;
    private static final long ARRIVAL = 15247535619L;

    @Mock
    private JourneyRepository journeyRepository;

    @Mock
    private ConnectionLookupClient connectionLookupClient;

    @InjectMocks
    private JourneyService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchJourneys_ShouldReturnConnections_WhenClientReturnsConnections() {
        // given
        Location locationA = new Location("a");
        Checkpoint from1 = Checkpoint.builder().station(locationA).departure(DEPARTURE).build();
        Location locationB = new Location("b");
        Checkpoint to1 = Checkpoint.builder().station(locationB).arrival(1524754519L).build();

        Connections connections = new Connections();
        Connection connection = new Connection(from1, to1);
        connections.add(connection);

        given(connectionLookupClient.getConnections("a", "b")).willReturn(connections);

        // when
        Connections connections1 = service.searchConnections("a", "b");

        // then
        assertThat(connections1.getConnections()).contains(connection);
    }

    @Test
    public void searchJourneys_ShouldReturnNothing_WhenClientReturnsNull() {
        // given
        given(connectionLookupClient.getConnections("a", "b")).willReturn(null);

        // when
        Connections connections = service.searchConnections("a", "b");

        // then
        assertThat(connections.getConnections()).isEmpty();
    }

    @Test
    public void searchJourneys_ShouldReturnNothing_WhenClientReturnsEmptyConnections() {
        // given
        given(connectionLookupClient.getConnections("a", "b")).willReturn(new Connections());

        // when
        Connections connections = service.searchConnections("a", "b");

        // then
        assertThat(connections.getConnections()).isEmpty();
    }

    @Test
    public void bookJourney_ShouldCallRepositorySave() {
        // given
        Checkpoint from = Checkpoint.builder().station(new Location("from")).departure(DEPARTURE).build();
        Checkpoint to = Checkpoint.builder().station(new Location("to")).arrival(ARRIVAL).build();

        // when
        service.bookJourney(new Connection(from, to));

        // then
        ArgumentCaptor<Journey> captor = ArgumentCaptor.forClass(Journey.class);
        verify(journeyRepository).save(captor.capture());
        Journey journey = captor.getValue();
        assertThat(journey.getFrom()).isEqualTo("from");
        assertThat(journey.getTo()).isEqualTo("to");
        assertThat(journey.getDeparture().getTime()).isEqualTo(SECONDS.toMillis(DEPARTURE));
        assertThat(journey.getArrival().getTime()).isEqualTo(SECONDS.toMillis(ARRIVAL));
    }

    @Test
    public void deleteJourney_ShouldCallRepositoryDelete_WhenJourneyExists() {
        // given
        given(journeyRepository.findById(42L)).willReturn(Optional.of(new Journey()));

        // when
        Journey journeyDeleted = service.deleteJourney(42L);

        // then
        verify(journeyRepository).deleteById(42L);
        assertThat(journeyDeleted).isNotNull();
    }

    @Test
    public void deleteJourney_ShouldThrowException_WhenJourneyNotExists() {
        // given
        given(journeyRepository.findById(42L)).willReturn(Optional.empty());

        // when + then
        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> service.deleteJourney(42L))
                .withMessage(String.format(MSG_JOURNEY_NOT_FOUND, 42L));
    }

    @Test
    public void deleteJourney_ShouldThrowException_WhenJourneyNull() {
        // given
        given(journeyRepository.findById(42L)).willReturn(Optional.empty());

        // when + then
        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> service.deleteJourney(42L))
                .withMessage(String.format(MSG_JOURNEY_NOT_FOUND, 42L));
    }

    @Test
    public void getJourney_ShouldReturnJourney_WhenJourneyExists() {
        // given
        Journey journey1 = new Journey();
        given(journeyRepository.findById(42L)).willReturn(Optional.of(journey1));

        // when
        Journey journey = service.getJourney(42L);

        // then
        assertEquals(journey1, journey);
    }

    @Test
    public void getJourney_ShouldThrowException_WhenJourneyNotExists() {
        // given
        given(journeyRepository.findById(42L)).willReturn(Optional.empty());

        // when + then
        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> service.getJourney(42L))
                .withMessage(String.format(MSG_JOURNEY_NOT_FOUND, 42L));
    }

    @Test
    public void updateJourney_ShouldUpdateJourney_WhenJourneyExists() {
        // given
        Journey journey = new Journey();
        journey.setId(18L);
        journey.setFrom("from");
        journey.setTo("to");
        journey.setDeparture(new Date(DEPARTURE));
        journey.setArrival(new Date(ARRIVAL));
        given(journeyRepository.findById(18L)).willReturn(Optional.of(journey));

        Checkpoint from1 = Checkpoint.builder().station(new Location("from")).departure(DEPARTURE).build();
        Checkpoint to1 = Checkpoint.builder().station(new Location("to")).arrival(ARRIVAL).build();
        Connection connection1 = new Connection(from1, to1);
        Journey journey1 = fromConnection(connection1).withId(18L);
        given(journeyRepository.save(journey1)).willReturn(journey1);

        // when
        Journey journey2 = service.updateJourney(18L, connection1);

        // then
        assertThat(journey2).isEqualToComparingFieldByField(journey1);
    }

    @Test
    public void updateJourney_ShouldThrowException_WhenJourneyNotExists() {
        // given
        given(journeyRepository.findById(42L)).willReturn(Optional.empty());

        // when
        Checkpoint from1 = Checkpoint.builder().station(new Location("from")).departure(DEPARTURE).build();
        Checkpoint to1 = Checkpoint.builder().station(new Location("to")).arrival(ARRIVAL).build();
        Connection connection1 = new Connection(from1, to1);

        // then
        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> service.updateJourney(18L, connection1))
                .withMessage(String.format(MSG_JOURNEY_NOT_FOUND, 18L));
    }

    @Test
    public void findAll_ShouldCallRepositoryFindAll() {
        // given
        given(journeyRepository.findAll()).willReturn(Collections.singletonList(new Journey()));

        // when
        List<Journey> allJourneys = service.getAllJourneys();

        // then
        then(journeyRepository).should().findAll();
        assertThat(allJourneys).hasSize(1);
    }
}
