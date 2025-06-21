package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import com.github.lkqm.spring.api.version.ApiVersion;
import net.fribbtastic.MyMiniaturesVault.backend.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@RestController
@RequestMapping(path = "/creator", produces = "application/json")
@ApiVersion("1")
public class CreatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatorController.class);

    @Autowired
    private CreatorServiceImpl service;

    /**
     * Get all currently available Creators from the Service layer
     * @return a {@link ResponseEntity} with the list of {@link Creator}s wrapped in a {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Creator>>> getAllCreators() {
        LOGGER.debug("get all Creators");

        List<Creator> creatorList = this.service.getAll();

        ApiResponse<List<Creator>> response = ApiResponse.createSuccessResponse(HttpStatus.OK, creatorList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Get a Creator by its unique ID from the Service Layer
     *
     * @param id the {@link UUID} of the {@link Creator}
     * @return the {@link ApiResponse} with the {@link Creator} wrapped in a {@link ResponseEntity}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Creator>> getOneCreator(@PathVariable UUID id) {
        LOGGER.debug("get Creator with ID={}", id);

        Creator creator = this.service.getOne(id);

        ApiResponse<Creator> response = ApiResponse.createSuccessResponse(HttpStatus.OK, creator);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Add a new {@link Creator} from the POSTed data and return the newly created Creator in the response
     *
     * @param creator the {@link Creator} that should be added
     * @return the {@link ApiResponse} with the newly added {@link Creator} wrapped in a {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Creator>> addNewCreator(@RequestBody Creator creator) {
        LOGGER.debug("add a new Creator with Name='{}'", creator.getName());

        Creator newCreator = this.service.addCreator(creator);

        ApiResponse<Creator> response = ApiResponse.createSuccessResponse(HttpStatus.CREATED, newCreator);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update the Creator with the provided ID and the {@link Creator} passed in the {@link RequestBody}
     *
     * @param id the ID of the {@link Creator} that should be updated
     * @param creator the {@link Creator} with the updated data
     * @return the {@link ApiResponse} with the updated {@link Creator} wrapped in a {@link ResponseEntity}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Creator>> updateCreator(@PathVariable UUID id, @RequestBody Creator creator) {
        LOGGER.debug("update Creator with ID='{}'", id);

        Creator updatedCreator = this.service.updateCreator(id, creator);

        ApiResponse<Creator> response = ApiResponse.createSuccessResponse(HttpStatus.OK, updatedCreator);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Delete the Creator with the specific ID passed with the {@link RequestBody}
     *
     * @param id the ID of the Creator that should be deleted
     * @return the {@link ApiResponse} (will 'null' data) with the updated Creator wrapped in a {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCreator(@PathVariable UUID id) {
        LOGGER.debug("delete creator with ID={}", id);

        this.service.deleteCreator(id);

        ApiResponse<?> response = ApiResponse.createSuccessResponse(HttpStatus.OK, null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
