package example.micronaut.bookcatalogue;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Arrays;
import java.util.List;

@Controller("/books") // <1>
public class BooksController {

    @Get // <2>
    public List<Book> index() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices");
        Book releaseIt = new Book("1680502395", "Release It!");
        Book cidelivery = new Book("0321601912", "Continuous Delivery:");
        return Arrays.asList(buildingMicroservices, releaseIt, cidelivery);
    }
}
