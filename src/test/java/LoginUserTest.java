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
    final static String authRegisterApiPath = "/api/auth/register";
    final static String authUserApiPath = "/api/auth/user";
    final static String authLoginApiPath = "/api/auth/login";
    final String INCORRECT_CREDENTIALS_MESSAGE = "email or password are incorrect";

    String name = "uniqueName";
    String email = "uniqueEmail@yandex.ru";
    String password = "uniquePassword";

    @Before
    public void createUser(){
        WholeUserBody user = new WholeUserBody(email, password, name);

        createUserRequest(user, authRegisterApiPath).statusCode();
    }

    @Test
    public void userLoginShouldBePossibleStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void userLoginShouldBePossibleBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, password);

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
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectEmailLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectPasswordLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userIncorrectCredentialsLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("email", "password");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoEmailLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoEmailLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", password);

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoPasswordLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoPasswordLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @Test
    public void userNoCredentialsLoginShouldFailStatusCode(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    public void userNoCredentialsLoginShouldFailBody(){
        Response response;
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody("", "");

        response = loginUserRequest(loginUserBody, authLoginApiPath);
        assertFalse(response.path("success"));
        assertEquals(INCORRECT_CREDENTIALS_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, password);
        String token;

        Response loginResponse = loginUserRequest(loginUserBody, authLoginApiPath);
        if(loginResponse.statusCode() == SC_OK) {
            token = loginResponse.path("accessToken")
                    .toString().replaceAll("Bearer ", "");
            assertEquals(SC_ACCEPTED, deleteUserRequest(token, authUserApiPath).statusCode());
        }
    }
}
