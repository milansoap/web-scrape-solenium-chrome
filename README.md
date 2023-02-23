NBA Three-Point Shooting
Statistics This program allows you to search for an NBA player and retrieve their three-point shooting statistics for each season they played. The program uses Selenium to automate a web browser and scrape the necessary data from the NBA Stats website.

Installation

- Make sure you have Java 8 or higher installed on your computer.
- Download and install Maven.
- Clone or download the repository to your local machine.
- Open a terminal or command prompt and navigate to the project directory.
- Run the following command to build the project and download its dependencies: mvn package

Usage 

- Run the following command to start the program - java -jar target/nba-stats-1.0.jar
- Follow the prompts to enter the name of the NBA player you wish to search for.
- If the player is found, the program will display their three-point shooting statistics for each season they played. If there are multiple players with the same name, the program will ask you to choose the correct player.
- The program will continue to prompt you for new searches until you enter "exit".

Troubleshooting

If the program is unable to find the player you are searching for, it may be because their name is not spelled correctly or they are not in the NBA Stats database.
If the program crashes or throws an exception, try running it again. If the problem persists, make sure you have the latest version of Java and that all dependencies are up-to-date.
Dependencies

1. Selenium
2. ChromeDriver

All dependencies are managed by Maven and will be downloaded automatically when building the project.
