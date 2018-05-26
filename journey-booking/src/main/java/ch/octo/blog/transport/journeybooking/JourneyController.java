package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.journeybooking.dto.Journeys;
import ch.octo.blog.transport.journeybooking.model.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return new Journeys(journeyService.getAllJourneys());
    }

    @GetMapping("/{id}")
    public Journey getJourney(@PathVariable("id") Long id) {
        return journeyService.getJourney(id);
    }

    @PostMapping(consumes = {APPLICATION_JSON_UTF8_VALUE},
            produces = {APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Journey bookJourney(@Valid @RequestBody Connection connection) {
        return journeyService.bookJourney(connection);
    }

    @PutMapping(value = "/{id}",
            consumes = {APPLICATION_JSON_UTF8_VALUE},
            produces = {APPLICATION_JSON_UTF8_VALUE})
    public Journey updateJourney(@PathVariable("id") Long id, @Valid @RequestBody Connection connection) {
        return journeyService.updateJourney(id, connection);
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
