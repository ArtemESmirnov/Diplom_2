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
import static requestgenerators.GetUserOrdersRequestGenerator.getUserOrdersRequest;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class GetUserOrdersTest {
    final static String INGREDIENTS_API_PATH = "/api/ingredients";
    final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    final static String AUTH_USER_API_PATH = "/api/auth/user";
    final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    final static String ORDERS_API_PATH = "/api/orders";
    final static String UNAUTHORIZED_MESSAGE = "You should be authorised";
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

    private void createOrder(){
        CreateOrderBody createOrderBody = new CreateOrderBody(ingredientsToSend);

        orderResponse = createOrderRequest(createOrderBody, ORDERS_API_PATH, userToken);
    }

    @Test
    public void getUserOrdersWithLoginShouldBePossibleStatusCode(){
        Response getUserOrdersResponse;

        createAndLoginUser();
        createIngredientsList();
        createOrder();

        getUserOrdersResponse = getUserOrdersRequest(userToken, ORDERS_API_PATH);
        assertEquals(SC_OK, getUserOrdersResponse.statusCode());

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void getUserOrdersWithLoginShouldBePossibleBody(){
        Response getUserOrdersResponse;

        createAndLoginUser();
        createIngredientsList();
        createOrder();

        getUserOrdersResponse = getUserOrdersRequest(userToken, ORDERS_API_PATH);
        assertTrue(getUserOrdersResponse.path("success"));
        assertNotNull(getUserOrdersResponse.path("orders"));
        assertNotNull(getUserOrdersResponse.path("total"));
        assertNotNull(getUserOrdersResponse.path("totalToday"));

        deleteUserRequest(userToken, AUTH_USER_API_PATH);
    }

    @Test
    public void getUserOrdersWithoutLoginShouldFailStatusCode(){
        Response getUserOrdersResponse;

        createIngredientsList();
        createOrder();

        getUserOrdersResponse = getUserOrdersRequest("", ORDERS_API_PATH);
        assertEquals(SC_UNAUTHORIZED, getUserOrdersResponse.statusCode());
    }

    @Test
    public void getUserOrdersWithoutLoginShouldFailBody(){
        Response getUserOrdersResponse;

        createIngredientsList();
        createOrder();

        getUserOrdersResponse = getUserOrdersRequest("", ORDERS_API_PATH);
        assertEquals(UNAUTHORIZED_MESSAGE, getUserOrdersResponse.path("message"));
    }
}
