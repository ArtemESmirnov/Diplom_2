package requestgenerators;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DeleteUserRequestGenerator extends Request{
    public DeleteUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response deleteUserRequest(String token, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .when()
                .delete(apiPath);
    }
}
