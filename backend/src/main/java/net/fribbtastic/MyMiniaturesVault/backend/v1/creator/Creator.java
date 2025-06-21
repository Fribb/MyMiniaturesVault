package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@Entity
@Table(name = "CREATORS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    public Creator(@NonNull String name) {
        this.name = name;
    }
}
