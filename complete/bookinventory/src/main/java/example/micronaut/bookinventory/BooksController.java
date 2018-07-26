package example.micronaut.bookinventory;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated // <1>
@Controller("/books") // <2>
public class BooksController {

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/stock/{isbn}") // <4>
    public HttpResponse<Boolean> stock(@NotNull @NotBlank String isbn) {
        Optional<BookInventory> bookInventoryOptional = bookInventoryByIsbn(isbn);
        if (!bookInventoryOptional.isPresent()) {
           return HttpResponse.notFound();
        }
        BookInventory bookInventory = bookInventoryOptional.get();
        return HttpResponse.ok(bookInventory.getStock() > 0 ? Boolean.TRUE : Boolean.FALSE);
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if(isbn.equals("1491950358")) {
            return Optional.of(new BookInventory(isbn, 4));

        } else if(isbn.equals("1680502395")) {
            return Optional.of(new BookInventory(isbn, 0));
        }
        return Optional.empty();
    }
}
