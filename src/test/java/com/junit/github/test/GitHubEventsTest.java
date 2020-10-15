package com.junit.github.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class GitHubEventsTest {

    private static String URL;

    @BeforeClass
    public static void setUp() {
        URL = "https://api.github.com/events";
    }

    @Test
    public void testGetGitHubEvents() {

        var response = given()
                .when()
                .get(URL)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("schema/schema-file.json"))
                .extract()
                .response();

        assertThat(response, notNullValue());
        assertThat(response.jsonPath().getList("").size(), greaterThan(0));

        var pushCount = response.jsonPath().getList("type").stream().filter(x -> x.equals("PushEvent")).count();
        var createEventCount = response.jsonPath().getList("type").stream().filter(x -> x.equals("CreateEvent")).count();

        System.out.println("PushEvent count is: "+pushCount);
        System.out.println("CreateEvent count is: "+createEventCount);
    }
}
