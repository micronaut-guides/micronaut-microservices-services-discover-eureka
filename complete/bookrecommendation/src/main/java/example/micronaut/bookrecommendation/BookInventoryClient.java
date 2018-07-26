//tag::packageandimports[]
package example.micronaut.bookrecommendation;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.Client;
import io.reactivex.Single;
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
    Single<HttpResponse<Boolean>> stock(String isbn);
}
//end::clazz[]
