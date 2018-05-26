package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connection;
import ch.octo.blog.transport.dto.Connections;
import ch.octo.blog.transport.journeybooking.model.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.octo.blog.transport.journeybooking.JourneyNotFoundException.MSG_JOURNEY_NOT_FOUND;

@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;

    private final ConnectionLookupClient connectionLookupClient;

    @Autowired
    public JourneyService(JourneyRepository repository, ConnectionLookupClient client){
        this.journeyRepository = repository;
        this.connectionLookupClient = client;
    }

    public Connections searchConnections(String from, String to) {
        Connections connections = connectionLookupClient.getConnections(from, to);
        return connections == null ? new Connections() : connections;
    }

    public Journey bookJourney(final Connection connection) {
        Journey journey = new Journey();
        journey.setFromConnection(connection);

        return journeyRepository.save(journey);
    }

    public Journey deleteJourney(final Long id) {
        return journeyRepository.findById(id).map(journey -> {
            journeyRepository.deleteById(id);
            return journey;
        }).orElseThrow(() -> new JourneyNotFoundException(String.format(MSG_JOURNEY_NOT_FOUND, id)));
    }

    public Journey getJourney(final Long id) {
        return journeyRepository.findById(id).orElseThrow(() -> new JourneyNotFoundException(String.format(MSG_JOURNEY_NOT_FOUND, id)));
    }

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    public Journey updateJourney(final Long id, final Connection connection) {
        return journeyRepository.findById(id).map(journey -> {
            journey.setFromConnection(connection);
            return journeyRepository.save(journey);
        }).orElseThrow(() -> new JourneyNotFoundException(String.format(MSG_JOURNEY_NOT_FOUND, id)));
    }

}
