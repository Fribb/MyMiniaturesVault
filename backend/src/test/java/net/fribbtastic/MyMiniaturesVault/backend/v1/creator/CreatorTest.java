package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author Frederic Eßer
 */
class CreatorTest {

    private static Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Create a Creator with a name
     */
    @Test
    @DisplayName("Creator Test with Name")
    public void testNewCreator() {

        Set<ConstraintViolation<Creator>> violationSet = validator.validate(new Creator("Test Creator 01"));

        Assertions.assertThat(violationSet.size()).isEqualTo(0);
    }

    /**
     * Create a Creator without a name
     */
    @Test
    @DisplayName("Creator Test without a Name")
    public void testNewCreator_blankName() {
        Set<ConstraintViolation<Creator>> violationSet = validator.validate(new Creator(""));

        Assertions.assertThat(violationSet.size()).isEqualTo(1);
    }

}