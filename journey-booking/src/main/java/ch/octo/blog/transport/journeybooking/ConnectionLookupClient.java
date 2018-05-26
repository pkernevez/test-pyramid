package ch.octo.blog.transport.journeybooking;

import ch.octo.blog.transport.dto.Connections;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "connections", fallback = ConnectionLookupClient.ConnectionLookupFallback.class)
public interface ConnectionLookupClient {

    @RequestMapping(method = GET, value = "/connections", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Connections getConnections(@RequestParam("from") String from, @RequestParam("to") String to);

    @Component
    class ConnectionLookupFallback implements ConnectionLookupClient {

        @Override
        public Connections getConnections(String from, String to) {
            return new Connections();
        }
    }
}
