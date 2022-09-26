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
    final static String INGREDIENTS_API_PATH = "/api/ingredients";
    final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    final static String AUTH_USER_API_PATH = "/api/auth/user";
    final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    final static String ORDERS_API_PATH = "/api/orders";
    final static String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
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

        createUserRequest(userBody, AUTH_REGISTER_API_PATH);
        userResponse = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        userToken = userResponse.path("accessToken")
                .toString().replaceAll("Bearer ", "");
    }

    private void createIngredientsList(){
        ingredients = getIngredientsHashList(INGREDIENTS_API_PATH);
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

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertEquals(SC_OK, orderResponse.statusCode());

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void createOrderWithLoginValidIngredientsShouldBePossibleBody(){
        createAndLoginUser();
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertTrue(orderResponse.path("success"));
        assertNotNull(orderResponse.path("name"));
        assertNotNull(orderResponse.path("order.number"));

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void createOrderWithoutLoginValidIngredientsShouldBePossibleStatusCode(){
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, "");
        assertEquals(SC_OK, orderResponse.statusCode());
    }

    @Test
    public void createOrderWithoutLoginValidIngredientsShouldBePossibleBody(){
        createIngredientsList();
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, "");
        assertTrue(orderResponse.path("success"));
        assertNotNull(orderResponse.path("name"));
        assertNotNull(orderResponse.path("order.number"));
    }

    @Test
    public void createOrderWithLoginNoIngredientsShouldBePossibleStatusCode(){
        createAndLoginUser();
        String[] emptyList = new String[0];
        CreateOrderBody createOrderBody = new CreateOrderBody(emptyList);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertEquals(SC_BAD_REQUEST, orderResponse.statusCode());

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void createOrderWithoutLoginNoIngredientsShouldBePossibleBody(){
        createAndLoginUser();
        String[] emptyList = new String[0];
        CreateOrderBody createOrderBody = new CreateOrderBody(emptyList);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertFalse(orderResponse.path("success"));
        assertEquals(NO_INGREDIENTS_MESSAGE, orderResponse.path("message"));

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void createOrderWithLoginInvalidIngredientsHashShouldFailStatusCode(){
        createAndLoginUser();
        String[] invalidHashList = {"this", "hashes", "don't", "exist"};
        CreateOrderBody createOrderBody = new CreateOrderBody(invalidHashList);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertEquals(SC_INTERNAL_SERVER_ERROR, orderResponse.statusCode());

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void createOrderWithoutLoginInvalidIngredientsHashShouldFailBody(){
        createAndLoginUser();
        String[] invalidHashList = {"this", "hashes", "don't", "exist"};
        CreateOrderBody createOrderBody = new CreateOrderBody(invalidHashList);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
        assertNotNull(orderResponse.asString());

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }
}
