# currency-rates

Simple test app using https://exchangeratesapi.io. Done with Spring Boot. Requires an api key to be supplied like this: `./gradlew bootRun --args=--key=<my key>`.

The running app can be viewed at http://localhost:8080/

Example conf included for AWS Beanstalk. Also an example Grafana telemetry conf is there but commented out since no-one is listening.

Very simplistic UI with limited error handling. Basically just alerts when the backend produces an error. No client side input validation, etc.
