import API.PostData;
import API.UserData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
    @Test
    public void successCreateNewsTest(){
        String email = Email.generate();
        String password = data.PASSWORD;
        Response regResponse = method.createAccount(email, password);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        UserData regUser = regResponse
                .body().jsonPath().getObject("user", UserData.class);

        Response response = given()
                .auth().oauth2(accessToken)
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
        PostData post = response.jsonPath().getObject("", PostData.class);
        Assert.assertEquals(statusCode, 201);
        Assert.assertNotNull(post.getId());
        Assert.assertEquals(post.getTitle(), data.TITLE);
        Assert.assertEquals(post.getText(), data.TEXT);
        Assert.assertEquals(post.getAuthorId(), regUser.getId());
        Assert.assertNotNull(post.getCoverPath());
    }
    @Test
    public void negativeCreateNewsWithEmptyFieldsTest(){

    }
    @Test
    public void negativeCreateNewsWithInvalidFileTest(){

    }
    @Test
    public void negativeCreateNewsWithoutAuthorizationTest(){

    }
}
