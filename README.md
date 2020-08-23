QMoney is an app for monitoring and analyzing stocks for Portfolio Managers are make trade recommendations to their clients. The core concepts covered in this Micro-Experience are Programming in Java, JSON, Design Patterns and Concurrency.

**Module 1: Read user portfolio file** <br />
Here we start with an introduction to the basics of the concepts that will be used quite regularly throughout the ME, i.e. Reading JSON and Deserialization of data. Here, we have to read JSON data from a file and then deserialize it to get the data values.

**Module 2: Get stock quotes from a third-party provider** <br />
In this module, we have to start building the actual backend for our software where we try to hit a third-party API to get stock data. In the module first, we have to set up the provider and make a sample API request through cURL commands. Then we have to request our java file. Lastly, we get a taste of refactoring the code, that we’ll have to do rigorously in a further module.

**Module 3: Implement logic to perform calculations** <br />
This module is one of the smallest modules (but trickiest one, if not tackled in the right way) of the Micro-Experience. All we have to implement is a formula to calculate the Annualized returns of the stocks in the portfolio, that’s it.

**Module 4: Create a portfolio management library** <br />
Here we start to make our code more manageable and robust. In this module, we begin by moving most of the code to an Implementation of an Interface. Then we collect the Implementations into a Factory to make it easier for external users to create objects of the desired type. Once all this is done, we implement a sample case scenario in our main application code to test our code.

**Module 5: Publish the library** <br />
The Frontend, that was being developed from the beginning while we were working on the backend, is ready now and we have to integrate it to the backend. We have to pull the frontend repository into our workspace. Then, we publish our backend to a Maven repository. Once this is done, we run the Spring Boot application that we created and the UI that we pulled. Then we open the dashboard in the browser and check if the code was working the way it was intended.

**Module 6: Add another service provider** <br />
This is where you realize how much code management takes place behind the scenes of any Software Product. Here we have to make the code robust enough to be able to accommodate for the future multiple Stock quotes providers, which shows how important future planning is. Here, we have to refactor the code further and move the code for already available Stock quotes provider and move it to a lower layer and add another provider on the same level to test our functioning.

**Module 7: Handle user issues** <br />
In this module, we start working on Exception Handling and how a developer works when the users are faced with a new exception that wasn’t handled the way it should have been. Here, first of all, we have to identify the Exception by reproducing the error and adding debugging statements. Next, we add suitable exception handling.

**Module 8: Enhance performance of the app** <br />
The module extracts the actual power of Java: “Multi-Threading.” In this module, we have to Multi-thread the stock quotes requests in order to reduce the execution time and in turn increases the efficiency. Firstly, we calculate the time that it takes for the process to execute without multi-threading. Then, we have to modify the code to add multi-threading and compare it with the earlier times.

QMoney was much more fun than before as it was up the alley I had been going. The kind of Software Development in this ME is what makes it more fun.
