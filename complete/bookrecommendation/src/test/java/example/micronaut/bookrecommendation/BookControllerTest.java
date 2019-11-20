package example.micronaut.bookrecommendation;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class BookControllerTest {

    @Inject
    @Client("/")
    RxStreamingHttpClient client;

    @Test
    public void testRetrieveBooks() throws Exception {
        Flowable<BookRecommendation> books = client.jsonStream(HttpRequest.GET("/books"), BookRecommendation.class);
        assertEquals(1, books.toList().blockingGet().size());
        assertEquals("Building Microservices", books.toList().blockingGet().get(0).getName());
    }
}
