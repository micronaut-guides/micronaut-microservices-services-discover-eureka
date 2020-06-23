package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientException
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification

class AcceptanceSpec extends Specification {

    static final String CATALOGUE_URL = 'http://localhost:8081'
    static final String RECOMMENDATION_URL = 'http://localhost:8080'
    static final String INVENTORY_URL = 'http://localhost:8082'

    @Shared
    HttpClient httpClient = HttpClient.create(new URL(RECOMMENDATION_URL))

    @Shared
    BlockingHttpClient client = httpClient.toBlocking()

    @Requires( {
        Closure isUp = { client, url ->
            String microservicesUrl = url.endsWith('/health') ? url : "${url}/health"
            try {
                StatusResponse statusResponse = client.retrieve(HttpRequest.GET(microservicesUrl), StatusResponse)
                if ( statusResponse.status == 'UP' ) {
                    return true
                }
            } catch (HttpClientException e) {
                println "HTTP Client exception for $microservicesUrl $e.message"
            }
            return false
        }
        BlockingHttpClient recommendationClient = HttpClient.create(new URL(RECOMMENDATION_URL)).toBlocking()
        BlockingHttpClient inventoryClient = HttpClient.create(new URL(INVENTORY_URL)).toBlocking()
        BlockingHttpClient catalogueClient = HttpClient.create(new URL(CATALOGUE_URL)).toBlocking()
        return isUp(recommendationClient, RECOMMENDATION_URL) && isUp(catalogueClient, CATALOGUE_URL) && isUp(inventoryClient, INVENTORY_URL)
    })
    def "verifies three microservices collaborate together"() {
        when:
        List<BookRecommendation> books = client.retrieve(HttpRequest.GET('/books'), Argument.listOf(BookRecommendation))

        then:
        books
        books.size() == 1
        books*.name.first() == "Building Microservices"
    }
}
