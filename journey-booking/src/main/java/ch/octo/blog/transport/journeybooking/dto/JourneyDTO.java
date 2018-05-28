package ch.octo.blog.transport.journeybooking.dto;

import ch.octo.blog.transport.journeybooking.model.Journey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@JsonInclude(NON_NULL)
public class JourneyDTO {

    private Long id;
    private String from;
    private String to;
    private Date departure;
    private Date arrival;

    public static JourneyDTO fromEntity(Journey entity) {
        JourneyDTO journey = new JourneyDTO();
        journey.setId(entity.getId());
        journey.setArrival(entity.getArrival());
        journey.setDeparture(entity.getDeparture());
        journey.setFrom(entity.getFrom());
        journey.setTo(entity.getTo());
        return journey;
    }
}
