//tag::packageandimports[]
package example.micronaut.bookrecommendation;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

import javax.validation.constraints.NotBlank;
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8082") // <1>
//end::harcoded[]
*/
//tag::eureka[]
@Client(id = "bookinventory") // <1>
//end::eureka[]
//tag::clazz[]
public interface BookInventoryClient extends BookInventoryOperations {

    @Get("/books/stock/{isbn}")
    Maybe<Boolean> stock(@NotBlank String isbn);
}
//end::clazz[]
