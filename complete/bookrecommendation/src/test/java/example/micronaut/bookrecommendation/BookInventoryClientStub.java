package example.micronaut.bookrecommendation;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpResponse;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Single;

import javax.inject.Singleton;

@Requires(env = Environment.TEST)
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Single<HttpResponse<Boolean>> stock(String isbn) {
        if(isbn.equals("1491950358")) {
            return Single.just(HttpResponse.ok(Boolean.TRUE));

        } else if(isbn.equals("1680502395")) {
            return Single.just(HttpResponse.ok(Boolean.FALSE));
        }
        return Single.just(HttpResponse.notFound());
    }
}
