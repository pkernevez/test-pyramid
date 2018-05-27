package ch.octo.blog.transport.connectionlookup;

import ch.octo.blog.transport.dto.Connections;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "transport", url = "${transport.url}")
public interface TransportClient {

    @RequestMapping(method = GET, value = "/connections", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Connections getConnections(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("fields[]") List<String> fields);

    default Connections getConnections(String from, String to) {
        return getConnections(from, to, Arrays.asList("connections/from", "connections/to"));
    }

}
