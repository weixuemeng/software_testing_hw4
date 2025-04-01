import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.path.json.JsonPath.from;
import static java.lang.reflect.Array.get;
import org.junit.jupiter.api.Test;

import java.net.ResponseCache;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JsonplaceholderRestAPITest {
    /*
    [test 1]: when a GET request is sent to /albums endpoint, verify that an entry with the title "omnis laborum odio" exists.
[test 2]: when a GET request is sent to /comments endpoint, verify that the returned JSON response contains at least 200 comments.
[test 3]: when a GET request is sent to /users endpoint, verify that a user named "Ervin Howell" with username "Antonette" and zipcode of "90566-7771" exists.
[test 4]: when a GET request is sent to /comments endpoint, verify that there are comments from people whose email address end in .biz.
     */
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    @Test
    public void testGetEndpointTitle() {
        given().
                baseUri(BASE_URL).
                when().
                get("/albums").
                then().
                body("title",hasItem("omnis laborum odio"));
    }
    @Test
    public void testCommentsLength() {
        String response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/comments")
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        int commentCount = jsonPath.getList("$").size(); // "$" means the root JSON array
        assertTrue(commentCount>=200);

        List<Map<String, Object>> products = given().
                baseUri(BASE_URL).
                when().
                get("/comments").as(new TypeRef<List<Map<String, Object>>>() {});
        assertTrue(products.size() >=200);


    }
    @Test
    public void testUser() {

        String response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/users")
                .asString();
        List<String> matchedUsersName = from(response).getList(
                "findAll { it.name == 'Ervin Howell' && it.username == 'Antonette' && it.address.zipcode == '90566-7771' }.name"
        );
        assertTrue(matchedUsersName.size()==1);
        System.out.println(matchedUsersName.get(0));

    }

    @Test
    public void testCommentsEmail() {

        String response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/comments")
                .asString();
        List<String> matchEmailEnding = from(response).getList(
                "findAll { it.email.endsWith('.biz') }.name"
        );
        assertTrue(matchEmailEnding.size()>=1);

    }

    @Test
    public void testPost(){
        Map<String, Object> requestBody = Map.of(
                "userId", 11,
                "id", 101,
                "title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "body", "quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"
        );
        RestAssured
                .given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201).
                body("userId", equalTo(11)).
                body("id", equalTo(101)).
                body("title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")).
                body("body", equalTo("quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"))
        ;

    }

}
