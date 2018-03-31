ABOUT THE APPLICATION

This is a rate calculation system allowing prospective borrowers to obtain a quote from pool of lenders for 1 to 84 month loans.
This system take the form of a command-line application.

Application require a file containing a list of all the offers being made by the lenders within the system in CSV format, see the example inputdata.csv file provided alongside this specification.
The application strive to provide as low a rate to the borrower as is possible.
The application provide the borrower with the details of the monthly repayment amount and the total repayment amount.
Borrowers are able to request a loan of any £50 increment between £1'000 and £20'000 inclusive.
If the market does not have sufficient offers from lenders to satisfy the loan then the system informs the borrower that it is not possible to provide a quote at that time.

RESTRICTIONS
Application calculates monthly payment using round up strategy. Amount for the last payment month is less comparing to others due to rounding (last month payment = the remaining part).
As input file is loaded fully into the memory, application cannot process a file of any size. File size is limited to available resources and jvm settings and not validated by the program.
Any invalid row in input file will be ignored. File header row is mandatory for the application. Missing header is not gracefully handled.

PRIOR TO USE IN IDE
The project uses Lombok library - code generation tool. See https://projectlombok.org/ for more info, please install Eclipse or IDEA plugin in order to get code compiled in IDE.
Maven will compile without issues.

HOW TO BUILD
Required tools: Maven 3 (3.3.9), JDK 8. Maven and Java must be added to classpath search variable.
In project root folder execute: mvn clean package
The executable jar file will be saved to /target/p2pcalc-jar-with-dependencies.jar

HOW TO USE
After the project was successfully build, execute in root folder:
cd target
java -jar p2pcalc-jar-with-dependencies.jar <path to file> <Loan length, any integer between 1 and 84> <amount, any 50 between 1000 and 20000>
e.g.
java -jar p2pcalc-jar-with-dependencies.jar ../inputdata.csv 36 1000

