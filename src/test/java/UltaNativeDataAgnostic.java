package test.java;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import ru.yandex.qatools.allure.annotations.Step;
import test.resources.UltaObjects;

public class UltaNativeDataAgnostic extends UltaDemoNative {

	@Parameters({"context"})
	@Test
	public void agnosticTest(String context) throws Exception {			
		try
		{
			reportiumClient.testStart("Ulta Demo Native", new TestContext());
			testStarted = true;
			signOut();
			clearSignInScreen();
			productSearchAddToBag();
			verifyBag();
			takeSafeScreenshot();
			reportiumClient.testStop(TestResultFactory.createSuccess());
			System.out.println("- - Test Completed Successfully " + getDeviceModel());	
		} 
		catch (NoSuchElementException e)
		{				
			takeSafeScreenshot();
			System.out.println(getStackTraceAsString(e));
			reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", e));
			throw(e);				
		} 
		catch (Exception ex)
		{
			takeSafeScreenshot();
			System.out.println(getStackTraceAsString(ex));
			if(testStarted)
			{
				reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", ex));
			}
			throw(ex);
		}
		finally 
		{ 
			try
			{ 
				testTearDown(context); 				
			} 
			catch(Exception ex) {} 		
		}
	}
	
	@Step("Clear Sign In Screen")
	private void clearSignInScreen()
	{
		reportiumClient.testStep("step: Clear Sign In Screen");
		System.out.println("- - Clear Sign In Screen " + getDeviceModel());			
		
		driver.findElementByXPath(UltaObjects.NativeCloseSignInButton).click();
		driver.findElementByXPath(UltaObjects.NativeMenuHome).click();		
	}
	
	@Step("Verify Bag")
	protected void verifyBag()
	{		
		reportiumClient.testStep("step: Verify Bag");
		System.out.println("- - Verify Bag " + getDeviceModel());	
		
		String bagCount = "";
		
		//Verify only 1 item in bag
		if(isAndroid())
		{
			bagCount = driver.findElementByXPath(UltaObjects.NativeBagCountObjectAndroid).getText();
		}
		else
		{
			WebElement element = driver.findElementByXPath(UltaObjects.NativeBagCountObjectiOS);
			bagCount = driver.findElementByXPath(UltaObjects.NativeBagCountObjectiOS).getAttribute("value");			
		}
		
		softAssert.assertTrue(bagCount.equals("1"), "Unexpected bag count of:" + bagCount);		
	}	
}
