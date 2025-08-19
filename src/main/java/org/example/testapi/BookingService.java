package org.example.testapi;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookingService {

    public static String autenticarERetornarToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(BookingFactory.gerarCorpoAutenticacao("admin", "password123"))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public static int criarBookingERetornarId() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(BookingFactory.gerarCorpoBooking())
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.path("bookingid");
    }
}
