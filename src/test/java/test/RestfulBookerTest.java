package test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestfulBookerTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void deveVerificarSeApiEstaAtiva() {
        given()
                .when()
                .get("/ping")
                .then()
                .statusCode(201);
    }

    @Test
    public void deveAutenticarComSucesso() {
        String corpoAutenticacao = "{ \"username\": \"admin\", \"password\": \"password123\" }";

        given()
                .contentType(ContentType.JSON)
                .body(corpoAutenticacao)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void deveCriarUmNovoBooking() {
        String corpoRequisicao = """
            {
                "firstname" : "Gabriel",
                "lastname" : "Severo",
                "totalprice" : 150,
                "depositpaid" : true,
                "bookingdates" : {
                    "checkin" : "2025-09-01",
                    "checkout" : "2025-09-10"
                },
                "additionalneeds" : "Café da manhã"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(corpoRequisicao)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("Gabriel"))
                .body("booking.lastname", equalTo("Severo"));
    }

    @Test
    public void deveListarTodosOsBookings() {
        given()
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", not(empty()));
    }

    @Test
    public void deveCriarEexcluirUmBookingComToken() {
        String corpoRequisicao = """
            {
                "firstname" : "Gabriel",
                "lastname" : "Severino",
                "totalprice" : 200,
                "depositpaid" : false,
                "bookingdates" : {
                    "checkin" : "2025-08-10",
                    "checkout" : "2025-08-15"
                },
                "additionalneeds" : "Nenhuma"
            }
        """;

        Response resposta = given()
                .contentType(ContentType.JSON)
                .body(corpoRequisicao)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().response();

        int bookingId = resposta.path("bookingid");

        String corpoAutenticacao = "{ \"username\": \"admin\", \"password\": \"password123\" }";
        String token = given()
                .contentType(ContentType.JSON)
                .body(corpoAutenticacao)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");

        given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(anyOf(is(200), is(201)));
    }

    @Test
    public void deveAtualizarBookingComToken() {
        String corpoCriacao = """
            {
                "firstname" : "Maria",
                "lastname" : "Oliveira",
                "totalprice" : 180,
                "depositpaid" : true,
                "bookingdates" : {
                    "checkin" : "2025-08-10",
                    "checkout" : "2025-08-15"
                },
                "additionalneeds" : "Almoço"
            }
        """;

        Response resposta = given()
                .contentType(ContentType.JSON)
                .body(corpoCriacao)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().response();

        int bookingId = resposta.path("bookingid");

        String corpoAutenticacao = "{ \"username\": \"admin\", \"password\": \"password123\" }";
        String token = given()
                .contentType(ContentType.JSON)
                .body(corpoAutenticacao)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");

        String corpoAtualizacao = """
            {
                "firstname" : "Maria",
                "lastname" : "Pereira",
                "totalprice" : 180,
                "depositpaid" : true,
                "bookingdates" : {
                    "checkin" : "2025-08-10",
                    "checkout" : "2025-08-15"
                },
                "additionalneeds" : "Almoço"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(corpoAtualizacao)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("lastname", equalTo("Pereira"));
    }
//FALHAS---------------------------------------------------------------------------------------
    @Test
    public void deveFalharAoAutenticarComCredenciaisInvalidas() {
        String corpoInvalido = "{ \"username\": \"usuarioErrado\", \"password\": \"senhaErrada\" }";

        given()
                .contentType(ContentType.JSON)
                .body(corpoInvalido)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", nullValue());
    }

    @Test
    public void deveFalharAoCriarBookingComPayloadInvalido() {
        String corpoInvalido = "{ \"firstname\": 123 }";

        given()
                .contentType(ContentType.JSON)
                .body(corpoInvalido)
                .when()
                .post("/booking")
                .then()
                .statusCode(500);
    }


    @Test
    public void deveFalharAoAtualizarBookingSemToken() {
        String corpoAtualizacao = "{ \"firstname\": \"Atualizado\" }";

        given()
                .contentType(ContentType.JSON)
                .body(corpoAtualizacao)
                .when()
                .put("/booking/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void deveRetornarNotFoundParaBookingInexistente() {
        given()
                .when()
                .get("/booking/999999")
                .then()
                .statusCode(404);
    }
}



