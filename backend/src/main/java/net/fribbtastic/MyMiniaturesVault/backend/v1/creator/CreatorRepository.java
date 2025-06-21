package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Frederic Eßer
 */
public interface CreatorRepository extends JpaRepository<Creator, UUID> {
}
