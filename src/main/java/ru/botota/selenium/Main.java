package ru.botota.selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.LongPollingBot;
import ru.botota.selenium.service.WardenService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maksim on 25.06.2017.
 */
public class Main {

    private static final String LOGIN = "Zubrrr";
    private static final String PASS = "121212";
    private static Pattern pattern = Pattern.compile("Задание \\d+");
    private static final String LEVEL_STRING = "Время на уровне: ";
    private static Pattern patternTime = Pattern.compile(LEVEL_STRING+"\\d{2}:\\d{2}:\\d{2}");
    private static Pattern patternAdv = Pattern.compile("Подсказка .+"+LEVEL_STRING);


    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        Bot bot = new Bot();
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        WardenService wardenService = new WardenService(
                //"http://classic.dzzzr.ru/mega-vlg/",
                "https://time100.ru/",
                "gamelogin",
                "gamepass",
                bot
        );

        Thread schedulerThread = new Thread(wardenService);
        schedulerThread.start();





/*
        // Создаем экземпляр WebDriver
        // Следует отметить что скрипт работает с интерфейсом,
        // а не с реализацией.
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files (x86)\\gecko\\geckodriver.exe");

        WebDriver driver = new FirefoxDriver();

*//*
        //AUTH __________________
        driver.get("http://classic.dzzzr.ru/mega-vlg/");
        driver.findElement(By.name("login")).sendKeys(LOGIN);;
        WebElement webElement = driver.findElement(By.name("password"));
        webElement.sendKeys(PASS);
        webElement.submit();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.className("grayBox")).getText().contains(LOGIN);
            }
        });
        //__________________________*//*

        //driver.get("http://dzrcc.tk/test/a/2.html");
        driver.get("http://abc:121212@vle.pe.hu/mega");
        driver.manage().window().maximize();


        try {
            driver.findElement(By.name("spoilerCode"));
        } catch (NoSuchElementException e){
            System.out.println("No spoiler");
        }


        List<WebElement> titles = driver.findElements(By.className("title"));
        String page = driver.findElement(By.tagName("table")).getText();
        Matcher timeMatcher = patternTime.matcher(page);
        if (timeMatcher.find()){
            String time = timeMatcher.group().replace(LEVEL_STRING,"");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            int hour = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);
            Matcher matcher = patternAdv.matcher(page.replace("\n"," ").replace("\r", ""));
            if (hour == 0 && min >= 30){
                System.out.println("первая подсказка");
                if (matcher.find()){
                    String textAdv = matcher.group().replace(LEVEL_STRING, "");
                    System.out.println(textAdv);
                }

            }

            if (hour == 1){
                System.out.println("Вторая подсказка");
            }
        }

        for (WebElement title : titles){
            if (title.getText().contains("Задание")) {
                Matcher matcher = pattern.matcher(title.getText());
                String z ="?";
                if (matcher.find()){
                    z = matcher.group().replace("Задание ", "");
                }
                System.out.println("Задание "+z);
            }
        }



        // SCREENSHOT
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)", "");
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        BufferedImage fullImg = null;
        try {
            fullImg = ImageIO.read(screenshot);


            WebElement ele = driver.findElement(By.className("zad"));
            WebElement msg = driver.findElement(By.className("sysmsg"));
            Point point = ele.getLocation();
            int posY = msg.getLocation().getY() + msg.getSize().getHeight();
            int dop = ele.getLocation().getY()-posY;
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();
            System.out.println(String.format("width %s, height %s, X %s, Y %s", eleWidth, eleHeight, point.getX(), point.getY()));

            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), posY, eleWidth, eleHeight+dop);
            ImageIO.write(eleScreenshot, "png", screenshot);

            FileUtils.copyFile(screenshot, new File("screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }



        driver.quit();*/
    }
}
