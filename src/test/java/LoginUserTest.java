import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.CreateUserBody;
import pojos.LoginUserBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateUserRequestGenerator.createUserRequest;
import static requestgenerators.DeleteUserRequestGenerator.deleteUserRequest;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class LoginUserTest {
    final static String authRegisterApiPath = "/api/auth/register";
    final static String authUserApiPath = "/api/auth/user";
    final static String authLoginApiPath = "/api/auth/login";
    final String INCORRECT_CREDENTIALS_MESSAGE = "email or password are incorrect";

    String name = "uniqueName";
    String email = "uniqueEmail@yandex.ru";
    String password = "uniquePassword";

    @Before
    public void createUser(){
        CreateUserBody user = new CreateUserBody(email, password, name);

        assertEquals(SC_OK, createUserRequest(user, authRegisterApiPath).statusCode());
    }

    @Test
    public void userLoginShouldBePossibleStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void userLoginShouldBePossibleBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertTrue(response.path("success"));
        assertNotNull(response.path("accessToken"));
        assertNotNull(response.path("refreshToken"));
        assertNotNull(response.path("user.email"));
        assertNotNull(response.path("user.name"));
    }

    @Test
    public void userIncorrectEmailLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("email", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectEmailLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("email", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("email", "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("email", "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoEmailLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoEmailLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoPasswordLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoPasswordLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody(email, "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoCredentialsLoginShouldFailStatusCode(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("", "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoCredentialsLoginShouldFailBody(){
        Response response;
        LoginUserBody loginUserBody = new LoginUserBody("", "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        LoginUserBody loginUserBody = new LoginUserBody(email, password);
        String token;

        Response loginResponse = loginUserRequest(loginUserBody, authLoginApiPath);
        if(loginResponse.statusCode() == SC_OK) {
            token = loginResponse.path("accessToken")
                    .toString().replaceAll("Bearer ", "");
            assertEquals(SC_ACCEPTED, deleteUserRequest(token, authUserApiPath).statusCode());
        }
    }
}
