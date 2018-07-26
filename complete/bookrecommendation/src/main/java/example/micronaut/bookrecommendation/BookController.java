package example.micronaut.bookrecommendation;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;

@Controller("/books") // <1>
public class BookController {

    private final BookCatalogueOperations bookCatalogueOperations;
    private final BookInventoryOperations bookInventoryOperations;

    public BookController(BookCatalogueOperations bookCatalogueOperations,
                          BookInventoryOperations bookInventoryOperations) { // <2>
        this.bookCatalogueOperations = bookCatalogueOperations;
        this.bookInventoryOperations = bookInventoryOperations;
    }

    @Get("/") // <3>
    public Flowable<BookRecommendation> index() {
        return bookCatalogueOperations.findAll()
            .flatMapMaybe(b -> bookInventoryOperations.stock(b.getIsbn())
                          .onErrorResumeNext(throwable -> {
                            if (throwable instanceof HttpClientResponseException) {
                                HttpClientResponseException ex = (HttpClientResponseException) throwable;
                                if (ex.getResponse().getStatus().equals(HttpStatus.NOT_FOUND)) {
                                    return Single.just(HttpResponse.ok(Boolean.FALSE));
                                }
                            }
                            return Single.error(throwable);
                        })
                        .filter(HttpResponse::body)
                        .map(rsp -> b)
                ).map(book -> new BookRecommendation(book.getName()));
    }
}
