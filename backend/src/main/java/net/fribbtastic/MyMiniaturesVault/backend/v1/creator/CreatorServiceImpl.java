package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import net.fribbtastic.MyMiniaturesVault.backend.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@Service
public class CreatorServiceImpl implements CreatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatorServiceImpl.class);

    @Autowired
    private CreatorRepository repository;

    /**
     * get all available Creators from the repository
     *
     * @return an {@link ArrayList} of Creators
     */
    @Override
    public List<Creator> getAll() {
        LOGGER.debug("get all Creators");

        return new ArrayList<>(this.repository.findAll());
    }

    /**
     * get a Creator by its ID
     *
     * @param id the ID of the Creator
     * @return the {@link Creator}
     */
    @Override
    public Creator getOne(UUID id) {
        LOGGER.debug("get Creator with ID={}", id);

        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Add a new Creator
     *
     * @param creator the Creator that should be added
     * @return the added {@link Creator}
     */
    @Override
    public Creator addCreator(Creator creator) {
        LOGGER.debug("add new Creator with name={}", creator.getName());

        return this.repository.save(creator);
    }

    /**
     * Update an existing Creator
     *
     * @param id the ID of the Creator that should be updated
     * @param creator the {@link Creator} that should be used to update the existing Information
     * @return the updated {@link Creator}
     */
    @Override
    public Creator updateCreator(UUID id, Creator creator) {
        LOGGER.debug("update creator with ID={}", id);

        return this.repository.findById(id).map(c -> {
            creator.setId(id);
            c = creator;
            this.repository.save(c);
            return c;
        }).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Delete a Creator by its ID
     *
     * @param id the ID of the Creator that should be deleted
     */
    @Override
    public void deleteCreator(UUID id) {
        LOGGER.debug("delete Creator with ID={}", id);

        this.repository.delete(this.getOne(id));
    }
}
