package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fribbtastic.MyMiniaturesVault.backend.exceptions.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(controllers =  CreatorController.class)
class CreatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreatorServiceImpl service;

    private final String endpoint = "/api/v1/creator";

    private final List<Creator> creatorList = Arrays.asList(
            new Creator(UUID.randomUUID(), "Test Creator Name 01"),
            new Creator(UUID.randomUUID(), "Test Creator Name 02")
    );

    private final Creator creator = new Creator(UUID.fromString("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"), "Test Creator Name 01");

    /**
     * Test the MVC Creator Controller to return a List of Creators
     *
     * @throws Exception thrown through the {@link MockMvc} perform
     */
    @Test
    @DisplayName("[WebMVC] get all Creators (2 Results)")
    public void testMvcGetAllCreators() throws Exception {
        Mockito.when(this.service.getAll()).thenReturn(this.creatorList);

        this.mockMvc.perform((MockMvcRequestBuilders.get(this.endpoint).accept(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Test Creator Name 01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Test Creator Name 02"));

        Mockito.verify(this.service, Mockito.times(1)).getAll();
    }

    /**
     * Test the MVC Creator Controller to return an empty List
     *
     * @throws Exception Thrown through the {@link MockMvc} perform
     */
    @Test
    @DisplayName("[WebMVC] get all Creators (0 Results")
    public void testMvcGetAllCreators_Empty() throws Exception {
        Mockito.when(this.service.getAll()).thenReturn(Collections.emptyList());

        this.mockMvc.perform((MockMvcRequestBuilders.get(this.endpoint).accept(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        Mockito.verify(this.service, Mockito.times(1)).getAll();
    }

    /**
     * Test the MVC Creator Controller to return a single {@link Creator} by its ID
     *
     * @throws Exception Thrown through the {@link MockMvc} perform
     */
    @Test
    @DisplayName("[WebMVC] get a single Creator")
    public void testMvcGetOneCreator() throws Exception {
        Mockito.when(this.service.getOne(this.creator.getId())).thenReturn(this.creator);

        this.mockMvc.perform((MockMvcRequestBuilders.get(this.endpoint + "/" + this.creator.getId()).accept(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Creator Name 01"));

        Mockito.verify(this.service, Mockito.times(1)).getOne(this.creator.getId());
    }

    /**
     * Test the MVC Creator Controller with an ID that doesn't exist.
     * The response should be that a {@link ResourceNotFoundException} is being thrown
     *
     * @throws Exception Thrown through the {@link MockMvc} perform
     */
    @Test
    @DisplayName("[WebMVC] get a missing Creator")
    public void testMvcGetOneCreator_Empty() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(this.service.getOne(id)).thenThrow(new ResourceNotFoundException(id));

        this.mockMvc.perform((MockMvcRequestBuilders.get(this.endpoint + "/" + id).accept(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Resource with the id '" + id + "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.exceptionType").value(ResourceNotFoundException.class.getSimpleName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Resource not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.details").value("Resource with the id '" + id+ "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.timestamp").isNotEmpty());

        Mockito.verify(this.service, Mockito.times(1)).getOne(id);
    }

    /**
     * Test the MVC Creator Controller to add a new Creator
     *
     * @throws Exception Thrown through the {@link MockMvc} perform
     */
    @Test
    @DisplayName("[WebMVC] add a new Creator")
    public void testMvcAddNewCreator() throws Exception {
        Mockito.when(this.service.addCreator(Mockito.any(Creator.class))).thenReturn(this.creator);

        this.mockMvc.perform((MockMvcRequestBuilders.post(this.endpoint)
                    .content(this.objectMapper.writeValueAsString(this.creator))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("eeb41c5f-9026-4cf1-9da1-23a2ef0cd9c1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Creator Name 01"));

        Mockito.verify(this.service, Mockito.times(1)).addCreator(Mockito.any(Creator.class));
    }

    /**
     * Test the MVC Creator Controller to update an existing Creator
     *
     * @throws Exception Thrown by the MockMVC Perform method
     */
    @Test
    @DisplayName("[WebMVC] update an existing Creator")
    public void testMvcUpdateCreator() throws Exception {

        UUID id = UUID.randomUUID();
        Creator updatedCreator = new Creator(id, "Test Updated Creator Name 01");

        Mockito.when(this.service.updateCreator(Mockito.any(UUID.class), Mockito.any(Creator.class))).thenReturn(updatedCreator);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.endpoint + "/{id}", id)
                        .content(this.objectMapper.writeValueAsString(updatedCreator))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Updated Creator Name 01"));

        Mockito.verify(this.service, Mockito.times(1)).updateCreator(Mockito.any(UUID.class), Mockito.any(Creator.class));
    }

    /**
     * Test the MVC Creator controller to update a missing Creator
     *
     * @throws Exception Thrown by the MockMVC perform method
     */
    @Test
    @DisplayName("[WebMVC] update a missing Creator")
    public void testMvcUpdateCreator_missing() throws Exception {

        UUID id = UUID.randomUUID();
        Creator updatedCreator = new Creator(id, "Test Updated Creator Name 01");

        Mockito.when(this.service.updateCreator(Mockito.any(UUID.class), Mockito.any(Creator.class))).thenThrow(new ResourceNotFoundException(id));

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.endpoint + "/{id}", id)
                        .content(this.objectMapper.writeValueAsString(updatedCreator))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Resource with the id '" + id + "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.exceptionType").value(ResourceNotFoundException.class.getSimpleName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Resource not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.details").value("Resource with the id '" + id + "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.timestamp").isNotEmpty());

        Mockito.verify(this.service, Mockito.times(1)).updateCreator(Mockito.any(UUID.class), Mockito.any(Creator.class));
    }

    /**
     * Test to delete an existing Creator
     *
     * @throws Exception Thrown by the MockMVC perform method
     */
    @Test
    @DisplayName("[WebMVC] delete an existing Creator")
    public void testMvcDeleteCreator() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(this.service).deleteCreator(id);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.endpoint + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

        Mockito.verify(this.service, Mockito.times(1)).deleteCreator(id);
    }

    /**
     * Test to delete a missing Creator
     *
     * @throws Exception Thrown by the MockMVC perform method
     */
    @Test
    @DisplayName("[WebMVC] delete a missing Creator")
    public void testMvcDeleteCreator_missing() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doThrow(new ResourceNotFoundException(id)).when(this.service).deleteCreator(id);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.endpoint + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Resource with the id '" + id + "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.exceptionType").value(ResourceNotFoundException.class.getSimpleName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Resource not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.details").value("Resource with the id '" + id + "' could not be found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.timestamp").isNotEmpty());
    }

}