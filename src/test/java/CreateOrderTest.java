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
    private final static String INGREDIENTS_API_PATH = "/api/ingredients";
    private final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    private final static String AUTH_USER_API_PATH = "/api/auth/user";
    private final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    private final static String ORDERS_API_PATH = "/api/orders";
    private final static String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    private final String NAME = "uniqueName";
    private final String EMAIL = "uniqueEmail@yandex.ru";
    private final String PASSWORD = "uniquePassword";
    private String[] ingredients;
    private String[] ingredientsToSend;
    private final Random random = new Random();
    private Response orderResponse;
    private Response userResponse;
    private String userToken;

    private void createAndLoginUser(){
        WholeUserBody userBody = new WholeUserBody(EMAIL, PASSWORD, NAME);
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);

        createUserRequest(userBody, AUTH_REGISTER_API_PATH);
        userResponse = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        userToken = userResponse.path("accessToken").toString();
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
