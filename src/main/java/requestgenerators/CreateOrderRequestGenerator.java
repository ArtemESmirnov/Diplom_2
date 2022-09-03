package requestgenerators;

import io.restassured.http.ContentType;
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
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .and()
                .body(createOrderBody)
                .when()
                .post(apiPath);
    }
}
