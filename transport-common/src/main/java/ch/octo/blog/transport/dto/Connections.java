package ch.octo.blog.transport.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Connections {
    private final List<Connection> connections = new ArrayList<>();

    public int size() {
        return connections.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return connections.isEmpty();
    }

    public Connection get(int index) {
        return connections.get(index);
    }

    public boolean add(Connection connection) {
        return connections.add(connection);
    }
}
