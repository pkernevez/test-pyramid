# The test pyramid in java / spring boot

This repo is linked to a series of articles published on the [OCTO blog](https://blog.octo.com) (in french).

To setup the whole environment amd execute all tests:

You should have a docker repository running on your desktop :
`docker run -d -p 5000:5000 --restart always --name my-registry registry:latest`

`mvn clean install` will build the project and launch unit tests, component tests and contract tests. 
It will also build the docker images of each module.

`docker-compose up` will start a postgresql database and use the built images to start both the connection-lookup service and journey-booking.

`mvn clean verify -f integration-test/pom.xml` will execute the integration tests and end-to-end tests on the on the freshly built environment.
