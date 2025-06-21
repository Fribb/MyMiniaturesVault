package net.fribbtastic.MyMiniaturesVault.backend.v1.creator;

import net.fribbtastic.MyMiniaturesVault.backend.BackendApplication;
import net.fribbtastic.MyMiniaturesVault.backend.exceptions.ResourceNotFoundException;
import net.fribbtastic.MyMiniaturesVault.backend.responses.ApiResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

/**
 * @author Frederic Eßer
 */
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreatorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private String url;

    @BeforeEach
    public void setUp() {
        this.url = "http://localhost:" + this.port + "/api/v1/creator";
    }

    /**
     * Integration Test for the Creator endpoint.
     * Seed the Database with existing Data and get all the Creators (2)
     */
    @Test
    @DisplayName("[Integration] get All Creators (2 Results")
    @Sql({"classpath:creator/truncate.sql"})
    @Sql({"classpath:creator/insert.sql"})
    public void testIntegrationGetAllCreators() {
        ResponseEntity<ApiResponse<List<Creator>>> response = this.template.exchange(this.url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getStatusCode()).isEqualTo(200);
        Assertions.assertThat(response.getBody().getData()).isNotNull();
        Assertions.assertThat(response.getBody().getData().size()).isEqualTo(3);

        Assertions.assertThat(response.getBody().getData().getFirst().getId()).isEqualTo(UUID.fromString("0320a817-a06b-48d8-8d36-a55a95650a10"));
        Assertions.assertThat(response.getBody().getData().getFirst().getName()).isEqualTo("Test Creator Name 01");

        Assertions.assertThat(response.getBody().getData().get(1).getId()).isEqualTo(UUID.fromString("596202da-948a-4d9d-bb87-0bae675f7336"));
        Assertions.assertThat(response.getBody().getData().get(1).getName()).isEqualTo("Test Creator Name 02");

        Assertions.assertThat(response.getBody().getData().get(2).getId()).isEqualTo(UUID.fromString("6e169bf3-ac69-49b9-8e9c-38439b45e9bd"));
        Assertions.assertThat(response.getBody().getData().get(2).getName()).isEqualTo("Test Creator Name 03");
    }

    /**
     * Integration Test for the Creator endpoint.
     * The returned List should be empty and not contain any Creators
     */
    @Test
    @DisplayName("[Integration] get all Creators (0 Results)")
    @Sql({"classpath:creator/truncate.sql"})
    public void testIntegrationGetAllCreators_Empty() {
        ResponseEntity<ApiResponse<List<Creator>>> response = this.template.exchange(this.url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getStatusCode()).isEqualTo(200);
        Assertions.assertThat(response.getBody().getData()).isNotNull();
        Assertions.assertThat(response.getBody().getData().size()).isEqualTo(0);
    }

    /**
     * Integration Test for the Creator endpoint.
     * Add a new Creator and return it.
     *
     */
    @Test
    @DisplayName("[Integration] add a new Creator")
    @Sql({"classpath:creator/truncate.sql"})
    public void testIntegrationAddNewCreator() {
        Creator newCreator = new Creator("Test New Creator 01");

        HttpEntity<Creator> creatorHttpEntity = new HttpEntity<>(newCreator);

        ResponseEntity<ApiResponse<Creator>> response = this.template.exchange(this.url, HttpMethod.POST, creatorHttpEntity, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(201);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getStatusCode()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getData()).isNotNull();
        Assertions.assertThat(response.getBody().getData().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getData().getId().toString()).isNotEmpty();
        Assertions.assertThat(response.getBody().getData().getName()).isEqualTo("Test New Creator 01");
    }

    /**
     * Integration Test for the Creator Endpoint,
     * Test that the Creator can be updated
     */
    @Test
    @DisplayName("[Integration] Update Creator")
    @Sql({"classpath:creator/insert.sql"})
    public void testIntegrationUpdateCreator() {
        UUID id = UUID.fromString("0320a817-a06b-48d8-8d36-a55a95650a10");
        Creator updateCreator = new Creator("Test Updated Name 01");

        HttpEntity<Creator> httpEntity = new HttpEntity<>(updateCreator);

        ResponseEntity<ApiResponse<Creator>> response = this.template.exchange(this.url + "/" + id, HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getStatusCode()).isEqualTo(200);
        Assertions.assertThat(response.getBody().getData()).isNotNull();
        Assertions.assertThat(response.getBody().getData().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getData().getId().toString()).isEqualTo("0320a817-a06b-48d8-8d36-a55a95650a10");
        Assertions.assertThat(response.getBody().getData().getName()).isEqualTo("Test Updated Name 01");
    }

    /**
     * Integration Test for the Creator Endpoint,
     * Test that the missing Creator cannot be Updated and shows an error message
     */
    @Test
    @DisplayName("[Integration] Update missing Creator")
    @Sql({"classpath:creator/truncate.sql"})
    public void testIntegrationUpdateCreator_missing() {
        UUID id = UUID.fromString("0320a817-a06b-48d8-8d36-a55a95650a10");
        Creator updateCreator = new Creator("Test Updated Name 01");

        HttpEntity<Creator> httpEntity = new HttpEntity<>(updateCreator);

        ResponseEntity<ApiResponse<Creator>> response = this.template.exchange(this.url + "/" + id, HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(404);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getStatusCode()).isEqualTo(404);
        Assertions.assertThat(response.getBody().getErrorDetails()).isNotNull();
        Assertions.assertThat(response.getBody().getErrorDetails().getExceptionType()).isEqualTo(ResourceNotFoundException.class.getSimpleName());
        Assertions.assertThat(response.getBody().getErrorDetails().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(response.getBody().getErrorDetails().getDetails()).isEqualTo("Resource with the id '" + id + "' could not be found");
        Assertions.assertThat(response.getBody().getErrorDetails().getTimestamp()).isNotEmpty();
    }

    /**
     * Integration Test for the Creator Endpoint,
     * Test that we can delete an existing Creator
     */
    @Test
    @DisplayName("[Integration] delete Creator")
    @Sql({"classpath:creator/truncate.sql"})
    @Sql({"classpath:creator/insert.sql"})
    public void testIntegrationDeleteCreator() {
        UUID id = UUID.fromString("0320a817-a06b-48d8-8d36-a55a95650a10");

        ResponseEntity<ApiResponse<Creator>> beforeDeletionResponse = this.template.exchange(this.url + "/" + id, HttpMethod.GET, null,  new ParameterizedTypeReference<>(){
        });

        // Asser that there is a Creator and the Data is as expected
        Assertions.assertThat(beforeDeletionResponse.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(beforeDeletionResponse.getBody()).isNotNull();
        Assertions.assertThat(beforeDeletionResponse.getBody().getStatusCode()).isEqualTo(200);
        Assertions.assertThat(beforeDeletionResponse.getBody().getData()).isNotNull();
        Assertions.assertThat(beforeDeletionResponse.getBody().getData().getId()).isNotNull();
        Assertions.assertThat(beforeDeletionResponse.getBody().getData().getId().toString()).isEqualTo(id.toString());
        Assertions.assertThat(beforeDeletionResponse.getBody().getData().getName()).isEqualTo("Test Creator Name 01");

        // delete the Creator
        ResponseEntity<ApiResponse<Creator>> deletionResponse = this.template.exchange(this.url + "/" + id, HttpMethod.DELETE, null,  new ParameterizedTypeReference<>(){
        });

        Assertions.assertThat(deletionResponse.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(deletionResponse.getBody()).isNotNull();
        Assertions.assertThat(deletionResponse.getBody().getStatusCode()).isEqualTo(200);
        Assertions.assertThat(deletionResponse.getBody().getData()).isNull();

        // check if the Creator was actually deleted
        ResponseEntity<ApiResponse<Creator>> afterDeletionResponse = this.template.exchange(this.url + "/" + id, HttpMethod.GET, null,  new ParameterizedTypeReference<>(){
        });

        Assertions.assertThat(afterDeletionResponse.getStatusCode().value()).isEqualTo(404);
        Assertions.assertThat(afterDeletionResponse.getBody()).isNotNull();
        Assertions.assertThat(afterDeletionResponse.getBody().getStatusCode()).isEqualTo(404);
        Assertions.assertThat(afterDeletionResponse.getBody().getErrorDetails()).isNotNull();
        Assertions.assertThat(afterDeletionResponse.getBody().getErrorDetails().getExceptionType()).isEqualTo(ResourceNotFoundException.class.getSimpleName());
        Assertions.assertThat(afterDeletionResponse.getBody().getErrorDetails().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(afterDeletionResponse.getBody().getErrorDetails().getDetails()).isEqualTo("Resource with the id '" + id + "' could not be found");
        Assertions.assertThat(afterDeletionResponse.getBody().getErrorDetails().getTimestamp()).isNotEmpty();
    }

    /**
     * Integration Test for the Creator Endpoint,
     * Test that we cannot delete a missing Creator and that there is an error message showing instead
     */
    @Test
    @DisplayName("[Integration] delete missing Creator")
    @Sql({"classpath:creator/truncate.sql"})
    public void testIntegrationDeleteCreator_missing() {
        UUID id = UUID.fromString("0320a817-a06b-48d8-8d36-a55a95650a10");

        // delete the Creator
        ResponseEntity<ApiResponse<Creator>> deletionErrorResponse = this.template.exchange(this.url + "/" + id, HttpMethod.DELETE, null,  new ParameterizedTypeReference<>(){
        });

        Assertions.assertThat(deletionErrorResponse.getStatusCode().value()).isEqualTo(404);
        Assertions.assertThat(deletionErrorResponse.getBody()).isNotNull();
        Assertions.assertThat(deletionErrorResponse.getBody().getStatusCode()).isEqualTo(404);
        Assertions.assertThat(deletionErrorResponse.getBody().getErrorDetails()).isNotNull();
        Assertions.assertThat(deletionErrorResponse.getBody().getErrorDetails().getExceptionType()).isEqualTo(ResourceNotFoundException.class.getSimpleName());
        Assertions.assertThat(deletionErrorResponse.getBody().getErrorDetails().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(deletionErrorResponse.getBody().getErrorDetails().getDetails()).isEqualTo("Resource with the id '" + id + "' could not be found");
        Assertions.assertThat(deletionErrorResponse.getBody().getErrorDetails().getTimestamp()).isNotEmpty();
    }
}
