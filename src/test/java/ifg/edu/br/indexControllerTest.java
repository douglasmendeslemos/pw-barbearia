package ifg.edu.br;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class IndexControllerTest {

    @Test
    void deveCarregarTelaDeLogin() {
        given()
                .when().get("/login")
                .then()
                .statusCode(200)
                .body(containsString("BARBEARIA"));
    }
}
