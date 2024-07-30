# ApiCountries

ApiCountries is a Java-based RESTful web service application that provides information about countries. It uses Spring Boot for the application framework and includes various functionalities such as fetching country details by region or subregion, population of subregion, and exporting the data in different formats like JSON and CSV. The API uses https://countryapi.io/ for fetching country data.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Endpoints](#endpoints)

## Prerequisites

Before you start, make sure you have the following installed:

- [Java 19+] (JDK)
- [Maven 3.9.5+]

## Installation

1. **Clone the repository**:

```sh
   git clone https://github.com/BStchw/ApiCountries.git
   cd ApiCountries
  ```
2. **Build the project**:

Ensure Maven is installed and use it to build the project:
   ```sh
      mvn clean install
   ```
## Running the Application

To run the application, use the Spring Boot Maven plugin:
```sh
mvn spring-boot:run
```
By default, the application will start on port 8080. You can change the port by modifying the application.properties file located in src/main/resources/.

## Testing

To run the tests, use Maven:
```sh
mvn test
```
## Endpoints

### Get Countries by Region

- **URL**: `/countries/region`
- **Method**: `GET`
- **Parameters**:
    - `region` (required): The region to fetch countries for. Example values: `Europe`, `Asia`.
    - `format` (optional): The response format. Values can be `json` or `csv`. Default is `json`.
- **Response**: A list of top 10 countries by area in the specified region.
  **Example Usage:**

```http
GET http://localhost:8080/countries/region?region=europe&format=csv
```

### Get Countries by Subregion

- **URL**: `/countries/subregion`
- **Method**: `GET`
- **Parameters**:
    - `subregion` (required): The subregion to fetch countries for. Example values: `Southern Asia`, `Western Europe`.
    - `format` (optional): The response format. Values can be `json` or `csv`. Default is `json`.
- **Response**: List of countries from the specified subregion that have more than three borders.

 **Example Usage:**

```http
GET http://localhost:8080/countries/subregion?subregion=Northern Europe&format=csv
```
### Get Sum of Population by Subregion

- **URL**: `/countries/subregion/population`
- **Method**: `GET`
- **Parameters**:
    - `subregion` (required): The subregion to calculate the sum of population for. Example values: `Northern Europe`.
- **Response**: The sum of the population of countries in the specified subregion.

**Example Usage:**

```http
GET http://localhost:8080/countries/subregion/population?subregion=Northern%20Europe
