import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    name "should_provide_connections"
    request {
        method('GET')
        url('/connections') {
            queryParameters {
                parameter('from', 'Lausanne')
                parameter('to', 'Zurich')
            }
        }
    }
    response {
        status 200
        headers {
            header 'Content-Type' : MediaType.APPLICATION_JSON_UTF8_VALUE
        }
        body(file("connections_response.json"))
    }
}