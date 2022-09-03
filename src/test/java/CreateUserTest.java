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
    final static String authRegisterApiPath = "/api/auth/register";
    final static String authUserApiPath = "/api/auth/user";
    final static String authLoginApiPath = "/api/auth/login";
    final String EQUAL_LOGINS_RESPONSE_STRING_MESSAGE = "User already exists";
    final String EMPTY_REQUIRED_FIELD_MESSAGE = "Email, password and name are required fields";

    String name = "uniqueName";
    String email = "uniqueEmail@yandex.ru";
    String password = "uniquePassword";
    Response response;

    @Test
    public void createUserShouldBePossibleStatusCode(){
        WholeUserBody user = new WholeUserBody(email, password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void createUserShouldBePossibleBody(){
        WholeUserBody user = new WholeUserBody(email, password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertTrue(response.path("success"));
        assertNotNull(response.path("accessToken"));
        assertNotNull(response.path("refreshToken"));
        assertNotNull(response.path("user.email"));
        assertNotNull(response.path("user.name"));
    }

    @Test
    public void createSecondEqualUserShouldFailStatusCode(){
        WholeUserBody user1 = new WholeUserBody(email, password, name);
        WholeUserBody user2 = new WholeUserBody(email, password, name);

        createUserRequest(user1, authRegisterApiPath);
        response = createUserRequest(user2, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createSecondEqualUserShouldFailBody(){
        WholeUserBody user1 = new WholeUserBody(email, password, name);
        WholeUserBody user2 = new WholeUserBody(email, password, name);

        createUserRequest(user1, authRegisterApiPath);
        response = createUserRequest(user2, authRegisterApiPath);
        assertFalse(response.path("success"));
        assertEquals(EQUAL_LOGINS_RESPONSE_STRING_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutNameShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody(email, password, "");

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutNameShouldFailBody(){
        WholeUserBody user = new WholeUserBody(email, password, "");

        response = createUserRequest(user, authRegisterApiPath);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutEmailShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody("", password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutEmailShouldFailBody(){
        WholeUserBody user = new WholeUserBody("", password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutPasswordShouldFailStatusCode(){
        WholeUserBody user = new WholeUserBody(email, "", name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutPasswordShouldFailBody(){
        WholeUserBody user = new WholeUserBody(email, "", name);

        response = createUserRequest(user, authRegisterApiPath);
        assertFalse(response.path("success"));
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        if(response.statusCode() == SC_OK || response.statusCode() == SC_FORBIDDEN){
            EmailPasswordUserBody loginUserBody = new EmailPasswordUserBody(email, password);
            String token;

            Response loginResponse = loginUserRequest(loginUserBody, authLoginApiPath);
            if(loginResponse.statusCode() == SC_OK){
                token = loginResponse.path("accessToken")
                        .toString().replaceAll("Bearer ", "");
                assertEquals(SC_ACCEPTED, deleteUserRequest(token, authUserApiPath).statusCode());
            }
        }
    }
}
