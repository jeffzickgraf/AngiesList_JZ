package test.java;

import org.openqa.selenium.NoSuchElementException;
import org.springframework.util.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import ru.yandex.qatools.allure.annotations.Step;
import test.resources.ALObjects;
import test.resources.Constants;

public class NativeDemo extends AppiumBase {
	@Parameters({"context"})
	@Test
	public void test(String context) throws Exception {			
		try
		{
			reportiumClient.testStart("AL Demo Native", new TestContext());
			testStarted = true;
			login();						
			searchTheList();			
			reportiumClient.testStop(TestResultFactory.createSuccess());
			System.out.println("- - Test Completed Successfully " + getDeviceModel());					
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
		catch (NoSuchElementException e)
		{	
			if(testStarted)
			{
				reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", e));
			}
			//try to take a snapshot
			takeSafeScreenshot();
			System.out.println(getStackTraceAsString(e));
			
			throw(e);				
		} 
		catch (Exception ex)
		{
			System.out.println(getStackTraceAsString(ex));
			if(testStarted)
			{
				reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", ex));
			}
			throw(ex);
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
	
	@Step("Login")
	protected void login() throws InterruptedException
	{	
		reportiumClient.testStep("step: Login");
		System.out.println("- - Login " + getDeviceModel());
		
		if(textCheckpoint("Search the List", 10))
		{
			System.out.println("- - Already logged in " + getDeviceModel());
			return;
		}		
		
		driver.findElementByXPath(ALObjects.NativeSignInLink).click();
		
		driver.findElementByXPath(ALObjects.NativeUsername).sendKeys(Constants.ALUSERNAME);
		driver.findElementByXPath(ALObjects.NativePassword).sendKeys(Constants.ALPASSWORD);
		driver.findElementByXPath(ALObjects.NativeSignInButton).click();
		
		Assert.isTrue(textCheckpoint("Search the List", 15), "Expected to be logged in and see Search the List");
		
	}
	
	@Step("Search The List")
	protected void searchTheList() throws InterruptedException
	{
		reportiumClient.testStep("step: Search the List");
		System.out.println("- - Search the List " + getDeviceModel());
				
		driver.findElementByXPath(ALObjects.NativeSearchtheList).click();
		driver.findElementByXPath(ALObjects.NativePopularCategories);
		driver.findElementByXPath(ALObjects.NativePlumbing).click();
		Thread.sleep(3000);
		driver.findElementByXPath(ALObjects.NativeChangeLocationLink).click();
		driver.findElementByXPath(ALObjects.NativeUseCurrentLocation).click();
		
		if(isAndroid())
		{
			Assert.isTrue(textCheckpoint("New York", 20), "Expected to find New York in results");
		}
		else
		{
			Assert.isTrue(textCheckpoint("Columbus", 20), "Expected to find Columbus in results");
		}		
		takeSafeScreenshot();
	}
}
