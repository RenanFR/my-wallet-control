package com.wallet.control.web.cucumber.auth.register;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.com.wallet.control.web.MyWalletControlApplication;
import br.com.wallet.control.web.repository.mongo.LoginRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(classes = MyWalletControlApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class RegisterUserFeatureSteps {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(RegisterUserFeatureSteps.class);
	
	private WebDriver driver;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Given("I access the login page and doesnt have a user")
	public void iAccessTheLoginPageAndDoesntHaveAUser() {
		LOG.info("ACESSANDO COM O DRIVER DO SELENIUM NO ANGULAR");
		System.setProperty("webdriver.chrome.driver","C:\\Development\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://localhost:4200/#/login");		
	}

	@Given("I click the sign up button")
	public void iClickTheSignUpButton() {
		driver.manage().window().maximize();
		driver.findElement(By.name("btnRegister")).click();
	}

	@Given("fill my personal and financial data")
	public void fillMyPersonalAndFinancialData() {
		driver.findElement(By.name("userName")).sendKeys("Renan Rodrigues");
		driver.findElement(By.name("userEmail")).sendKeys("renanfr1047@gmail.com");
		driver.findElement(By.name("cpf")).sendKeys("36589817847");
		driver.findElement(By.name("password")).sendKeys("renan");
		driver.findElement(By.name("passwordConfirm")).sendKeys("renan");
		driver.findElement(By.className("icon-wallet")).click();
		driver.findElement(By.id("btnAddBank")).click();
		Select selectBank = new Select(driver.findElement(By.name("bank")));
		selectBank.selectByValue("Banco Inter");
		driver.findElement(By.name("account-number")).sendKeys("18831400");
		driver.findElement(By.name("agency")).sendKeys("0001-9");
		Select selectAccountType = new Select(driver.findElement(By.name("account-type")));
		selectAccountType.selectByValue("Corrente");		
		driver.findElement(By.name("balance")).sendKeys("0");
		driver.findElement(By.name("btnAddBank")).click();
		driver.findElement(By.className("icon-login")).click();
		
	}

	@When("I click submit")
	public void iClickSubmit() throws InterruptedException {
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(10000);
		driver.findElement(By.className("swal2-confirm")).click();
	}

	@Then("my user is enabled and I can login")
	public void myUserIsEnabledAndICanLogin() throws InterruptedException {
		LOG.info("PREENCHENDO USUÁRIO E SENHA NA PÁGINA DE LOGIN");
		driver.findElement(By.name("user-email")).sendKeys("renanfr1047@gmail.com");
		driver.findElement(By.name("password")).sendKeys("renan");
		driver.findElement(By.name("btnLogin")).click();
		Thread.sleep(5000);
		assertEquals("http://localhost:4200/#/dashboard", driver.getCurrentUrl());
	}
	
	@After
	public void deleteUser() {
		loginRepository.delete(loginRepository.findByEmail("renanfr1047@gmail.com").get());
	}
	
}
