# Seletest
Selenium Testing framework

# What is this?
Seletest combines Selenium with TestNG so you can create Automated browser tests with easy. 
Seletest can also update JIRA tickets based on Test results. 
The Seletest framework is designed to allow testers to begin scripting automated tests quickly without having to
set up test reporting alongside selenium. 

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
Also populate all the required URLs for starting browser and assert pages. If using JIRA or Slack, you will want to add details for instances here as well

## Scripting
You are now ready to script tests. An example test has been included.

#Tips
* Testers should aim to user the CommonFunctions functions over the standard Selenium functions. 
* These functions are more stable and consistent that OOTB selenium. It also allows for easier maintainability of large test suites. 
* Currently only chrome is supported. To add other browsers, add the drivers to the WebDrivers folder and update the properties file
