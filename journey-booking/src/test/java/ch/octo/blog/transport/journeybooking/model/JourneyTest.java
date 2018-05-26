package ch.octo.blog.transport.journeybooking.model;

import ch.octo.blog.transport.dto.Checkpoint;
import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Location;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JourneyTest {

    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";
    private static final long DEPARTURE = 1524753519L;
    private static final long ARRIVAL = 15247535619L;

    @Test
    public void setFromConnection_ShouldSetAttributes_WhenConnectionIsValid() {
        // given
        Connection connection = setupValidConnection();
        Journey journey = new Journey();

        // when
        journey.setFromConnection(connection);

        // then
        assertThat(journey.getId()).isNull();
        assertValidJourney(journey);
    }

    @Test
    public void setFromConnection_ShouldSetAttributesAndKeepId_WhenIdSet() {
        // given
        Connection connection = setupValidConnection();
        Journey journey = new Journey();
        long id = 2012L;
        journey.setId(id);

        // when
        journey.setFromConnection(connection);

        assertThat(journey.getId()).isEqualTo(id);
        assertValidJourney(journey);
    }

    @Test
    public void setFromConnection_ShouldThrowException_WhenConnectionIsNull() {
        // given
        Journey journey = new Journey();

        // when + then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> journey.setFromConnection(null));
    }

    @Test
    public void setFromConnection_ShouldThrowException_WhenFromIsNull() {
        // given
        Journey journey = new Journey();
        Connection connection = new Connection();
        connection.setTo(Checkpoint.builder().build());

        // when + then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> journey.setFromConnection(connection));
    }

    @Test
    public void setFromConnection_ShouldThrowException_WhenToIsNull() {
        // given
        Journey journey = new Journey();
        Connection connection = new Connection();
        connection.setFrom(Checkpoint.builder().build());

        // when + then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> journey.setFromConnection(connection));
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

