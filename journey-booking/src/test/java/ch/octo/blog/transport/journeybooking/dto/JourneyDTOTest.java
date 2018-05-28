package ch.octo.blog.transport.journeybooking.dto;

import ch.octo.blog.transport.journeybooking.model.Journey;
import org.junit.Test;

import java.util.Date;

import static ch.octo.blog.transport.journeybooking.dto.JourneyDTO.fromEntity;
import static org.assertj.core.api.Assertions.assertThat;

public class JourneyDTOTest {

    private static final long DEPARTURE = 1524753519000L;
    private static final long ARRIVAL = 15247535619000L;
    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";

    @Test
    public void fromEntity_ShouldReturnSameJourneyAsDTO() {
        // given
        Journey journey = new Journey();
        journey.setId(42L);
        journey.setArrival(new Date(ARRIVAL));
        journey.setDeparture(new Date(DEPARTURE));
        journey.setTo(GENEVE);
        journey.setFrom(LAUSANNE);

        // when
        JourneyDTO dto = fromEntity(journey);

        // then
        assertThat(dto).isEqualToComparingFieldByField(journey);
    }
}
