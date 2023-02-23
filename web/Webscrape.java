package web;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Webscrape {

    public static void main(String[] args) throws InterruptedException {

        // Declare name and make the possibility of taking in a NBA player's name
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Please enter the name of your NBA player: ");
        String name = myScanner.nextLine();
        System.out.println("Searching for a player under name " + name.toUpperCase() + " ...");

        // Setting up Solenium and Chrome driver arguments
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-cookie-encryption");
        options.addArguments("--start-maximized");

        // Declaring driver and wait driver to wait for stuff to load
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.get("https://www.nba.com/search");

        // Sending search inputs with the string name that
        WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"search-text-input\"]"));
        searchInput.sendKeys(name);

        // The page has a problem when the user sends an undefined or unmeaningful parameters like 'ádsasdasd' or 'blabla bee ba bee'
        // Page crashes and returns white background, so I handled this with an recognition of Timeoutexception
        // If the page catches the exception here, the program ends.
        try {
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/div[1]/form/button")));
            searchButton.click();

            WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/div[2]/div[1]/div[1]/div[2]/button[1]")));
            filterButton.click();
        } catch (TimeoutException e) {
            System.out.println("Input parameters are causing page to crash, please try again");
            return;
        }

        // Defining a boolean to do the process while is in running mode
        boolean isRunning = true;

        while (isRunning) {

            // If the parameter is meaningful for example "Lebron Durant" but the player doesn't exist the page has an errorDisplay
            // Message div that says no results are found under that name.
            // WaitError is declared with timeout of 2 seconds. No need for more time since it loads fast.
            WebDriverWait waitError = new WebDriverWait(driver, 2);
            boolean isErrorDisplayed = true;

            // Trying to identify the erroDiv with ClassName, if the Timeout activates then isErrorDisplayed is set to false
            try {
                WebElement errorDiv = waitError.until(ExpectedConditions.visibilityOfElementLocated(By.className("SearchResults_srNoResults__QnvA_")));
            } catch (TimeoutException e) {
                isErrorDisplayed = false;
            }

            if (isErrorDisplayed == true) {
                System.out.println("Player not found.");
                break;
            } else if (isErrorDisplayed == false) {
                // Div with XPath has all anchor tags inside with list of NBA players, every NBA player is a seperate anchor tag
                List<WebElement> anchorElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/div[2]/div[2]/div[1]/div[1]//a")));

                if (anchorElements.size() > 1) {
                    System.out.println("There are too many results, please be more specific");
                } else if (anchorElements.size() == 1) {

                    // Defining executor to do code and search for player 3pts percentages while the system prints out some jokes while we wait for the result.
                    CountDownLatch latch = new CountDownLatch(1);
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {

                        WebElement selectedPlayer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/div[2]/div[2]/div[1]/div[1]/a")));
                        selectedPlayer.click();

                        WebElement statsNavigate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[2]/div/div[1]/div/ul/li[2]/a")));
                        statsNavigate.click();

                        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[4]/section[3]/div/div[2]/div[3]/table")));
                        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));

                        System.out.println("---------------------- RESULTS --------------------");

                        for (WebElement row : rows) {
                            WebElement yearElement = row.findElement(By.xpath(".//td[1]"));
                            String year = yearElement.getText();

                            WebElement threePtElement = row.findElement(By.xpath(".//td[11]"));
                            String threePt = threePtElement.getText();

                            System.out.println(year + "    " + threePt);
                        }

                        latch.countDown();
                    });

                    String[] jokes = {
                            "Why don't scientists trust atoms? Because they make up everything.",
                            "What's the difference between a poorly dressed man on a trampoline and a well-dressed man on a trampoline? Attire.",
                            "I'm reading a book on anti-gravity. It's impossible to put down.",
                            "Why did the tomato turn red? Because it saw the salad dressing!",
                            "Why did the scarecrow win an award? Because he was outstanding in his field.",
                            "Why don't eggs tell jokes? Because they'd crack each other up.",
                            "Why do seagulls fly over the sea? Because if they flew over the bay, they'd be bagels.",
                            "Why did the coffee file a police report? It got mugged.",
                            "What do you call a fake noodle? An impasta.",
                            "Why did the cookie go to the doctor? Because it was feeling crummy.",
                            "Why don't scientists trust atoms? Because they make up everything.",
                            "I told my wife she was drawing her eyebrows too high. She looked surprised.",
                            "What did the grape say when it got stepped on? Nothing, it just let out a little wine."
                    };

                    Random rand = new Random();

                    System.out.println("While you are waiting, let me tell you a joke ...");
                    for (int i = 0; i < 5; i++) {
                        int randomIndex = rand.nextInt(jokes.length);
                        System.out.println(jokes[randomIndex]);
                        Thread.sleep(6000);
                        if (latch.getCount() == 0) {
                            break;
                        }
                    }
                }
                isRunning = false;
            }
        }
    }
}
