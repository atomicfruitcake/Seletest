# Seletest
Selenium Testing framework

# What is this?
Seletest combines Selenium, TestNG, Docker and BrowerMobProxy so you can create automated browser tests with ease. 
Seletest can also update JIRA tickets based on Test results and update slack, completely automated.
The Seletest framework is designed to allow testers to begin scripting automated tests quickly without having to
set up test reporting alongside selenium. 
Seletest supports dockerised testing (if docker is installed) and network traffic capture with BrowserMobProxy. Example tests can be found in src.tests

# How do I use it?
## Initial Setup
Clone this repo and run the following commands

gradle createGradleWrapper  
gradle eclipse (If using Eclipse)  
gradle idea (If using IntelliJ IDEA  
gradle clean build  

Now just import the project in to your IDE

## Custom settings and Properties
The settings.txt file is used to pass custom settings to the test. This is where you should enter test usernames, test passwords and environment names etc.... 
The Properties file should contain all the data required to run the tests. Test usernames, passwords and environments should be updated here from the settings file. 
Also populate all the required URLs for starting browser and assert pages. If using JIRA or Slack, you will want to add the URL or API details for the relevent instances here as well

## Scripting
You are now ready to script tests. An example test has been included.

#Tips
* Use the CommonFunctions functions over the standard Selenium functions. If a functions does not exist CommonFunctions, add it and then submit it to this project!
* Remove all hardcoded data from tests and keep it in properties. 
* Currently only chrome is supported. To add other browsers, add the drivers to the WebDrivers folder and update the properties file.
* Build your test methods in CommonMethods and build tests in pieces. This allows for maintable tests and fast scripting of complex E2E tests
