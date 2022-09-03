package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.LoginUserBody;

import static io.restassured.RestAssured.given;

public class LoginUserRequestGenerator extends Request{
    public LoginUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response loginUserRequest(LoginUserBody loginUserBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(loginUserBody)
                .when()
                .post(apiPath);
    }
}
