package GFG_Maven.GFG_Maven;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.util.*;
import java.util.regex.*;
public class IdentityE2E {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver","C:\Users\Shivangi\Documents\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		
		String inputFilePath = "C:\Users\Shivangi\Downloads\car_input.txt";
		List<String> registrations = extractRegistrationNumbers(inputFilePath);
		
		String outputFilePath = "C:\Users\Shivangi\Downloads\car_output.txt";
        Map<String, String[]> expectedData = loadExpectedData(outputFilePath);
        String websiteURL = "https://www.webuyanycar.com";
        Map<String, String[]> actualData = new HashMap<>();
        for (String reg : registrations) {
            driver.get(websiteURL);
            actualData.put(reg, performCarValuation(driver, reg));
        }
        compareData(actualData, expectedData);
        driver.quit();
	}
	private static List<String> extractRegistrationNumbers(String filePath) throws IOException {
        List<String> registrations = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{2}[A-Z]{3}");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    registrations.add(matcher.group());
                }
            }
        }
        return registrations;
	}
	private static Map<String, String[]> loadExpectedData(String filePath) throws IOException {
        Map<String, String[]> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    data.put(parts[0], new String[]{parts[1], parts[2], parts[3]});
                }
            }
        }
        return data;
	}
	private static String[] performCarValuation(WebDriver driver, String reg) {
        // Locate and interact with elements to input registration number and mileage
        WebElement regInput = driver.findElement(By.id("car regi no.")); // Update as per the actual site
        WebElement mileageInput = driver.findElement(By.id("car mileage no.")); // Update as per the actual site
        WebElement submitButton = driver.findElement(By.id("Submit")); // Update as per the actual site

        regInput.sendKeys(reg);
        mileageInput.sendKeys("50000"); // Random mileage for now
        submitButton.click();

       
        WebElement make = driver.findElement(By.id("make")); // Update as per the actual site
        WebElement model = driver.findElement(By.id("model")); // Update as per the actual site
        WebElement year = driver.findElement(By.id("year")); // Update as per the actual site

        return new String[]{make.getText(), model.getText(), year.getText()};
	}
	private static void compareData(Map<String, String[]> actual, Map<String, String[]> expected) {
        for (String reg : expected.keySet()) {
            String[] expectedDetails = expected.get(reg);
            String[] actualDetails = actual.getOrDefault(reg, new String[]{"N/A", "N/A", "N/A"});

            System.out.println("Comparing data for: " + reg);
            for (int i = 0; i < expectedDetails.length; i++) {
                if (!expectedDetails[i].equalsIgnoreCase(actualDetails[i])) {
                    System.out.println("Mismatch for " + reg + ": Expected " + expectedDetails[i] +
                            ", but found " + actualDetails[i]);
                }
            }
        }
	}
}
