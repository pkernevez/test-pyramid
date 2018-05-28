package ch.octo.blog.transport.journeybooking.model;

import ch.octo.blog.transport.dto.Checkpoint;
import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Location;
import org.junit.Test;

import static ch.octo.blog.transport.journeybooking.model.Journey.fromConnection;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JourneyTest {

    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";
    private static final long DEPARTURE = 1524753519L;
    private static final long ARRIVAL = 15247535619L;

    @Test
    public void fromConnection_ShouldSetAttributes_WhenConnectionIsValid() {
        // given
        Connection connection = setupValidConnection();

        // when
        Journey journey = fromConnection(connection);

        // then
        assertThat(journey.getId()).isNull();
        assertValidJourney(journey);
    }

    @Test
    public void fromConnectionWithId_ShouldSetAttributesAndKeepId_WhenIdSet() {
        // given
        Connection connection = setupValidConnection();
        Journey journey = new Journey();
        long id = 20L;
        journey.setId(id);

        // when
        journey = fromConnection(connection).withId(id);

        assertThat(journey.getId()).isEqualTo(id);
        assertValidJourney(journey);
    }

    @Test
    public void fromConnection_ShouldThrowException_WhenConnectionIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fromConnection(null));
    }

    @Test
    public void fromConnection_ShouldThrowException_WhenFromIsNull() {
        // given
        Connection connection = new Connection();
        connection.setTo(Checkpoint.builder().build());

        // when + then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fromConnection(connection));
    }

    @Test
    public void fromConnection_ShouldThrowException_WhenToIsNull() {
        // given
        Connection connection = new Connection();
        connection.setFrom(Checkpoint.builder().build());

        // when + then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fromConnection(connection));
    }

    private Connection setupValidConnection() {
        Checkpoint from = new Checkpoint(new Location(LAUSANNE));
        from.setDeparture(DEPARTURE);

        Checkpoint to = new Checkpoint(new Location(GENEVE));
        to.setArrival(ARRIVAL);

        return new Connection(from, to);
    }

    private void assertValidJourney(Journey journey) {
        assertThat(journey.getFrom()).isEqualTo(LAUSANNE);
        assertThat(journey.getTo()).isEqualTo(GENEVE);
        assertThat(journey.getDeparture().getTime()).isEqualTo(SECONDS.toMillis(DEPARTURE));
        assertThat(journey.getArrival().getTime()).isEqualTo(SECONDS.toMillis(ARRIVAL));
    }
}

