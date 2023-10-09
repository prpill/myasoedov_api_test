import API.PostData;
import API.UserData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;


public class CreateNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
    @Epic(value = "Working with posts")
    @Feature(value = "Successful post creation")
    @Test
    public void successCreateNewsTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        UserData regUser = regResponse
                .body().jsonPath().getObject("user", UserData.class);
        Response response = method.createPost(accessToken);
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        PostData post = response.jsonPath().getObject("", PostData.class);
        Assert.assertEquals(statusCode, 201);
        Assert.assertNotNull(post.getId());
        Assert.assertEquals(post.getTitle(), data.TITLE);
        Assert.assertEquals(post.getText(), data.TEXT);
        Assert.assertEquals(post.getAuthorId(), regUser.getId());
        Assert.assertNotNull(post.getCoverPath());
    }
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - creating a post with empty title and text fields")
    @Test
    public void negativeCreateNewsWithEmptyFieldsTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        String emptyTitle = "";
        String emptyText = "";
        Response regResponse = method.createAccount(email, password);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", emptyTitle)
                .multiPart("text", emptyText)
                .multiPart("file", data.ICON_NEWS, "image/png")
                .multiPart("tags[]", data.TAGS)
                .contentType(ContentType.MULTIPART)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - creating a post with the wrong file format")
    @Test
    public void negativeCreateNewsWithInvalidFileTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        Response regResponse = method.createAccount(email, password);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.TITLE)
                .multiPart("text", data.TEXT)
                .multiPart("file", data.NO_VALID_FILE, "text/plain")
                .multiPart("tags[]", data.TAGS)
                .contentType(ContentType.MULTIPART)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Epic(value = "Working with posts")
    @Feature(value = "Negative test - creating a post without authorization")
    @Test
    public void negativeCreateNewsWithoutAuthorizationTest() {
        Response response = given()
                .multiPart("title", data.TITLE)
                .multiPart("text", data.TEXT)
                .multiPart("file", data.ICON_NEWS, "image/png")
                .multiPart("tags[]", data.TAGS)
                .contentType(ContentType.MULTIPART)
                .when()
                .post(path)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}
