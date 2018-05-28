package ch.octo.blog.transport.journeybooking.dto;

import lombok.Data;

import java.util.List;

@Data
public class Journeys {
    private final List<JourneyDTO> journeys;
}
