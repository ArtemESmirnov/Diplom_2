package requestgenerators;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.*;

import static io.restassured.RestAssured.given;

public class ChangeUserRequestGenerator extends Request{
    public ChangeUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response changeUserRequest(WholeUserBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }

    public static Response changeUserRequest(EmailPasswordUserBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }

    public static Response changeUserRequest(ChangeUserPasswordBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }

    public static Response changeUserRequest(ChangeUserEmailBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }

    public static Response changeUserRequest(ChangeUserNameBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }
}
