package ch.octo.blog.transport.connectionlookup;

import ch.octo.blog.transport.dto.Connections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
public class ConnectionsController {

    private final ConnectionsService service;

    @Autowired
    public ConnectionsController(ConnectionsService service) {
        this.service = service;
    }

    @GetMapping
    public Connections getConnections(@RequestParam("from") String from, @RequestParam("to") String to) {
        return service.retrieveConnections(from, to);
    }

//    @GetMapping
//    public Connections getConnections(@RequestParam("from") String from) {
//        return service.retrieveConnections(from, "No where");
//    }

}
