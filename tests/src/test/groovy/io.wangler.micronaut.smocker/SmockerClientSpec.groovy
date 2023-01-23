package io.wangler.micronaut.smocker

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.MediaType.TEXT_PLAIN

@MicronautTest
class SmockerClientSpec extends Specification {

    @Inject
    SmockerClient smockerClient

    @Client("http://localhost:8080")
    @Inject
    HttpClient httpClient;

    void "Read the version"() {

        given:
        SmockerVersion version = smockerClient.healthcheck()

        expect:
        with(version) {
            appName() == 'smocker'
            buildVersion() == '0.18.2'
            buildCommit() == '3f1407b50778e57f651d1ae3d06d1398765d0805'
            buildDate() == '2022-05-05T11:09:56+0000'
        }
    }

    void "Count the amount of mocks in the default session"() {

        given:
        String sessionName = "Test-Session-1"

        when:
        smockerClient.reset()

        then:
        smockerClient.getSessions().isEmpty()

        when:
        smockerClient.startSession(sessionName)

        then: 'there is one session'
        List<SmockerSession> sessions = smockerClient.getSessions()
        sessions.size() == 1

        and:
        sessions.first().name() == sessionName

        and: 'there are no mocks'
        smockerClient.getMocks(sessions.first().id()).isEmpty()

        when:
        smockerClient.addMocks(false, [helloWorldMock('/foobar')])

        /*
        [
  {
    "request": {
      "path": {
        "matcher": "ShouldEqual",
        "value": "/foobar"
      },
      "method": {
        "matcher": "ShouldEqual",
        "value": "GET"
      },
      "headers": {
        "Accept": [
          {
            "matcher": "ShouldEqual",
            "value": "text/plain"
          }
        ]
      }
    },
    "response": {
      "body": "Hello World",
      "status": 200,
      "delay": {},
      "headers": {
        "CONTENT_TYPE": [
          "text/plain"
        ]
      }
    },
    "context": {},
    "state": {
      "id": "OIwqLvoVR",
      "times_count": 0,
      "locked": false,
      "creation_date": "2023-01-20T06:26:16.426008053Z"
    }
  }
]
         */
        then: 'there are no mocks'
        smockerClient.getMocks(sessions.first().id()).size() == 1
    }

    void "Add a mock and test the mocked response"() {

        given:
        final String PATH = "/hello"


        SmockerMock helloWorldMock = helloWorldMock(PATH)

        when:
        SmockerResponse res = smockerClient.addMocks(true, 'my-session',
                [
                        helloWorldMock
                ]
        )

        then:
        noExceptionThrown()

        and:
        res.message() == 'Mocks registered successfully'

        when:
        String response = httpClient.toBlocking().retrieve(io.micronaut.http.HttpRequest.GET(PATH).header(ACCEPT, TEXT_PLAIN))

        then:
        response == 'Hello World'
    }

    private SmockerMock helloWorldMock(String PATH) {
        new SmockerMock(
                new SmockerMock.Request('GET', ['Accept': TEXT_PLAIN], PATH),
                new SmockerMock.Response(
                        HttpStatus.OK.code,
                        [CONTENT_TYPE: TEXT_PLAIN],
                        "Hello World"
                )
        )
    }
}
