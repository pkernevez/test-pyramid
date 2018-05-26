package ch.octo.blog.transport.journeybooking.dto;

import ch.octo.blog.transport.journeybooking.model.Journey;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Journeys {
    private final List<Journey> journeys;
}
