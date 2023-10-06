import API.PostData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PatchNewsTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.POST_ACTION;
    @Test
    public void successPatchNewsTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.PATCH_TITLE)
                .multiPart("text", data.PATCH_TEXT)
                .multiPart("file", data.PATCH_ICON_NEWS, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + post.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        PostData patchPost = response.jsonPath().getObject("", PostData.class);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(patchPost.getId(), post.getId());
        Assert.assertEquals(patchPost.getTitle(), data.PATCH_TITLE);
        Assert.assertEquals(patchPost.getText(), data.PATCH_TEXT);
        Assert.assertNotNull(patchPost.getCoverPath());
        Assert.assertEquals(patchPost.getAuthorId(), post.getAuthorId());
    }
    @Test
    public void negativePatchNewsWithEmptyFieldsTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        String emptyTitle = "";
        String emptyText = "";
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", emptyTitle)
                .multiPart("text", emptyText)
                .multiPart("file", data.PATCH_ICON_NEWS, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + post.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativePatchNewsWithInvalidFileTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.PATCH_TITLE)
                .multiPart("text", data.PATCH_TEXT)
                .multiPart("file", data.NO_VALID_FILE, "text/plain")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + post.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativePatchNewsWithInvalidPostIdTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        int invalidPostId = -1;
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.PATCH_TITLE)
                .multiPart("text", data.PATCH_TEXT)
                .multiPart("file", data.PATCH_ICON_NEWS, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + invalidPostId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
    @Test
    public void negativePatchNewsWithoutAccessTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        method.createPost(accessToken).jsonPath().getObject("", PostData.class);

        Response regCreator = method.createAccount(Email.generate(), data.PASSWORD);
        String creatorToken = regCreator
                .body().jsonPath().getString("accessToken");
        PostData creatorPost = method.createPost(creatorToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.PATCH_TITLE)
                .multiPart("text", data.PATCH_TEXT)
                .multiPart("file", data.PATCH_ICON_NEWS, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + creatorPost.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 403);
    }
    @Test
    public void negativePatchNewsWithoutAuthorizationTest(){
        Response regResponse = method.createAccount(Email.generate(), data.PASSWORD);
        String accessToken = regResponse
                .body().jsonPath().getString("accessToken");
        PostData post = method.createPost(accessToken).jsonPath().getObject("", PostData.class);
        Response response = given()
                .multiPart("title", data.PATCH_TITLE)
                .multiPart("text", data.PATCH_TEXT)
                .multiPart("file", data.PATCH_ICON_NEWS, "image/png")
                .contentType(ContentType.MULTIPART)
                .when()
                .patch(path + post.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}
