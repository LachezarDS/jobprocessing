package co.sumup.jobprocessing;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JobProcessingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class JobProcessCommonComponentTest {

    protected static final String BASH_SCRIPT_PARAMETER_NAME = "bashScript";

    @LocalServerPort
    private int port;

    @PostConstruct
    public void init() {
        RestAssured.port = this.port;
    }

    public abstract boolean getBashScripts();

    @Test
    public void shouldReturn400WhenMultipleTasksWithSameName() {
        File payload = new File(this.getClass().getResource("duplicateNamesPayload.json").getFile());
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("There are multiple tasks with the same name."));
    }

    @Test
    public void shouldReturn400WhenNoIndependentTask() {
        File payload = new File(this.getClass().getResource("noIndependentTaskPayload.json").getFile());
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("The tasks cannot be ordered due to cyclic dependency."));
    }

    @Test
    public void shouldReturn400WhenCyclicDependency() {
        File payload = new File(this.getClass().getResource("cyclicDependencyPayload.json").getFile());
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("The tasks cannot be ordered due to cyclic dependency."));
    }

    @Test
    public void shouldReturn400WhenDependentTaskNotExists() {
        File payload = new File(this.getClass().getResource("dependentTaskNotExistsPayload.json").getFile());
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("There is no task with name 'task-3'."));
    }

    @Test
    public void shouldReturn400WhenTaskNameNull() {
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body("{\"tasks\": [{ \"name\": null, \"command\": \"test\" }]}")
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("There is an error with the request structure."));
    }

    @Test
    public void shouldReturn400WhenTaskCommandNull() {
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, getBashScripts())
            .contentType(ContentType.JSON)
            .body("{\"tasks\": [{ \"name\": \"test\", \"command\": null }]}")
        .when()
            .post("/job")
        .then()
            .statusCode(400)
        .and()
            .body("status", equalTo(400))
            .body("message", equalTo("There is an error with the request structure."));
    }

}
