import API.UserData;
import DTO.AuthenticationDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetUserInfoTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    RequestSpecification request = RestAssured.given();
    String path = EndPoints.USER_ACTION;
    @Test
    public void successGetUserInfoTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        UserData regUser = method.createAccount(email, password)
                .body().jsonPath().getObject("user", UserData.class);

        Response response = request
                .when()
                .get(path + regUser.getId())
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        UserData user = response.jsonPath().getObject("", UserData.class);
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(user.getId(), regUser.getId());
        Assert.assertEquals(user.getEmail(), regUser.getEmail());
    }
    @Test
    public void negativeGetUserInfoWithInvalidUserIdTest(){
        int invalidUserId = -1;

        Response response = request
                .when()
                .get(path + invalidUserId)
                .then()
                .extract().response();

        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
}
