package com.wallet.control.web.cucumber.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.com.wallet.control.web.MyWalletControlApplication;
import br.com.wallet.control.web.exception.UserAlreadyExistsException;
import br.com.wallet.control.web.model.NewLoginDTO;
import br.com.wallet.control.web.service.LoginService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(classes = MyWalletControlApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class AuthenticationFeatureSteps {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFeatureSteps.class);
	
	private WebDriver driver;
	
	private String user;
	
	private String password;
	
	@Autowired
	private LoginService loginService;
	
	@Before
	public void insertDefaultUser() {
		user = "projectsjohndoe@gmail.com";
		password = "projectsjohndoe";
		NewLoginDTO login = new NewLoginDTO();
		login.setUserName(user);
		login.setUserEmail(user);
		login.setPassword(password);
		try {
			loginService.registerUserAccount(login);
		} catch (UserAlreadyExistsException e) {
			LOG.error("USUÁRIO {} JÁ EXISTE NA BASE", user);
		}
	}
	
	
	@Given("valid user credentials")
	public void validUserCredentials() {
		LOG.info("TESTANDO AUTENTICAÇÃO COM USUÁRIO {} E SENHA {}", user, password);
	}

	@When("i access the login page")
	public void iAccessTheLoginPage() {
		LOG.info("ACESSANDO COM O DRIVER DO SELENIUM NO ANGULAR");
		System.setProperty("webdriver.chrome.driver","C:\\Development\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://localhost:4200/#/login");
	}

	@When("i input my credentials and click login button")
	public void iInputMyCredentialsAndClickLoginButton() {
		LOG.info("PREENCHENDO USUÁRIO E SENHA NA PÁGINA DE LOGIN");
		driver.findElement(By.name("user-email")).sendKeys(user);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("btnLogin")).click();
	}

	@Then("i should be able to see the dashboard")
	public void iShouldBeAbleToSeeTheDashboard() {
		LOG.info("VERIFICANDO SE O DASHBOARD FOI ACESSADO COM SUCESSO");
		assertEquals("http://localhost:4200/#/dashboard", driver.getCurrentUrl());
	}	

}
