#  Car Parking Microservice

The Car Parking Microservice is responsible for building and maintaining a searchable car park available in SG
Users can search for car park nearest based on longitude and latitude.

## How does it work

To solve the problem we can think 2 above 2 solutions:

**MySQL's Built-in Spatial Extensions:**

Purpose: MySQL's spatial extensions provide a way to store, query, and analyze geospatial data directly within the MySQL database. It's suitable for applications that require geospatial data to be stored and queried using standard SQL.

Data Types: MySQL provides spatial data types like POINT, LINESTRING, POLYGON, and others to represent different types of geometric shapes.

Functions: MySQL offers a set of spatial functions (e.g., ST_Distance, ST_Within, ST_Contains) that allow you to perform various geospatial operations, such as measuring distances between points or determining if one geometry is within another.

Spatial Indexing: You can create spatial indexes to optimize spatial queries for better performance.

Use Cases: MySQL's spatial extensions are suitable for a wide range of applications where geospatial data is an integral part of the database, such as location-based services, mapping applications, and GIS (Geographic Information Systems) applications.

**Geohashing:**

Purpose: Geohashing is a technique for encoding geographic coordinates into a single string, often used for proximity-based searching and indexing. It's particularly useful when you need to perform fast and approximate location-based searches.

Data Structure: Geohashing typically uses a hierarchical grid system where each level of the grid represents a different level of precision (e.g., Quadtree or Hilbert Curve). Each grid cell is represented by a unique hash string.

Operations: Geohashing allows you to quickly find nearby points by comparing the hash strings. You can easily search for locations within a given geohash prefix.

Use Cases: Geohashing is commonly used in applications that require location-based search and indexing, such as geospatial indexing for large datasets, geosocial applications, and geofencing.

**Due to we had choose MySQL as database and for wide range support for application -> we go with Spatial Extension**

## Technologies Used

- Java 17
- Spring Boot 3.1.2
- Spring Data JPA
- Lombok 1.18.3
- Maven 3.8.6
- (Testing) TestContainer 1.18.3

## Prerequisites

Before you begin, ensure you have the following installed:

1. Docker: [Install Docker](https://docs.docker.com/get-docker/)
2. (Optional if cannot install docker) Colima: [Install Colima](https://github.com/abiosoft/colima)
3. Docker-compose: [Install Docker Compose](https://docs.docker.com/compose/install/)
4. Java 17: [Install Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
5. Maven: [Install Maven](https://maven.apache.org/download.cgi)
6. Docker Need at least 4 CPU and 8GB RAM

## How to run

1. Check docker version
   ```
    docker version
2. Build Application
   ```
    mvn clean package -Dmaven.test.skip 
   
3. Run by docker-compose
   ```
    docker-compose up -d
4. Check services ready
   Run below command to ensure all services running correctly.
   ```
    docker-compose ps
5. Check configuration `com.huynguyen.wego.import.enabled=true` 
6. For the first time wait for the importing process finished before start call api. It can take around 4-5 minutes.

## How to run test

1. Check docker version
   ``` 
    docker version
2. (Optional) if you run docker by colima **
    ```
     export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock 
     export DOCKER_HOST="unix://${HOME}/.colima/docker.sock"
3. ** Run Test **
    ```
     mvn clean test

## Query for data
   ```
   curl --request GET \
      --url 'http://localhost:8080/carparks/nearest?latitude=1.37326&longitude=103.897&page=1&per_page=2'
   ```
##  Improvement
   - I write only unit test for controller layer by integration test with `Test Containers` to test all layers of the application.
     -> we can improve the test for service layers
   - Add more edge cases checks for now it is only internal server error.