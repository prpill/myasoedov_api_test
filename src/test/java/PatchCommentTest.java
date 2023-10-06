import API.CommentData;
import DTO.CreateCommentDto;
import DTO.PatchCommentDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PatchCommentTest {
    Methods method = new Methods();
    DataUsed data = new DataUsed();
    String path = EndPoints.COMMENT_ACTION;
    @Test
    public void successPatchCommentTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        CommentData comment = method.createComment(accessToken,postId)
                .jsonPath().getObject("", CommentData.class);
        PatchCommentDto commentDto = new PatchCommentDto(data.PATCH_COMMENT);
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .patch(path + comment.getId())
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        CommentData patchComment = response.jsonPath().getObject("", CommentData.class);
        Assert.assertEquals(patchComment.getId(), comment.getId());
        Assert.assertEquals(patchComment.getAuthorId(), comment.getAuthorId());
        Assert.assertEquals(patchComment.getPostId(), comment.getPostId());
        Assert.assertEquals(patchComment.getText(), data.PATCH_COMMENT);
    }
    @Test
    public void negativePatchCommentWithEmptyTextTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        int commentId = method.createComment(accessToken,postId)
                .jsonPath().getInt("id");
        String emptyString = "";
        PatchCommentDto commentDto = new PatchCommentDto(emptyString);
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .patch(path + commentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
    @Test
    public void negativePatchCommentWithInvalidCommentIdTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        PatchCommentDto commentDto = new PatchCommentDto(data.PATCH_COMMENT);
        int invalidCommentId = -1;
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .patch(path + invalidCommentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
    @Test
    public void negativePatchCommentWithoutAccessTest(){
        String creatorToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(creatorToken).jsonPath().getInt("id");
        int commentId = method.createComment(creatorToken,postId)
                .jsonPath().getInt("id");

        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        PatchCommentDto commentDto = new PatchCommentDto(data.PATCH_COMMENT);
        Response response = given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .patch(path + commentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 403);
    }
    @Test
    public void negativePatchCommentWithoutAuthorizationTest(){
        String accessToken = method.createAccount(Email.generate(), data.PASSWORD)
                .body().jsonPath().getString("accessToken");
        int postId = method.createPost(accessToken).jsonPath().getInt("id");
        int commentId = method.createComment(accessToken,postId)
                .jsonPath().getInt("id");
        PatchCommentDto commentDto = new PatchCommentDto(data.PATCH_COMMENT);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .patch(path + commentId)
                .then()
                .extract().response();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);

    }
}
