package example.micronaut.bookrecommendation;

import io.micronaut.http.HttpResponse;
import io.reactivex.Single;

public interface BookInventoryOperations {
    Single<HttpResponse<Boolean>> stock(String isbn);
}
