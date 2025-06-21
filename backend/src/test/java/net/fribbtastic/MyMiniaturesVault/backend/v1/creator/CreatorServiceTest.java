package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import net.fribbtastic.MyMiniaturesVault.backend.exceptions.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

/**
 * @author Frederic Eßer
 */
@ExtendWith(MockitoExtension.class)
class CreatorServiceTest {

    @Mock
    private CreatorRepository repository;

    @InjectMocks
    private CreatorServiceImpl service;

    private final Creator testCreator = new Creator(UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"), "Test Creator Name 01");

    private final List<Creator> testCreatorList = Arrays.asList(
            new Creator(UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"), "Test Creator Name 01"),
            new Creator(UUID.fromString("1eeaabc2-1093-4692-858b-e21cdee7ead6"), "Test Creator Name 02")
    );

    /**
     * Test the Service Layer of the Creator to get all Creators
     */
    @Test
    @DisplayName("get all Creators (2 Results)")
    void testGetAll_WithResults() {
        Mockito.when(this.repository.findAll()).thenReturn(this.testCreatorList);

        List<Creator> creators = this.service.getAll();

        Assertions.assertThat(creators).isNotNull();
        Assertions.assertThat(creators.size()).isEqualTo(2);

        Assertions.assertThat(creators.getFirst().getId().toString()).isEqualTo("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1");
        Assertions.assertThat(creators.getFirst().getName()).isEqualTo("Test Creator Name 01");

        Assertions.assertThat(creators.get(1).getId().toString()).isEqualTo("1eeaabc2-1093-4692-858b-e21cdee7ead6");
        Assertions.assertThat(creators.get(1).getName()).isEqualTo("Test Creator Name 02");

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    /**
     * Test the Service Layer of the Creator to get all Creators
     * This case will assume that the returned list is empty
     */
    @Test
    @DisplayName("get all Creators (0 Results)")
    void testGetAll_Empty() {
        Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());

        List<Creator> creators = this.service.getAll();

        Assertions.assertThat(creators).isNotNull();
        Assertions.assertThat(creators).isEmpty();

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    /**
     * Test the Service Layer of the Creator to get one specific Creator by its ID
     */
    @Test
    @DisplayName("get one Creator")
    public void testGetOne() {
        Mockito.when(this.repository.findById(this.testCreator.getId())).thenReturn(Optional.of(this.testCreator));

        Creator creator = this.service.getOne(this.testCreator.getId());

        Assertions.assertThat(creator).isNotNull();
        Assertions.assertThat(creator.getId()).isEqualTo(UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"));
        Assertions.assertThat(creator.getName()).isEqualTo("Test Creator Name 01");

        Mockito.verify(this.repository, Mockito.times(1)).findById(this.testCreator.getId());
    }

    /**
     * Test the Service Layer of the Creator to get one non-existing Creator.
     * Assert that there is a {@link ResourceNotFoundException} being thrown
     */
    @Test
    @DisplayName("get one Creator that doesn't exist")
    public void testGetOne_Missing() {
        UUID id = UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1");

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.service.getOne(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Resource with the id 'eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1' could not be found");

        Mockito.verify(this.repository, Mockito.times(1)).findById(id);
    }

    /**
     * Test the Service Layer of the Creator to add a new Creator
     */
    @Test
    @DisplayName("add a new Creator")
    public void testAddCreator() {

        Mockito.when(this.repository.save(this.testCreator)).thenReturn(this.testCreator);

        Creator savedCreator = this.service.addCreator(this.testCreator);

        Assertions.assertThat(savedCreator).isNotNull();
        Assertions.assertThat(savedCreator.getId()).isEqualTo(UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"));
        Assertions.assertThat(savedCreator.getName()).isEqualTo("Test Creator Name 01");

        Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Creator.class));
    }

    /**
     * Test the Service Layer of the Creator to update an existing Creator
     */
    @Test
    @DisplayName("update an existing Creator")
    public void testUpdateCreator() {
        Mockito.when(this.repository.findById(this.testCreator.getId())).thenReturn(Optional.of(this.testCreator));
        Mockito.when(this.repository.save(this.testCreator)).thenReturn(this.testCreator);

        Creator updatedCreator = this.service.updateCreator(this.testCreator.getId(), this.testCreator);

        Assertions.assertThat(updatedCreator).isNotNull();
        Assertions.assertThat(updatedCreator.getId()).isEqualTo(this.testCreator.getId());
        Assertions.assertThat(updatedCreator.getName()).isEqualTo(this.testCreator.getName());

        Mockito.verify(this.repository, Mockito.times(1)).findById(this.testCreator.getId());
        Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Creator.class));
    }

    /**
     * Test the Service Layer of the Creator to try to update a non-existing Creator
     * Assert that there is a {@link ResourceNotFoundException} being thrown
     */
    @Test
    @DisplayName("update a missing Creator")
    public void testUpdateCreator_missing() {
        Mockito.when(this.repository.findById(this.testCreator.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.service.updateCreator(this.testCreator.getId(), this.testCreator))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Resource with the id 'eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1' could not be found");

        Mockito.verify(this.repository, Mockito.times(1)).findById(this.testCreator.getId());
    }

    /**
     * Test the Service Layer of the Creator to delete an existing Creator
     */
    @Test
    @DisplayName("delete a Creator")
    public void testDeleteCreator() {
        Mockito.when(this.repository.findById(this.testCreator.getId())).thenReturn(Optional.of(this.testCreator));

        this.service.deleteCreator(this.testCreator.getId());

        Mockito.verify(this.repository, Mockito.times(1)).findById(this.testCreator.getId());
        Mockito.verify(this.repository, Mockito.times(1)).delete(this.testCreator);
    }

    /**
     * Test the Service layer of the Creator to delete a non-existent Creator
     * Assert that there is a {@link ResourceNotFoundException} being thrown
     */
    @Test
    @DisplayName("delete a missing Creator")
    public void testDeleteCreator_missing() {
        UUID id = UUID.randomUUID();

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.service.deleteCreator(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Resource with the id '" + id + "' could not be found");

        Mockito.verify(this.repository, Mockito.times(1)).findById(id);
    }
}