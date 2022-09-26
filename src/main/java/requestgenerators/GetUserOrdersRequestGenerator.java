package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class GetUserOrdersRequestGenerator extends Request{
    public GetUserOrdersRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response getUserOrdersRequest(String token, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(apiPath);
    }
}
