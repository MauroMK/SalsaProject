package example;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import example.Pojo.CreateUserPojo;
import example.Pojo.LoginUserSuccessPojo;
import example.Pojo.LoginUserUnsuccessPojo;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresAPITest {
    private static final String BASE_URL = "https://reqres.in/api";
    private static final String AUTH_TOKEN = "QpwL5tke4Pnpja7X4";

    @Test
    public void testCreateUser_Success() {
        CreateUserPojo createUserPojo = new CreateUserPojo("Pedro", "Suporte");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createUserPojo)
                .post(BASE_URL + "/users");

        response.then()
                .statusCode(201)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("JsonSchema/createUser.json"));
    }

    @Test
    public void testLoginUser_Success() {
        LoginUserSuccessPojo loginUserSuccessPojo = new LoginUserSuccessPojo("eve.holt@reqres.in", "cityslicka");

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .body(loginUserSuccessPojo)
                .post(BASE_URL + "/login")
                .then();

        response.statusCode(200);
        response.body("token", equalTo(AUTH_TOKEN));
    }

    @Test
    public void testLoginUser_Failed() {
        LoginUserUnsuccessPojo loginUserUnsuccessPojo = new LoginUserUnsuccessPojo("peter@klaven");

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .body(loginUserUnsuccessPojo)
                .post(BASE_URL + "/login")
                .then();

        response.statusCode(400);
        response.body("error", containsString("Missing password"));
    }

    @Test
    public void testDeleteUser() {
        Response response = given()
                .delete(BASE_URL + "/users/2");

        response.then()
                .statusCode(204);
    }

    @Test
    public void testGetSingleUser() {
        Response response = given()
                .get(BASE_URL + "/users/2");

        response.then()
                .statusCode(200);
    }
}