package test.java;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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
		
		if(isLoggedIn())
		{
			System.out.println("- - Already logged in " + getDeviceModel());
			takeSafeScreenshot();
			return;
		}		
		
		driver.findElementByXPath(ALObjects.NativeSignInLink).click();
		
		driver.findElementByXPath(ALObjects.NativeUsername).sendKeys(Constants.ALUSERNAME);
		driver.findElementByXPath(ALObjects.NativePassword).sendKeys(Constants.ALPASSWORD);
		driver.findElementByXPath(ALObjects.NativeSignInButton).click();
		
		WebElement element = driver.findElementByXPath(ALObjects.NativeSearchtheList);
		Assert.isTrue(element != null, "Expected to be logged in and see Search the List");
		takeSafeScreenshot();
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
			Assert.isTrue(textCheckpoint("Columbus", 20), "Expected to find Columbus in results");
		}
		else
		{
			Assert.isTrue(textCheckpoint("Columbus", 20), "Expected to find Columbus in results");
		}		
		takeSafeScreenshot();
	}
	
	private Boolean isLoggedIn()
	{
		try {
			Map<String, Object> searchListParams = new HashMap<>();
			searchListParams.put("content", "Search the List");
			searchListParams.put("screen.top", "60%");
			searchListParams.put("screen.height", "40%");
			searchListParams.put("screen.left", "0%");
			searchListParams.put("screen.width", "100%");
			searchListParams.put("timeout", 15);
			Object resultSearch = driver.executeScript("mobile:checkpoint:text", searchListParams);
			return Boolean.valueOf(resultSearch.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return false;
	}
	
}
