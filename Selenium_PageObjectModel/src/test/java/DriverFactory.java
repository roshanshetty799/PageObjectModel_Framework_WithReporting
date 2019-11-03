
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

	protected static WebDriver driver;
	protected static Properties propFile;

	public static Properties getPropFile() {
		return propFile;
	}

	// Method to locate and load the properties file
	public static Properties loadPropFile() {
		propFile = new Properties();
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\BaseData.properties");
		} catch (FileNotFoundException e) {
			System.out.println("Properties File is missing or the path specified is incorrect");
		}
		try {
			propFile.load(fs);
		} catch (IOException e) {
			System.out.println("Unable to load the propeties file");
		}
		return propFile;
	}

	// The constructor initialises the driver and loads the properties file
	public DriverFactory() {
		initialize();

	}

	// Method to initialise driver
	public void initialize() {
		if (driver == null)
			createNewDriverInstance();
	}

	// This method reads the properties file and creates a driver instance for a
	// browser
	
	public void createNewDriverInstance() {

		loadPropFile();

		if (propFile.getProperty("browser").equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();

			FirefoxOptions firefoxOptions = new FirefoxOptions();

			// To get through the firefox security warning
			firefoxOptions.setCapability("acceptInsecureCerts", true);

			driver = new FirefoxDriver(firefoxOptions);
			driver.manage().deleteAllCookies();

		} else if (propFile.getProperty("browser").equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (propFile.getProperty("browser").equals("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	public void closingBrowser() {
		driver.quit();
		driver = null;

	}

	public WebDriver getDriver() {
		return driver;
	}

	public String getBaseUrl() {

		String BaseUrl = propFile.getProperty("baseUrl");
		return BaseUrl;

	}
}
