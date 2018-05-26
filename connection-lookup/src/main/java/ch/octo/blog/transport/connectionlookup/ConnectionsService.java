package ch.octo.blog.transport.connectionlookup;

import ch.octo.blog.transport.dto.Connections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionsService {

    private final TransportClient client;

    @Autowired
    public ConnectionsService(TransportClient client) {
        this.client = client;
    }

    public Connections retrieveConnections(String from, String to) {
        return client.getConnections(from, to);
    }
}
