package test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestfulBookerTest {

    private static String BASE_URL;
    private static String USERNAME;
    private static String PASSWORD;

    private static final String AUTH_ENDPOINT = "/auth";
    private static final String BOOKING_ENDPOINT = "/booking";

    private static Faker faker;

    @BeforeAll
    public static void setup() throws IOException {
        Properties props = new Properties();

        try (InputStream input = RestfulBookerTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Arquivo config.properties não encontrado no classpath");
            }
            props.load(input);
        }

        BASE_URL = props.getProperty("base.url");
        USERNAME = props.getProperty("username");
        PASSWORD = props.getProperty("password");

        RestAssured.baseURI = BASE_URL;
        faker = new Faker();
    }

    @Test
    public void deveRetornarStatus200QuandoPing() {
        given()
                .when()
                .get("/ping")
                .then()
                .statusCode(201);
    }

    @Test
    public void deveAutenticarComSucesso() {
        given()
                .contentType(ContentType.JSON)
                .body(gerarCorpoAutenticacao())
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void deveFalharAoAutenticarComCredenciaisInvalidas() {
        String corpoInvalido = """
            {
                "username": "usuarioErrado",
                "password": "senhaErrada"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(corpoInvalido)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(200)
                .body("token", nullValue());
    }

    @Test
    public void deveCriarUmNovoBooking() {
        given()
                .contentType(ContentType.JSON)
                .body(gerarCorpoBooking())
                .when()
                .post(BOOKING_ENDPOINT)
                .then()
                .statusCode(200)
                .body("booking.firstname", notNullValue())
                .body("booking.lastname", notNullValue());
    }

    @Test
    public void deveFalharAoCriarBookingComCamposFaltando() {
        String corpoInvalido = """
        {
            "lastname": "Silva"
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(corpoInvalido)
                .when()
                .post(BOOKING_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }


    @Test
    public void deveListarTodosOsBookings() {
        given()
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(200)
                .body("bookingid", not(empty()));
    }

    @Test
    public void deveAtualizarBookingComToken() {
        int bookingId = criarBookingERetornarId();
        String token = autenticarERetornarToken();

        String corpoAtualizacao = """
            {
                "firstname" : "Atualizado",
                "lastname" : "Teste",
                "totalprice" : 250,
                "depositpaid" : true,
                "bookingdates" : {
                    "checkin" : "2025-09-10",
                    "checkout" : "2025-09-15"
                },
                "additionalneeds" : "Jantar"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(corpoAtualizacao)
                .when()
                .put(BOOKING_ENDPOINT + "/" + bookingId)
                .then()
                .statusCode(200)
                .body("lastname", equalTo("Teste"));
    }

    @Test
    public void deveFalharAoAtualizarBookingSemToken() {
        String corpoAtualizacao = """
            {
                "firstname" : "Atualizado"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(corpoAtualizacao)
                .when()
                .put(BOOKING_ENDPOINT + "/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void deveDeletarBookingComToken() {
        int bookingId = criarBookingERetornarId();
        String token = autenticarERetornarToken();

        given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .delete(BOOKING_ENDPOINT + "/" + bookingId)
                .then()
                .statusCode(201);
    }

    @Test
    public void deveFalharAoDeletarBookingSemToken() {
        int bookingId = criarBookingERetornarId();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(BOOKING_ENDPOINT + "/" + bookingId)
                .then()
                .statusCode(403);
    }

    @Test
    public void deveRetornar404ParaBookingInexistente() {
        given()
                .when()
                .get(BOOKING_ENDPOINT + "/99999999")
                .then()
                .statusCode(404);
    }

    // Métodos auxiliares

    private String gerarCorpoBooking() {
        return String.format("""
                {
                    "firstname" : "%s",
                    "lastname" : "%s",
                    "totalprice" : %d,
                    "depositpaid" : %b,
                    "bookingdates" : {
                        "checkin" : "2025-09-01",
                        "checkout" : "2025-09-10"
                    },
                    "additionalneeds" : "Café da manhã"
                }
                """,
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().numberBetween(100, 300),
                faker.bool().bool());
    }

    private String gerarCorpoAutenticacao() {
        return String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, USERNAME, PASSWORD);
    }

    private String autenticarERetornarToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(gerarCorpoAutenticacao())
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    private int criarBookingERetornarId() {
        String corpo = gerarCorpoBooking();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(corpo)
                .when()
                .post(BOOKING_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.path("bookingid");
    }
}
