package requestgenerators;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Request {
    private static RequestSpecification requestSpecification;

    public Request(RequestSpecification requestSpecification){
        Request.requestSpecification = requestSpecification;
    }

    public static RequestSpecification setRequestSpecification() {
        requestSpecification = RestAssured.given().baseUri("https://stellarburgers.nomoreparties.site/");
        return requestSpecification;
    }
}
