package api_test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PetStoreTest {

    //успешное создание нового питомца
    @Test
    public void successPostAddNewPat() {
        String bodyJson = """
                {
                  "id": 1,
                  "category": {
                    "id": 2,
                    "name": "dog"
                  },
                  "name": "Paulina",
                  "photoUrls": [
                    "string"
                  ],
                  "tags": [
                    {
                      "id": 1,
                      "name": "Bulldog French"
                    }
                  ],
                  "status": "available"
                }""";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bodyJson)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        String jSonControl = response.asString();
        Assertions.assertTrue(jSonControl.contains("Paulina"));

    }

    //неуспешный поиск питомца по несуществующему id.
    @Test
    public void unsuccessfulGetFindPetById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://petstore.swagger.io/v2/pet/-1")
                .then()
                .statusCode(404).extract().response();
        String jSonControl = response.asString();
        Assertions.assertTrue(jSonControl.contains("Pet not found"));
    }

    //успешное внесение изменений питомца по id.
    @Test
    public void successPutUpdateExistingPet() {
        String bodyJson = """
                {
                  "id": 0,
                  "category": {
                    "id": 0,
                    "name": "string"
                  },
                  "name": "doggie15",
                  "photoUrls": [
                    "string"
                  ],
                  "tags": [
                    {
                      "id": 0,
                      "name": "string"
                    }
                  ],
                  "status": "available"
                }""";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bodyJson)
                .when()
                .put("https://petstore.swagger.io/v2/pet")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        String jSonControl = response.asString();
        Assertions.assertTrue(jSonControl.contains("doggie15"));
    }

    //неуспешная попытка удаления питомца, с несуществующим id.
    @Test
    public void unsuccessfulDeletePetById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("https://petstore.swagger.io/v2/pet/-1")
                .then()
                .statusCode(404)
                .log().body()
                .extract().response();
        String jSonControl = response.asString();
        Assertions.assertEquals(jSonControl, "");
    }
}

