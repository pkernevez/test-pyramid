package ch.octo.blog.transport.journeybooking;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * End-to-end testing : we call the API through http and expect it to do its job with other components (other APIs, DB, ...)
 */
public class JourneyBookingE2ET {

    private static final String LAUSANNE = "Lausanne";
    private static final String GENEVE = "Genève";

    @Before
    public void setup() {
        // TODO : extract config to test against an integration server
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/journeys";
    }

    @Test
    public void lookup_shouldCallLookupServiceAndTransportAPI() {
        given()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .queryParam("from", LAUSANNE)
                .queryParam("to", GENEVE)
        .when()
                .get("/search")
        .then()
                .statusCode(200)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body("connections.from.station.name", everyItem(equalTo(LAUSANNE)))
                .body("connections.to.station.name", everyItem(equalTo(GENEVE)));
    }

    @Test
    public void getAllJourneys_shouldQueryDatabase() {
        given()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
                .get()
        .then()
                .statusCode(200)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
