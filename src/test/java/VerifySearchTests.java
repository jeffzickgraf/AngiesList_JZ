package test.java;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import ru.yandex.qatools.allure.annotations.Step;
import test.java.jsSelect.JSSelect;
import test.resources.Constants;
import test.resources.ALObjects;
import test.resources.WindTunnelUtils;

public class VerifySearchTests extends SeleniumBase {
	
		@Parameters({ "context" })
		@Test
		public void categorySearchTest(String context) throws Exception {			
			try{
				String searchTerm = "Plumbing";
				reportiumClient.testStart("AL Category Search", new TestContext());
				testStarted = true;
				openALSite();
				login();
				searchAndBasicVerify(searchTerm);
				
				if(windTimerFailed)
				{
					reportiumClient.testStop(TestResultFactory.createFailure("One or more wind tunnel timer(s) exceeded.", new Exception("Timer exceeded.")));
				}
				else
				{
					reportiumClient.testStop(TestResultFactory.createSuccess());
				}
				
				System.out.println("- - Test Completed Successfully " + getDeviceModel());		
			} catch (NoSuchElementException e){
				takeSafeScreenshot();
				System.out.println(getStackTraceAsString(e));
				if(testStarted)
				{
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", e));
				}				
			}
			catch (Exception ex)
			{
				System.out.println(getStackTraceAsString(ex));
				if(testStarted)
				{
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", ex));
				}				
			}
			finally { 
				try
				{ 
					testTearDown(context);
				}
				catch(Exception ex) 
				{
					System.out.println("Failure in testTearDown");
				}
			}
		}
		
		@Step("Open Angies List Site")
		protected void openALSite() {
			reportiumClient.testStep("step: Open web URL");
			System.out.println("- - Opening URL " + getDeviceModel());
			driver.get("https://member.angieslist.com/member");
			takeSafeScreenshot();
		}

		@Step("Logout")
		protected void logout() throws InterruptedException
		{
			reportiumClient.testStep("step: Logout");
			System.out.println("- - Logging Out " + getDeviceModel());
			
			driver.findElementByXPath(ALObjects.WebSignOutButton).click();
							
			//verify:
			Assert.assertTrue(textCheckpoint(Constants.MEMBERSIGNIN, Constants.TEXTCHECKWAIT), "Expected to see Member Sign In.");
		}
		
		@Step("Login")
		protected void login() throws InterruptedException
		{	
			WebElement element;
			
			reportiumClient.testStep("step: Login");
			System.out.println("- - Logging In " + getDeviceModel());
			
			if(textCheckpoint("Welcome. " + Constants.ALACCOUNTFIRSTNAME, 8))
			{
				System.out.println("- - Already logged in - sending to Log Out " + getDeviceModel());
				return;
			}
			
			element = driver.findElementByXPath(ALObjects.WebSignInUsername);
			element.clear();
			element.sendKeys(Constants.ALUSERNAME);
			
			element = driver.findElementByXPath(ALObjects.WebSignInPassword);
			element.clear();
			element.sendKeys(Constants.ALPASSWORD);
			
			driver.findElementByXPath(ALObjects.WebSignInSubmitButton).click();
			
			//verify on post login landing screen
			Assert.assertTrue(textCheckpoint("Welcome. " + Constants.ALACCOUNTFIRSTNAME, Constants.TEXTCHECKWAIT, true, false),"Expected to be signed in but couldn't find Welcome message.");
			takeWindTunnelTimer("Logged In", 8000);
			takeSafeScreenshot();			
			softAssert.assertAll();
			takeWindTunnelPointOfInterest("Successfully logged in.", WindTunnelUtils.SUCCESS);
		}
		
		@Step("Search with Basic Verify")
		protected void searchAndBasicVerify(String searchText) throws InterruptedException
		{	
			reportiumClient.testStep("step: Search with Basic Verify");
			System.out.println("- - Searching " + getDeviceModel());
			
			driver.findElementByXPath(ALObjects.WebSearchInput).sendKeys(searchText);
			driver.findElementByXPath(ALObjects.WebSearchInput).sendKeys(Keys.ENTER);
			
			Assert.assertTrue(textCheckpoint("Showing results for " + searchText, 30), "Expected to see Showing results for " + searchText);
			takeWindTunnelTimer("Search Results Returned", 7500);
		}
		
}
