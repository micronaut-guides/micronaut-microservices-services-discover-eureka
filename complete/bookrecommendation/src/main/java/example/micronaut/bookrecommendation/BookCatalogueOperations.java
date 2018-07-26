package example.micronaut.bookrecommendation;

import io.reactivex.Flowable;

public interface BookCatalogueOperations {
    Flowable<Book> findAll();
}
