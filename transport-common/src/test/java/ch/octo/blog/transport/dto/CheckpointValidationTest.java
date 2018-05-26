package ch.octo.blog.transport.dto;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static ch.octo.blog.transport.dto.Checkpoint.ValidTimes.DEFAULT_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckpointValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void checkpointWithNoLocation_ShouldNotBeValid() {
        // given
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.setDeparture(42L);
        checkpoint.setArrival(42L);

        // when
        Set<ConstraintViolation<Checkpoint>> violations = validator.validate(checkpoint);

        // then
        assertEquals(1, violations.size());
        assertEquals("station", violations.stream().findFirst().get().getPropertyPath().toString());
    }

    @Test
    public void checkpointWithNoDepartureNoArrival_ShouldNotBeValid() {
        // given
        Checkpoint checkpoint = new Checkpoint(new Location());

        // when
        Set<ConstraintViolation<Checkpoint>> violations = validator.validate(checkpoint);

        // then
        assertEquals(1, violations.size());
        assertEquals(DEFAULT_MESSAGE, violations.stream().findFirst().get().getMessage());
    }

    @Test
    public void checkpointWithNoDepartureButArrival_ShouldBeValid() {
        // given
        Checkpoint checkpoint = new Checkpoint(new Location());
        checkpoint.setArrival(42L);

        // when
        Set<ConstraintViolation<Checkpoint>> violations = validator.validate(checkpoint);

        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void checkpointWithNoArrivalButDeparture_ShouldBeValid() {
        // given
        Checkpoint checkpoint = new Checkpoint(new Location());
        checkpoint.setDeparture(42L);

        // when
        Set<ConstraintViolation<Checkpoint>> violations = validator.validate(checkpoint);

        // then
        assertTrue(violations.isEmpty());
    }
}

