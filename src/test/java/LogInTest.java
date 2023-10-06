import API.UserData;
import DTO.AuthenticationDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogInTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    RequestSpecification request = RestAssured.given();
    String path = EndPoints.LOG_IN;
    @Test
    public void successLogInTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        Response signUpResponse = method.createAccount(email, password);
        UserData regUser = signUpResponse.body().jsonPath().getObject("user", UserData.class);

        AuthenticationDto authDto = new AuthenticationDto(email, password);
        Response response = request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(path)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        UserData user = response.body().jsonPath().getObject("user", UserData.class);
        String accessToken = response.body().jsonPath().getString("accessToken");

        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(user.getId(), regUser.getId());
        Assert.assertEquals(user.getEmail(), regUser.getEmail());
        Assert.assertNotNull(accessToken);
    }
    @Test
    public void negativeLogInWithInvalidEmailTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String invalidEmail = data.NO_VALID_EMAIL;
        method.createAccount(email, password);

        AuthenticationDto authDto = new AuthenticationDto(invalidEmail, password);
        Response response = request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(path)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
    @Test
    public void negativeLogInWithEmptyEmailTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String emptyEmail = "";
        method.createAccount(email, password);

        AuthenticationDto authDto = new AuthenticationDto(emptyEmail, password);
        Response response = request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(path)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
    @Test
    public void negativeLogInWithEmptyPasswordTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String emptyPassword = "";
        method.createAccount(email, password);

        AuthenticationDto authDto = new AuthenticationDto(email, emptyPassword);
        Response response = request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(path)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }

    @Test
    public void negativeLogInWithWithoutAuthorizationTest(){
        String email = Email.generate();
        String password = data.PASSWORD;

        AuthenticationDto authDto = new AuthenticationDto(email, password);
        Response response = request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(path)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}
