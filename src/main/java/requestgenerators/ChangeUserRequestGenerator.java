package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.*;

import static io.restassured.RestAssured.given;

public class ChangeUserRequestGenerator extends Request{
    public ChangeUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response changeUserRequest(UserBody changeUserBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .and()
                .body(changeUserBody)
                .when()
                .patch(apiPath);
    }
}
