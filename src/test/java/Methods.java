import DTO.AuthenticationDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Methods {
    RequestSpecification request = RestAssured.given();
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
}
