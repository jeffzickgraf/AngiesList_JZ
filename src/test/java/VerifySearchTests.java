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

public class VerifySearchTests extends AngiesListSeleniumBase {
	
		@Parameters({ "context" })
		@Test
		public void categorySearchTest(String context) throws Exception {			
			try{
				String searchTerm = "Plumbing";
				reportiumClient.testStart("AL Category Search", new TestContext());
				testStarted = true;
				openALSite();
				login(15);
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
			catch (AssertionError ae)
			{
				takeSafeScreenshot();
				System.out.println("Assertion Error: " + ae.getMessage());
				if(testStarted)
				{
					System.out.println("- - testStop hit assertion error");		
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", new Exception("Assertion Error: " + ae.getMessage())));
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
		
		@Parameters({ "context" })
		@Test
		public void keywordSearchTest(String context) throws Exception {			
			try{
				String searchTerm = "Oil";
				reportiumClient.testStart("AL Keyword Search", new TestContext());
				testStarted = true;
				openALSite();
				login(15);
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
			catch (AssertionError ae)
			{
				takeSafeScreenshot();
				System.out.println("Assertion Error: " + ae.getMessage());
				if(testStarted)
				{
					System.out.println("- - testStop hit assertion error");		
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", new Exception("Assertion Error: " + ae.getMessage())));
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
