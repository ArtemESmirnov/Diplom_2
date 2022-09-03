package requestgenerators;

import io.restassured.specification.RequestSpecification;
import pojos.GetIngredientsResponseBody;
import pojos.IngredientData;

import static io.restassured.RestAssured.given;

public class GetIngredientsRequestGenerator extends Request{
    public GetIngredientsRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static String[] getIngredientsHashList(String apiPath){
        GetIngredientsResponseBody ingredientsResponseBody = given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .get(apiPath)
                .as(GetIngredientsResponseBody.class);
        String[] ingredientList = new String[ingredientsResponseBody.getData().length];
        for (int i = 0; i < ingredientsResponseBody.getData().length; i++)
            ingredientList[i] = ingredientsResponseBody.getData()[i].get_id();
        return ingredientList;
    }
}
