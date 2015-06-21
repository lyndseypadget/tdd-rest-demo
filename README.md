tdd-rest-demo
======================
An automated testing demo for a simple REST service.

The TDD Exercise
======================
Get revision "Foundational API with baseline test failures." to see finished tests and implement the server-side checking yourself to make the 13 failing tests pass!  The following revision, "All tests passing." has example answers.  Note that some logic might be more appropriate in a business or data layer, but this is demo and those don't exist here.  :)

My Environment
======================
- Apache Maven 3.2.5 (local)
- Java 1.8.0_40 x64 (local)
- Tomcat 8.0.20 (local)

The Projects
======================
- awesome-api contains the RESTful API code (using Jersey) and unit tests
- awesome-api-system-tests contains system tests

Local Setup
======================
- If using Eclipse IDE, run "mvn eclipse:clean eclipse:eclipse" to generate Eclipse artifacts.  Then import them into your workspace.
- Note that you <b>must</b> change the Tomcat deployment directory in the awesome-api pom.xml if using "mvn package" to deploy!
- For the awesome-api project, build and deploy using "mvn clean compile package".
- If not already done, start Tomcat using "$CATALINA_HOME/bin/startup.sh" (shutdown.sh to stop).
