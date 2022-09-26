import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.WholeUserBody;
import pojos.EmailPasswordUserBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateUserRequestGenerator.createUserRequest;
import static requestgenerators.DeleteUserRequestGenerator.deleteUserRequest;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class LoginUserTest {
    private final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    private final static String AUTH_USER_API_PATH = "/api/auth/user";
    private final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    private final static String INCORRECT_CREDENTIALS_MESSAGE = "email or password are incorrect";
    private final String EMAIL = "uniqueEmail@yandex.ru";
    private final String PASSWORD = "uniquePassword";

    @Before
    public void createUser(){
        String name = "uniqueName";
        WholeUserBody user = new WholeUserBody(EMAIL, PASSWORD, name);

        createUserRequest(user, AUTH_REGISTER_API_PATH).statusCode();
    }

    @Test
    public void userLoginShouldBePossibleStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void userLoginShouldBePossibleBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertTrue(response.path("success"));
        assertNotNull(response.path("accessToken"));
        assertNotNull(response.path("refreshToken"));
        assertNotNull(response.path("user.email"));
        assertNotNull(response.path("user.name"));
    }

    @Test
    public void userIncorrectEmailLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectEmailLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, "password");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, "password");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", "password");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", "password");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoEmailLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoEmailLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", PASSWORD);

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoPasswordLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, "");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoPasswordLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, "");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoCredentialsLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", "");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoCredentialsLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", "");

        response = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);
        String token;

        Response loginResponse = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
        if(loginResponse.statusCode() == SC_OK) {
            token = loginResponse.path("accessToken").toString();
            assertEquals(SC_ACCEPTED, deleteUserRequest(token, AUTH_USER_API_PATH).statusCode());
        }
    }
}
