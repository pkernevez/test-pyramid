package ch.octo.blog.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

    @NotNull
    private Checkpoint from;

    @NotNull
    private Checkpoint to;
}
