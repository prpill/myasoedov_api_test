import API.UserData;
import DTO.AuthenticationDto;
import DTO.CreateCommentDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static io.restassured.RestAssured.given;

public class Methods {
    RequestSpecification request = RestAssured.given();
    DataUsed data = new DataUsed();
    public Response createAccount(String email, String password){
        AuthenticationDto authDto = new AuthenticationDto(email,password);

        return request
                .when()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(EndPoints.SIGN_UP)
                .then()
                .extract().response();
    }
    public Response searchPost(int postId){
        return given()
                .when()
                .get(EndPoints.POST_ACTION + postId)
                .then()
                .extract().response();
    }
    public Response createPost(String accessToken){
        return given()
                .auth().oauth2(accessToken)
                .multiPart("title", data.TITLE)
                .multiPart("text", data.TEXT)
                .multiPart("file", data.ICON_NEWS, "image/png")
                .multiPart("tags[]", data.TAGS)
                .contentType(ContentType.MULTIPART)
                .when()
                .post(EndPoints.POST_ACTION)
                .then()
                .extract().response();
    }
    public Response createComment(String accessToken, int postId){
        CreateCommentDto commentDto = new CreateCommentDto(data.COMMENT, postId);
        return given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(commentDto)
                .when()
                .post(EndPoints.COMMENT_ACTION)
                .then()
                .extract().response();
    }

}
