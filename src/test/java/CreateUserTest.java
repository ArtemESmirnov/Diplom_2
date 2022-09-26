import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojos.WholeUserBody;
import pojos.EmailPasswordUserBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateUserRequestGenerator.createUserRequest;
import static requestgenerators.DeleteUserRequestGenerator.deleteUserRequest;
import static requestgenerators.LoginUserRequestGenerator.loginUserRequest;

public class CreateUserTest {
    private final static String AUTH_REGISTER_API_PATH = "/api/auth/register";
    private final static String AUTH_USER_API_PATH = "/api/auth/user";
    private final static String AUTH_LOGIN_API_PATH = "/api/auth/login";
    private final static String EQUAL_LOGINS_RESPONSE_STRING_MESSAGE = "User already exists";
    private final static String EMPTY_REQUIRED_FIELD_MESSAGE = "Email, password and name are required fields";

    private final String NAME = "uniqueName";
    private final String EMAIL = "uniqueEmail@yandex.ru";
    private final String PASSWORD = "uniquePassword";
    private Response response;

    @Test
    public void createUserShouldBePossibleStatusCode(){
        WholeUserBody user = new WholeUserBody(EMAIL, PASSWORD, NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void createUserShouldBePossibleBody(){
        WholeUserBody user = new WholeUserBody(EMAIL, PASSWORD, NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertTrue(response.path("success"));
        assertNotNull(response.path("accessToken"));
        assertNotNull(response.path("refreshToken"));
        assertNotNull(response.path("user.email"));
        assertNotNull(response.path("user.name"));
    }

    @Test
    public void createSecondEqualUserShouldFailStatusCode(){
        WholeUserBody user1 = new WholeUserBody(EMAIL, PASSWORD, NAME);
        WholeUserBody user2 = new WholeUserBody(EMAIL, PASSWORD, NAME);

        createUserRequest(user1, AUTH_REGISTER_API_PATH);
        response = createUserRequest(user2, AUTH_REGISTER_API_PATH);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createSecondEqualUserShouldFailBody(){
        WholeUserBody user1 = new WholeUserBody(EMAIL, PASSWORD, NAME);
        WholeUserBody user2 = new WholeUserBody(EMAIL, PASSWORD, NAME);

        createUserRequest(user1, AUTH_REGISTER_API_PATH);
        response = createUserRequest(user2, AUTH_REGISTER_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(EQUAL_LOGINS_RESPONSE_STRING_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutNameShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody(EMAIL, PASSWORD, "");

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutNameShouldFailBody(){
        WholeUserBody user = new WholeUserBody(EMAIL, PASSWORD, "");

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutEmailShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody("", PASSWORD, NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutEmailShouldFailBody(){
        WholeUserBody user = new WholeUserBody("", PASSWORD, NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutPasswordShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody(EMAIL, "", NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutPasswordShouldFailBody(){
        WholeUserBody user = new WholeUserBody(EMAIL, "", NAME);

        response = createUserRequest(user, AUTH_REGISTER_API_PATH);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        if(response.statusCode() == SC_OK || response.statusCode() == SC_FORBIDDEN){
            EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(EMAIL, PASSWORD);
            String token;

            Response loginResponse = loginUserRequest(loginUserBody, AUTH_LOGIN_API_PATH);
            if(loginResponse.statusCode() == SC_OK){
                token = loginResponse.path("accessToken").toString();
                assertEquals(SC_ACCEPTED, deleteUserRequest(token, AUTH_USER_API_PATH).statusCode());
            }
        }
    }
}
