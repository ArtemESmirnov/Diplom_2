package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.CreateOrderBody;

import static io.restassured.RestAssured.given;

public class CreateOrderRequestGenerator extends Request{

    public CreateOrderRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response createOrderRequest(CreateOrderBody createOrderBody, String apiPath, String token){
        return given()
                .spec(setRequestSpecification())
                .header("Authorization", token)
                .and()
                .body(createOrderBody)
                .when()
                .post(apiPath);
    }
}
