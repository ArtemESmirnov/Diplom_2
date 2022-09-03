import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.CreateUserBody;
import pojos.LoginUserBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
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

    @Before
    public void setUp(){
        baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void createUserShouldBePossibleStatusCode(){
        CreateUserBody user = new CreateUserBody(email, password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    public void createUserShouldBePossibleBody(){
        CreateUserBody user = new CreateUserBody(email, password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertTrue(response.path("success"));
        assertNotNull(response.path("accessToken"));
        assertNotNull(response.path("refreshToken"));
        assertNotNull(response.path("user.email"));
        assertNotNull(response.path("user.name"));
    }

    @Test
    public void createSecondEqualUserShouldFailStatusCode(){
        CreateUserBody user1 = new CreateUserBody(email, password, name);
        CreateUserBody user2 = new CreateUserBody(email, password, name);

        createUserRequest(user1, authRegisterApiPath);
        response = createUserRequest(user2, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createSecondEqualUserShouldFailBody(){
        CreateUserBody user1 = new CreateUserBody(email, password, name);
        CreateUserBody user2 = new CreateUserBody(email, password, name);

        createUserRequest(user1, authRegisterApiPath);
        response = createUserRequest(user2, authRegisterApiPath);
        assertEquals(EQUAL_LOGINS_RESPONSE_STRING_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutNameShouldFailStatusCode(){
        CreateUserBody user = new CreateUserBody(email, password, "");

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutNameShouldFailBody(){
        CreateUserBody user = new CreateUserBody(email, password, "");

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutEmailShouldFailStatusCode(){
        CreateUserBody user = new CreateUserBody("", password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutEmailShouldFailBody(){
        CreateUserBody user = new CreateUserBody("", password, name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @Test
    public void createUserWithoutPasswordShouldFailStatusCode(){
        CreateUserBody user = new CreateUserBody(email, "", name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void createUserWithoutPasswordShouldFailBody(){
        CreateUserBody user = new CreateUserBody(email, "", name);

        response = createUserRequest(user, authRegisterApiPath);
        assertEquals(EMPTY_REQUIRED_FIELD_MESSAGE, response.path("message"));
    }

    @After
    public void deleteUser(){
        if(response.statusCode() == SC_OK || response.statusCode() == SC_FORBIDDEN){
            LoginUserBody loginUserBody = new LoginUserBody(email, password);
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
