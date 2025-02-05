
📖 Simple Book API Automation

This repository contains API automation scripts for the Simple Book API, using:
✅ RestAssured for API testing
✅ Cucumber (BDD) for behavior-driven testing
✅ TestNG for test execution


📌 Project Structure
📂 src/test/java (Test Code)
📁 steps/ → Contains step definition files for CRUD operations.
📁 utils/ → Includes ConfigManager to manage configurations centrally.
📁 runners/ → Contains the TestRunner class to execute tests.

📂 src/test/resources (Test Data & Configs)
📁 features/ → Contains feature files for CRUD operations.
📁 testData/ → Stores JSON or other test data files.
📄 config.properties → Stores base URLs and other configurations.


🚀 How to Run the Tests
🔹 Prerequisites
Java 8+ installed
Maven installed (mvn -version to check)
Clone this repository:
git clone (https://github.com/Mohamed-Ishak/SimpleBooksAPIs.git)

🔹 Run Tests using TestRunner
 To execute all tests via the TestRunner class:
 mvn test

 To run tests for a specific feature:
 mvn test -Dcucumber.options="src/test/resources/features/E2E_Scenario.feature"

🛠️ Configuration Management

Base URI & other settings are stored in config.properties.

ConfigManager.java loads these values automatically before test execution.

📜 Reporting

The framework generates a Cucumber HTML report after test execution.

Check the report in:
reports/cucumber-reports.html

📩 Contact

📧 mohamed.abdelrahman.ishak@gmail.com

💼 https://www.linkedin.com/in/mohamed-ishak-%F0%9F%87%B5%F0%9F%87%B8-4a160a163/




