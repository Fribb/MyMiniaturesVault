package net.fribbtastic.MyMiniaturesVault.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(UUID id) {
        super(String.format("Resource with the id '%s' could not be found", id));
    }
}
