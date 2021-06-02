# Recruiting Process Backend
A web application backend for handling a recruiting process.

## Setup
The project is a standard Maven based Java project built on Spring Boot.

### Testing
To run the unit and integration tests:

`mvn test`

To run the code coverage report:

`mvn cobertura:cobertura`

### Running
To run the application using the embedded Jetty webserver either run the included JAR or package the jar yourself:

**Java Command**:
`java -jar heavenhr-task-1.0-SNAPSHOT.one-jar.jar`

**Create JAR manually**:
`mvn install`

The default server port is 8080 so the server can be accessed at: 

`http://localhost:8080`

## API
The full API is documented using a living Swagger v2 specification, but the most relevant information is documented below.
All response and requests are `application/json` compliant.

## Applications
***GET `/applications?page=0`*** List applications (Optional page query parameter)

***GET `/application/:id`*** Get a single application

***POST `/application`*** Create an application

***PUT `/application/:id/:applicationStatus`*** Update an application's status (One of APPLIED, INVITED, REJECTED, HIRED)

### Example Application Response
```json
{
  "applicationStatus": "APPLIED",
  "candidate": {
    "email": "email@place.com",
    "id": 0
  },
  "id": 0,
  "resume": "Some resume"
}
```

### Example Application Post
```json
{
  "candidateEmail": "email@place.com",
  "offerId": 0,
  "resume": "Some resumse"
}
```

## Offers
***GET `/offers?page=0`*** List offers (Optional page query parameter)

***GET `/offer/:id`*** Get a single offer

***POST `/offer`*** Create an offer

***GET `/offer/:id/applications`*** Get all applications for an offer

### Example Offer Response
```json
{
  "id": 0,
  "jobTitle": "Software Engineer",
  "numberOfApplicants": 12,
  "startDate": "2017-12-07T03:18:55.264Z"
}
```

### Example Offer Post
```json
{
  "jobTitle": "Designer",
  "startDate": "2017-12-07T03:18:40.790Z"
}
```