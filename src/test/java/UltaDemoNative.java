package test.java;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;
import ru.yandex.qatools.allure.annotations.Step;
import test.resources.Constants;
import test.resources.UltaObjects;
import test.resources.WindTunnelUtils;

public class UltaDemoNative extends AppiumBase  {
	
	
	@Parameters({"context"})
	@Test
	public void test(String context) throws Exception {			
		try
		{
			reportiumClient.testStart("Ulta Demo Native", new TestContext());
			testStarted = true;
			navigateToAccountLogin();
			login();
			navigateToShop();			
			signOut();
			
			reportiumClient.testStop(TestResultFactory.createSuccess());
			System.out.println("- - Test Completed Successfully " + getDeviceModel());					
		} 
		catch (NoSuchElementException e)
		{				
			//try to take a snapshot
			takeSafeScreenshot();
			System.out.println(getStackTraceAsString(e));
			reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", e));
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
		finally 
		{ 
			try
			{ 
				testTearDown(context); 				
			} 
			catch(Exception ex) {} 		
		}
	}
	
	@Step("SignOut")
	protected void signOut() throws InterruptedException
	{
		reportiumClient.testStep("step: SignOut");
		System.out.println("- - Sign Out " + getDeviceModel());
				
		if(isAndroid())
		{
			//Need to open side menu
			driver.findElementByXPath(UltaObjects.NativeAndroidMenuButton).click();
			buttonClick("My Account", false, false);
			//driver.findElementByXPath(UltaObjects.NativeAndroidMenuMyAccount).click();
			
			if(textCheckpoint("Sign In", 8))
				return;				
		}
		else
		{
			//driver.findElementByXPath(UltaObjects.NativeMenuMore).click();
			Map<String, Object> params1 = new HashMap<>();
			params1.put("label", "more");
			params1.put("screen.top", "85%");
			params1.put("screen.height", "15%");
			params1.put("screen.left", "70%");
			params1.put("screen.width", "30%");
			driver.executeScript("mobile:button-text:click", params1);
			
			//Give just a sec here to wait for app to catch up
			Thread.sleep(2500);
			
			if(textCheckpoint("Not Signed In", 8) == false)
			{
				//need to logout - go to account detail
				driver.findElementByXPath(UltaObjects.NativeViewAccountLink).click();					
			}
			else
			{
				buttonClick("Not Signed In", false, false);
				return;
			}
		}
		
		buttonClick("Sign Out", true, true);
		
		takeSafeScreenshot();
		
		//Verify alert text is shown
		textCheckpoint("Are you sure you want to", 15);
		
		driver.findElementByXPath(UltaObjects.NativeSignOutConfirmButton).click();
	}


	@Step("navigate-to-login")
	protected void navigateToAccountLogin() throws Exception
	{
		signOut();
		
		//When logging out - Android takes you home. iOS stays on "More"
		if(isAndroid())
		{
			//Need to open side menu
			driver.findElementByXPath(UltaObjects.NativeAndroidMenuButton).click();
			driver.findElementByXPath(UltaObjects.NativeAndroidMenuMyAccount).click();			
		}
		else
		{
			buttonClick("Not Signed In", false, false);
		}
	}

	@Step("login")
	protected void login() throws Exception
	{						
		reportiumClient.testStep("step: Login");
		System.out.println("- - Login " + getDeviceModel());
		
		driver.findElementByXPath(UltaObjects.NativeLoginUsername).sendKeys(Constants.ULTAUSERNAME);
		driver.findElementByXPath(UltaObjects.NativeLoginPassword).sendKeys(Constants.ULTAPASSWORD);
		driver.findElementByXPath(UltaObjects.NativeSignInButton).click();
		
		Assert.assertTrue(textCheckpoint(Constants.ULTAACCOUNTFULLNAME, 10), "Customer account name not found");
		takeSafeScreenshot();
					
		long uxTimer1 = timerGet("ux");
		System.out.println("'Measured UX time is: " + uxTimer1);						
		// Wind Tunnel: Add timer to Wind Tunnel Report
		WindTunnelUtils.reportTimer(driver, uxTimer1, 15000, "Checkpoint login time - logged in.", "uxTimer1");
		Thread.sleep(500); //space for report to show POI below
		WindTunnelUtils.pointOfInterest(driver, "Point Of Interest 1: LoggedIn", WindTunnelUtils.SUCCESS);
		takeSafeScreenshot();
	}
	
	@Step("Product Search Add To Bag")
	protected void productSearchAddToBag()
	{		
		reportiumClient.testStep("step: Product Search Add to Bag");
		System.out.println("- - Product Search Add to Bag" + getDeviceModel());			
		
		driver.findElementByXPath(UltaObjects.NativeSearchBeginButton).click();
		driver.findElementByXPath(UltaObjects.NativeSearchTextBox).sendKeys(Constants.ULTAPRODUCTFORSEARCHNATIVE);
		
		if(isAndroid())
		{
			((AndroidDriver)driver).pressKeyCode(66);

			/*//another way to submit items on Android keyboard:
			Map<String, Object> params3 = new HashMap<>();
			params3.put("label", "PRIVATE:TestImages\\Ulta\\SamsungSearchButton.png");
			Object result3 = driver.executeScript("mobile:button-image:click", params3);*/
		}
		else
		{
			driver.findElementByXPath(UltaObjects.NativeSearchSubmitButton).click();
		}
		
		Assert.assertTrue(textCheckpoint(Constants.ULTAPRODUCTFORSEARCHNATIVE, 15), "Unable to identify results of product search.");
		
		buttonClick("Add To Bag", true, true);
		
		//verify prompt to add to bag
		Assert.assertTrue(textCheckpoint("View Bag", 15), "Expected 'View Bag' to be found on Add to Bag prompt but did not find prompt.");
		
		takeSafeScreenshot();
		
		buttonClick("View Bag", false, false);	
	}	

	@Step("Navigate to Shop")
	protected void navigateToShop() 
	{
		reportiumClient.testStep("step: Navigate to Shop");
		System.out.println("- - Navigate to Shop " + getDeviceModel());
		
		//Go Home 1st
		openNavMenuIfNeeded();
		driver.findElementByXPath(UltaObjects.NativeMenuHome).click();
		
		//This Visual Analysis code checks the top 50% of the screen for "Shop" 
		//to make sure we landed on the right screen
		Map<String, Object> shopTextParams = new HashMap<>();
		shopTextParams.put("content", "Shop");
		shopTextParams.put("screen.top", "0%");
		shopTextParams.put("screen.height", "50%");
		shopTextParams.put("screen.left", "0%");
		shopTextParams.put("screen.width", "100%");
		Object checkpointResult = driver.executeScript("mobile:checkpoint:text", shopTextParams);
		Boolean resultBool = Boolean.valueOf(checkpointResult.toString());
		softAssert.assertTrue(resultBool, "Expected to be on the Shop screen but did not find.");			
		takeSafeScreenshot();
	}
}