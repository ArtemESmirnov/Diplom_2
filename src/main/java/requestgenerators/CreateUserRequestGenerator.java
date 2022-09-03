package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.CreateUserBody;

import static io.restassured.RestAssured.given;

public class CreateUserRequestGenerator extends Request{
    public CreateUserRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response createUserRequest(CreateUserBody createUserBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(createUserBody)
                .when()
                .post(apiPath);
    }
}
