package co.sumup.jobprocessing;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JobProcessJsonComponentTest extends JobProcessCommonComponentTest {

    @Test
    public void shouldReturn200AndJsonBody() {
        File payload = new File(this.getClass().getResource("successPayload.json").getFile());
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, false)
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(200)
        .and()
            .body("[0].name", equalTo("task-1"))
            .body("[1].name", equalTo("task-3"))
            .body("[2].name", equalTo("task-2"))
            .body("[3].name", equalTo("task-4"));
    }

    @Test
    public void shouldReturn200WithEmptyArrayWhenTasksEmpty() {
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, false)
            .contentType(ContentType.JSON)
            .body("{ \"tasks\": [] }")
        .when()
            .post("/job")
        .then()
            .statusCode(200)
        .and()
            .body(equalTo("[]"));
    }

    @Override
    public boolean getBashScripts() {
        return false;
    }
}
