import API.UserData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PatchUserInfoTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.USER_ACTION;
    @Test
    public void successPatchUserInfoTest(){
        String patchEmail = Email.generate();
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        UserData regUser = regResponse
                .body().jsonPath().getObject("user", UserData.class);

        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("email", patchEmail)
                .multiPart("password", data.PATCH_PASSWORD)
                .multiPart("firstName", data.FIRST_NAME)
                .multiPart("lastName", data.LAST_NAME)
                .multiPart("file", data.ICON_USER, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + regUser.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        UserData user = response.jsonPath().getObject("", UserData.class);
        Assert.assertEquals(statusCode, 200);
        Assert.assertNotNull(user.getAvatarPath());
        Assert.assertEquals(user.getId(), regUser.getId());
        Assert.assertEquals(user.getEmail(), patchEmail);
        Assert.assertEquals(user.getFirstName(), data.FIRST_NAME);
        Assert.assertEquals(user.getLastName(), data.LAST_NAME);
    }
    @Test
    public void negativePatchUserInfoWithInvalidEmailTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String invalidEmail = data.NO_VALID_EMAIL;
        Response regResponse = method.createAccount(email, password);

        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        UserData regUser = regResponse
                .body().jsonPath().getObject("user", UserData.class);

        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("email", invalidEmail)
                .multiPart("password", data.PATCH_PASSWORD)
                .multiPart("firstName", data.FIRST_NAME)
                .multiPart("lastName", data.LAST_NAME)
                .multiPart("file", data.ICON_USER, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + regUser.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativePatchUserInfoWithInvalidFileTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String patchEmail = Email.generate();
        Response regResponse = method.createAccount(email, password);

        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        UserData regUser = regResponse
                .body().jsonPath().getObject("user", UserData.class);

        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("email", patchEmail)
                .multiPart("password", data.PATCH_PASSWORD)
                .multiPart("firstName", data.FIRST_NAME)
                .multiPart("lastName", data.LAST_NAME)
                .multiPart("file", data.NO_VALID_FILE, "text/plain")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + regUser.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativePatchUserInfoWithInvalidUserIdTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        int invalidUserId = -1;
        String accessToken = method.createAccount(email, password)
                .body().jsonPath().getString("user.id");

        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("email", Email.generate())
                .multiPart("password", data.PATCH_PASSWORD)
                .multiPart("firstName", data.FIRST_NAME)
                .multiPart("lastName", data.LAST_NAME)
                .multiPart("file", data.ICON_USER, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + invalidUserId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
    @Test
    public void negativePatchUserInfoWithoutAuthorizationTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        UserData regUser = method.createAccount(email, password)
                .body().jsonPath().getObject("user", UserData.class);

        Response response = given()
                .multiPart("email", Email.generate())
                .multiPart("password", data.PATCH_PASSWORD)
                .multiPart("firstName", data.FIRST_NAME)
                .multiPart("lastName", data.LAST_NAME)
                .multiPart("file", data.ICON_USER, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + regUser.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}
