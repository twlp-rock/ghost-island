# ghost-island
## Technical Stack
* ghost-island-gateway: Spring gateway
* ghost-island-dashboard: Vue.js + D3.js
* ghost-island-cargo: NodeJs + Loopback4 + Mongodb
* ghost-island-batch: Spring batch + Mongodb
* ghost-island-analysis: Spring web + Spark + Mongodb

## How to run
You need to establish Java 8, NodeJS and Mongodb environment yourself.

### ghost-island-gateway
    $ ./gradlew build -x test
    $ java -jar ./build/libs/ghost-island-0.0.1-SNAPSHOT.jar
 
### ghost-island-batch
    $ ./gradlew build -x test
    $ java -jar ./build/libs/ghost-lsland-0.0.1-SNAPSHOT.jar
    
### ghost-island-cargo
    $ npm install
    $ port=8082 npm start
    
### ghost-island-dashboard
    $ npm install
    $ npm run serve
    
### ghost-island-analysis
    $ ./gradlew build -x test
    $ java -jar ./build/libs/ghost-island-0.0.1-SNAPSHOT.jar 

    
## Server port
* 8080, ghost-island-gateway
* 8081, ghost-island-dashboard
* 8082, ghost-island-cargo
* 8083, ghost-island-batch
* 8084, ghost-island-analysis

## Start to test
### Import data
    $ curl http://localhost:8083/jobLauncher?job=importOpenDataJob
### Browse the project
http://localhost:8080/map