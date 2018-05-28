package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.journeybooking.dto.JourneyDTO;
import ch.octo.blog.transport.journeybooking.dto.Journeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static ch.octo.blog.transport.journeybooking.dto.JourneyDTO.fromEntity;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/journeys")
@Transactional
public class JourneyController {

    private final JourneyService journeyService;

    @Autowired
    public JourneyController(JourneyService service) {
        this.journeyService = service;
    }

    /**
     * not really REST :)
     */
    @GetMapping("/search")
    public Connections searchConnections(@RequestParam("from") String from, @RequestParam("to") String to) {
        return journeyService.searchConnections(from, to);
    }

    @GetMapping
    public Journeys getAllJourneys() {
        return new Journeys(journeyService.getAllJourneys()
                .stream()
                .map(JourneyDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public JourneyDTO getJourney(@PathVariable("id") Long id) {
        return fromEntity(journeyService.getJourney(id));
    }

    @PostMapping(consumes = {APPLICATION_JSON_UTF8_VALUE},
            produces = {APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public JourneyDTO bookJourney(@Valid @RequestBody Connection connection) {
        return fromEntity(journeyService.bookJourney(connection));
    }

    @PutMapping(value = "/{id}",
            consumes = {APPLICATION_JSON_UTF8_VALUE},
            produces = {APPLICATION_JSON_UTF8_VALUE})
    public JourneyDTO updateJourney(@PathVariable("id") Long id, @Valid @RequestBody Connection connection) {
        return fromEntity(journeyService.updateJourney(id, connection));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJourney(@PathVariable("id") Long id) {
        journeyService.deleteJourney(id);
    }


    @SuppressWarnings("unused")
    @ExceptionHandler(JourneyNotFoundException.class)
    private ResponseEntity<String> handleNotFoundException(JourneyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
