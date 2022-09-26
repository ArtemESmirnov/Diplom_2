import io.restassured.response.Response;
import org.junit.Test;
import pojos.CreateOrderBody;
import pojos.EmailPasswordUserBody;
import pojos.WholeUserBody;


import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateOrderRequestGenerator.createOrderRequest;
import static requestgenerators.CreateUserRequestGenerator.createUserRequest;
import static requestgenerators.DeleteUserRequestGenerator.deleteUserRequest;
import static requestgenerators.GetIngredientsRequestGenerator.getIngredientsHashList;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class CreateOrderTest {
    final static String ingredientsApiPath = "/api/ingredients";
    final static String authRegisterApiPath = "/api/auth/register";
    final static String authUserApiPath = "/api/auth/user";
    final static String authLoginApiPath = "/api/auth/login";
    final static String ordersApiPath = "/api/orders";
    final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    final String name = "uniqueName";
    final String email = "uniqueEmail@yandex.ru";
    final String password = "uniquePassword";
    String[] ingredients;
    String[] ingredientsToSend;
    final Random random = new Random();
    Response orderResponse;
    Response userResponse;
    String userToken;

    private void createAndLoginUser(){
        WholeUserBody userBody = new WholeUserBody(email, password, name);
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, password);

        createUserRequest(userBody, authRegisterApiPath);
        userResponse = loginUserRequest(loginUserBody, authLoginApiPath);
        userToken = userResponse.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    private void createIngredientsList(){
        ingredients = getIngredientsHashList(ingredientsApiPath);
        int numberOfIngredients = random.nextInt(ingredients.length) + 1;

        ingredientsToSend = new String[numberOfIngredients];
        for(int i = 0; i < numberOfIngredients; i++)
            ingredientsToSend[i] = ingredients[random.nextInt(ingredients.length)];
    }

    @Test
    public void createOrderWithLoginValidIngredientsShouldBePossibleStatusCode(){
        createAndLoginUser();
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertEquals(SC_OK, orderResponse.statusCode());

        deleteUserRequest(userToken, authUserApiPath);
    }

    @Test
    public void createOrderWithLoginValidIngredientsShouldBePossibleBody(){
        createAndLoginUser();
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertTrue(orderResponse.path("success"));
        assertNotNull(orderResponse.path("name"));
        assertNotNull(orderResponse.path("order.number"));

        deleteUserRequest(userToken, authUserApiPath);
    }

    @Test
    public void createOrderWithoutLoginValidIngredientsShouldBePossibleStatusCode(){
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, "");
        assertEquals(SC_OK, orderResponse.statusCode());
    }

    @Test
    public void createOrderWithoutLoginValidIngredientsShouldBePossibleBody(){
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, "");
        assertTrue(orderResponse.path("success"));
        assertNotNull(orderResponse.path("name"));
        assertNotNull(orderResponse.path("order.number"));
    }

    @Test
    public void createOrderWithLoginNoIngredientsShouldBePossibleStatusCode(){
        createAndLoginUser();
        String[] emptyList = new String[0];
        CreateOrderBody createOrderBody = new CreateOrderBody(emptyList);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertEquals(SC_BAD_REQUEST, orderResponse.statusCode());

        deleteUserRequest(userToken, authUserApiPath);
    }

    @Test
    public void createOrderWithoutLoginNoIngredientsShouldBePossibleBody(){
        createAndLoginUser();
        String[] emptyList = new String[0];
        CreateOrderBody createOrderBody = new CreateOrderBody(emptyList);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertFalse(orderResponse.path("success"));
        assertEquals(NO_INGREDIENTS_MESSAGE, orderResponse.path("message"));

        deleteUserRequest(userToken, authUserApiPath);
    }

    @Test
    public void createOrderWithLoginInvalidIngredientsHashShouldFailStatusCode(){
        createAndLoginUser();
        String[] invalidHashList = {"this", "hashes", "don't", "exist"};
        CreateOrderBody createOrderBody = new CreateOrderBody(invalidHashList);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertEquals(SC_INTERNAL_SERVER_ERROR, orderResponse.statusCode());

        deleteUserRequest(userToken, authUserApiPath);
    }

    @Test
    public void createOrderWithoutLoginInvalidIngredientsHashShouldFailBody(){
        createAndLoginUser();
        String[] invalidHashList = {"this", "hashes", "don't", "exist"};
        CreateOrderBody createOrderBody = new CreateOrderBody(invalidHashList);

        orderResponse = createOrderRequest(createOrderBody, ordersApiPath, userToken);
        assertNotNull(orderResponse.asString());

        deleteUserRequest(userToken, authUserApiPath);
    }
}
