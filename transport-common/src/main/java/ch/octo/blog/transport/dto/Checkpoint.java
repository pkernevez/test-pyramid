package ch.octo.blog.transport.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.time.LocalDateTime;
import java.util.OptionalLong;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Checkpoint.ValidTimes
@JsonInclude(NON_NULL)
public class Checkpoint {
    @NotNull
    Location station;

    @JsonProperty("departureTimestamp")
    Long departure;

    @JsonProperty("arrivalTimestamp")
    Long arrival;

    public Checkpoint(Location location) {
        station = location;
    }


    @Documented
    @Constraint(validatedBy = CheckpointTimesValidator.class)
    @Target({ ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ValidTimes {
        String DEFAULT_MESSAGE = "Either departure or arrival must be set (both cannot be null at the same time)";

        String message() default DEFAULT_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class CheckpointTimesValidator implements ConstraintValidator<ValidTimes, Checkpoint> {
        @Override
        public boolean isValid(Checkpoint checkpoint, ConstraintValidatorContext context) {
            return checkpoint.arrival != null || checkpoint.departure != null;
        }
    }
}
