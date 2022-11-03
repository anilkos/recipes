# Explanation

This is a Recipe Tracking application that user can add, update, delete or search recipes for their tastes.

## To Use Application

First you need to make sure that you have docker on your machine. 
After that you need to fetch Mongo DB repository from central and then you need to run it on port 27017 or you 
already have mongo db on your machine then you need to change connection url inside application.yml
```bash
mvn springboot:run
```

## Api Documantation

You can find detailed api documentation on [swagger](http://localhost:8080/swagger-ui.html
)

## Tests

This application has both unit and integration tests.  
Total test coverage is around 95%

## Future Changes
For the future, this application could be an image with Mongodb.
To do that, we can use docker.
The concrate Docker-Compose file should look like that.
#### docker-compose.yml
```yaml
version: "3"
services:
  moviemongodb:
    image: mongo:latest
    container_name: "recipesmongodb"
    hostname: recipesmongodb
    ports:
      - 27017:27017
  movie-app:
    image: recipe-app
    container_name: recipe-app
    ports:
      - 8080:8080
    links:
      - recipesmongodb
```
1) We can definitely use cache for search requests. To keep it simple, i didn't use a cache mechanism but it is a must for performance.
2) Develop a seperate application for authorazation. Fow now it is embeded in recipe application.If we want to use more than one instance or replica for this application, login services also will be more than once. It is not good idea. Login services should be one and main service. We can use here an api gateway for authorazation and routing the requests.
3) Implement a  fallback mechanism for  db connection. of course we should define proper bulkheads and timelimittters for those. 
4) Add metric to capture more details and provide more data to create logging and monitoring system. (Grafana,Prometeus,Kibana ..)
5) Provide CI/CD scripts to make the CI/CD process automatically.



## License
[MIT](https://choosealicense.com/licenses/mit/)