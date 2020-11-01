# Job Processing Service

This is an HTTP based job processing service.  
A job is a collection of tasks, where each task has a name and a shell command. Tasks may
depend on other tasks and require that those are executed beforehand. The service takes care
of sorting the tasks to create a proper execution order.

## Build and execution

To build the service run the following command in the root directory of the project:  
**gradlew build** (for Windows)  
**./gradlew build** (for Unix based OS)

After the build finish:  
- a unit test report can be find here: _build/reports/tests/test/index.html_  
- a component test report can be find here: _build/reports/tests/componentTest/index.html_  
- a test coverage report can be find here: _build/reports/jacoco/test/html/index.html_

To start the service run the following command:  
**gradlew bootRun** (for Windows)  
**./gradlew bootRun** (for Unix based OS)  
or navigate to build/libs and run  
**java -jar jobprocessing-0.0.1-SNAPSHOT.jar**

In order to run the bash script directly from shell, simply run for example:  
**curl -s -H 'Content-Type: application/json' -d @mytask.json http://localhost:8080/job?bashScript=true | bash**