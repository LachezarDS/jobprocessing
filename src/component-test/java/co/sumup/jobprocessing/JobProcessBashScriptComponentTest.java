package co.sumup.jobprocessing;

import io.restassured.http.ContentType;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JobProcessBashScriptComponentTest extends JobProcessCommonComponentTest {

    @Test
    public void shouldReturn200AndBashScript() throws IOException {
        File payload = new File(this.getClass().getResource("successPayload.json").getFile());
        String expectedResponse = IOUtils.toString(this.getClass()
                                                       .getResourceAsStream("successPayloadBashScriptResponse.sh"),
                                                   StandardCharsets.UTF_8);
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, true)
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/job")
        .then()
            .statusCode(200)
        .and()
            .body(equalTo(expectedResponse));
    }

    @Test
    public void shouldReturn200WithNoCommandsScriptWhenTasksEmpty() {
        given()
            .queryParam(BASH_SCRIPT_PARAMETER_NAME, true)
            .contentType(ContentType.JSON)
            .body("{ \"tasks\": [] }")
        .when()
            .post("/job")
        .then()
            .statusCode(200)
        .and()
            .body(equalTo("#!/usr/bin/env bash\n\n"));
    }

    @Override
    public boolean getBashScripts() {
        return true;
    }
}
