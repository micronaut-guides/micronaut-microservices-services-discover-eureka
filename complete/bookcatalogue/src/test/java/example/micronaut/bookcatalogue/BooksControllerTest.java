package example.micronaut.bookcatalogue;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BooksControllerTest {
    private static EmbeddedServer server;
    private static RxStreamingHttpClient client;

    @BeforeClass // <1>
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(RxStreamingHttpClient.class, server.getURL());
    }

    @AfterClass // <1>
    public static void stopServer() {
        if(server != null) {
            server.stop();
        }
        if(client != null) {
            client.stop();
        }
    }

    @Test
    public void testRetrieveBooks() {
        List<Book> books = client.jsonStream(HttpRequest.GET("/books"), Book.class).toList().blockingGet();
        assertEquals(3, books.size());
        assertTrue(books.contains(new Book("1491950358", "Building Microservices")));
        assertTrue(books.contains(new Book("1680502395", "Release It!")));
    }
}
