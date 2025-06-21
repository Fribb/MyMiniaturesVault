package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Frederic Eßer
 */
@DataJpaTest
public class CreatorJPATest {

    @Autowired
    private CreatorRepository repository;

    private Creator creator;

    @BeforeEach
    public void setUpEach() {
        this.creator = Creator.builder().name("Test Creator Name 01").build();
    }

    @Test
    @DisplayName("Injected Components are not Null")
    public void testInjectComponentsNotNull() {
        Assertions.assertThat(this.repository).isNotNull();
    }

    @Test
    @DisplayName("Save a new Creator in DB")
    public void testSaveNewCreator() {
        Creator savedCreator = this.repository.save(this.creator);

        Assertions.assertThat(savedCreator).isNotNull();
        Assertions.assertThat(savedCreator.getId()).isNotNull();
        Assertions.assertThat(savedCreator.getName()).isEqualTo("Test Creator Name 01");
    }

    @Test
    @DisplayName("Save a new Creator with empty Name")
    @Transactional
    public void testSaveNewCreator_noName() {

        Assertions.assertThatThrownBy(() -> this.repository.saveAndFlush(new Creator(""))).isInstanceOf(ConstraintViolationException.class);
        //Assertions.assertThatThrownBy(() -> this.repository.saveAndFlush(new Creator(null))).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Get all Creators")
    public void testGetAllCreators() {
        // create a new Creator
        Creator c2 = Creator.builder().name("Test Creator Name 02").build();

        // save creators
        this.repository.save(this.creator);
        this.repository.save(c2);

        List<Creator> creatorList = this.repository.findAll();

        Assertions.assertThat(creatorList).isNotNull();
        Assertions.assertThat(creatorList.size()).isEqualTo(2);
        Assertions.assertThat(creatorList.getFirst().getName()).isEqualTo("Test Creator Name 01");
        Assertions.assertThat(creatorList.get(1).getName()).isEqualTo("Test Creator Name 02");
    }

    @Test
    @DisplayName("get Creator by ID")
    public void testGetCreatorById() {
        // save creator
        this.repository.save(this.creator);

        // get the creator with the ID from the DB
        Creator dbCreator = this.repository.findById(this.creator.getId()).orElse(null);

        Assertions.assertThat(dbCreator).isNotNull();
        Assertions.assertThat(dbCreator.getName()).isEqualTo("Test Creator Name 01");
    }

    @Test
    @DisplayName("update Creator information")
    public void testUpdateCreator() {
        //save creator
        this.repository.save(this.creator);

        // get the creator with the ID from the DB
        Creator dbCreator = this.repository.findById(this.creator.getId()).orElse(null);

        Assertions.assertThat(dbCreator).isNotNull();
        Assertions.assertThat(dbCreator.getName()).isEqualTo("Test Creator Name 01");

        // set a different name for the creator
        dbCreator.setName("New Creator Name 01");

        // save the updated Creator
        Creator updatedCreator = this.repository.save(dbCreator);

        Assertions.assertThat(updatedCreator).isNotNull();
        Assertions.assertThat(updatedCreator.getName()).isEqualTo("New Creator Name 01");
    }

    @Test
    @DisplayName("delete Creator")
    public void testDeleteCreator() {
        // save creator
        Creator dbCreator = this.repository.save(this.creator);

        Assertions.assertThat(dbCreator).isNotNull();
        Assertions.assertThat(dbCreator.getId()).isNotNull();
        Assertions.assertThat(dbCreator.getName()).isEqualTo("Test Creator Name 01");

        // delete the creator
        this.repository.deleteById(dbCreator.getId());

        // try to find the deleted creator
        Optional<Creator> creatorOptional = this.repository.findById(this.creator.getId());

        Assertions.assertThat(creatorOptional).isEmpty();
    }
}
