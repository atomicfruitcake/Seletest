# Seletest
Selenium Testing framework

# What is this?
Seletest combines Selenium with TestNG so you can create Automated browser tests with easy. 
Seletest can also update JIRA tickets based on Test results. 
The Seletest framework is designed to allow testers to begin scripting automated tests quickly without having to
set up test reporting alongside selenium. 

# How do I use it?
Clone this repo and run the following commands

gradle createGradleWrapper  
gradle eclipse (If using Eclipse)  
gradle idea (If using IntelliJ IDEA  
gradle clean build  

Now just import the project in to your IDE and your ready to start Scripting

#Tips
Testers should aim to user the CommonFunctions functions over the standard Selenium functions. 
These functions are more stable and consistent that OOTB selenium. It also allows for easier maintainability of large test suites.
