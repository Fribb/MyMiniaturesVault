package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import java.util.List;
import java.util.UUID;

/**
 * @author Frederic Eßer
 */
public interface CreatorService {

    List<Creator> getAll();
    Creator getOne(UUID id);
    Creator addCreator(Creator creator);
    Creator updateCreator(UUID id, Creator creator);
    void deleteCreator(UUID id);
}
