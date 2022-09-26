package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.EmailPasswordUserBody;

import static io.restassured.RestAssured.given;

public class LoginUserRequestGenerator extends Request{
    public LoginUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response loginUserRequest(EmailPasswordUserBody loginUserBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .and()
                .body(loginUserBody)
                .when()
                .post(apiPath);
    }
}
