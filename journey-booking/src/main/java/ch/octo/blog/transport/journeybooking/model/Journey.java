package ch.octo.blog.transport.journeybooking.model;

import ch.octo.blog.transport.dto.Connection;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.*;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Journey {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "station_from")
    private String from;

    @Column(name = "station_to")
    private String to;

    @Temporal(TemporalType.TIMESTAMP)
    private Date departure;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrival;

    public static Journey fromConnection(Connection connection) {
        if (connection == null || connection.getFrom() == null || connection.getTo() == null) {
            throw new IllegalArgumentException("Invalid connection");
        }
        Journey journey = new Journey();
        journey.setFrom(connection.getFrom().getStation().getName());
        journey.setTo(connection.getTo().getStation().getName());
        journey.setDeparture(new Date(SECONDS.toMillis(connection.getFrom().getDeparture())));
        journey.setArrival(new Date(SECONDS.toMillis(connection.getTo().getArrival())));
        return journey;
    }

    public Journey withId(long id) {
        this.setId(id);
        return this;
    }
}
