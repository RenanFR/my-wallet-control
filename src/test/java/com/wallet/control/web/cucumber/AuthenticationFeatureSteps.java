package com.wallet.control.web.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.com.wallet.control.web.MyWalletControlApplication;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(classes = MyWalletControlApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class AuthenticationFeatureSteps {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFeatureSteps.class);
	
	private WebDriver driver;
	
	private String user;
	
	private String password;
	
	@Given("valid user credentials")
	public void validUserCredentials() {
		user = "renanfr1047@gmail.com";
		password = "renan";
	}

	@When("i access the login page")
	public void iAccessTheLoginPage() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\rodrigues\\Dev\\software\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://localhost:4200/#/login");
	}

	@When("i input my credentials and click login button")
	public void iInputMyCredentialsAndClickLoginButton() {
		driver.findElement(By.name("user-email")).sendKeys(user);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.className("submit")).click();
	}

	@Then("i should be able to see the dashboard")
	public void iShouldBeAbleToSeeTheDashboard() {
		assertEquals("dashboard", driver.getCurrentUrl());
	}	

}
