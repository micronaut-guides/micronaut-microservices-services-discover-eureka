package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@IgnoreIf({ System.getenv("TRAVIS") })
class AcceptanceSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run()

    @Shared
    @AutoCleanup
    RxHttpClient client = applicationContext.createBean(RxHttpClient, 'http://localhost:8080')

    def "verifies three microservices collaborate together with eureka service registration"() {

        when:
        PollingConditions conditions = new PollingConditions(timeout: 5)
        StatusResponse statusResponse = client.toBlocking().retrieve(HttpRequest.GET('http://localhost:8080/health'), StatusResponse)

        then:
        conditions.eventually {
            statusResponse.status == 'UP'
        }

        when:
        conditions = new PollingConditions(timeout: 5)
        statusResponse = client.toBlocking().retrieve(HttpRequest.GET('http://localhost:8081/health'), StatusResponse)

        then:
        conditions.eventually {
            statusResponse.status == 'UP'
        }

        when:
        conditions = new PollingConditions(timeout: 5)
        statusResponse = client.toBlocking().retrieve(HttpRequest.GET('http://localhost:8082/health'), StatusResponse)

        then:
        conditions.eventually {
            statusResponse.status == 'UP'
        }

        when:
        List<BookRecommendation> books = client.toBlocking().retrieve(HttpRequest.GET('/books'), Argument.of(List, BookRecommendation))

        then:
        books
        books*.name.first() == "Building Microservices"
    }
}
