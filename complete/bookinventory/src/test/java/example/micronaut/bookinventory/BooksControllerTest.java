package example.micronaut.bookinventory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooksControllerTest {

    private static EmbeddedServer server;
    private static RxHttpClient rxHttpClient;

    @BeforeClass // <1>
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        rxHttpClient = server
                .getApplicationContext()
                .createBean(RxHttpClient.class, server.getURL());
    }

    @AfterClass // <1>
    public static void stopServer() {
        if(server != null) {
            server.stop();
        }
        if(rxHttpClient != null) {
            rxHttpClient.stop();
        }
    }

    @Test
    public void testBooksController() {
        boolean noExceptionThrown = true;
        HttpResponse<Boolean> rsp = null;
        try {
            rsp = rxHttpClient.toBlocking().exchange(HttpRequest.GET("/books/stock/1491950358"), Boolean.class);
        } catch (HttpClientResponseException e) {
            noExceptionThrown = false;
        }

        assertTrue(noExceptionThrown);
        assertEquals(rsp.status(), HttpStatus.OK);
        assertTrue(rsp.body());

        HttpClientResponseException ex = null;
        noExceptionThrown = true;
        try {
            rxHttpClient.toBlocking().exchange(HttpRequest.GET("/books/stock/XXXXX"), Boolean.class);
        } catch (HttpClientResponseException e) {
            noExceptionThrown = false;
            ex = e;
        }

        assertFalse(noExceptionThrown);

        HttpResponse response = ex.getResponse();

        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND);
    }
}
