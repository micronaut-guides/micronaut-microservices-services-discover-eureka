package example.micronaut.bookcatalogue;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
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
    private static HttpClient client;

    @BeforeClass // <1>
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
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
        HttpRequest request = HttpRequest.GET("/books"); // <2>
        List books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class)); // <3>
        assertEquals(3, books.size());
        assertTrue(books.contains(new Book("1491950358", "Building Microservices")));
        assertTrue(books.contains(new Book("1680502395", "Release It!")));
    }
}
