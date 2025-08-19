package org.example.testapi;

public class BookingFactory {

    public static String gerarCorpoAutenticacao(String username, String password) {
        return String.format("""
        {
            "username": "%s",
            "password": "%s"
        }
        """, username, password);
    }

    public static String gerarCorpoBooking() {
        return """
        {
            "firstname": "Gabriel",
            "lastname": "Testador",
            "totalprice": 150,
            "depositpaid": true,
            "bookingdates": {
                "checkin": "2025-08-01",
                "checkout": "2025-08-10"
            },
            "additionalneeds": "Café da manhã"
        }
        """;
    }
}
