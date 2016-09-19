package test.java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class DesktopOnlyTests extends AngiesListSeleniumBase {
	
		@Parameters({ "context" })
		@Test
		public void verifyHoverTest(String context) throws Exception {			
			try{				
				reportiumClient.testStart("AL Hover Test", new TestContext());
				testStarted = true;
				driver.manage().window().maximize();
				openALSite();
				login(35);
				verifyHover();
				
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
					System.out.println("- - testStop hit NSEE");
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
					System.out.println("- - testStop hit general ex");		
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
		public void verifyBusinessCenter(String context) throws Exception {			
			try{				
				reportiumClient.testStart("AL Business Center Test", new TestContext());
				testStarted = true;
				driver.manage().window().maximize();
				openALSite();
				verifyBusinessCenter();
				
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
					System.out.println("- - testStop hit NSEE");
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
					System.out.println("- - testStop hit general ex");		
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
		
		
		
		@Step("Verify Hover")
		protected void verifyHover()
		{	
			reportiumClient.testStep("step: VerifyHover");
			System.out.println("- - Verifying Hover " + getDeviceModel());
			
			handleChromePopupIfNeeded();
			
			WebElement element = driver.findElementByXPath(ALObjects.WebHoverProfile);
			Actions action  = new Actions(driver);
			action.moveToElement(element).perform();
			action.moveByOffset(10, 10).perform();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map<String, Object> checkPtParams = new HashMap<>();
			checkPtParams.put("content", "\"Reviews\" \"Purchase\"");
			checkPtParams.put("screen.top", "0%");
			checkPtParams.put("screen.height", "44%");
			checkPtParams.put("screen.left", "73%");
			checkPtParams.put("screen.width", "25%");
			checkPtParams.put("timeout", 40);
			checkPtParams.put("target", "all");
			Object result = driver.executeScript("mobile:checkpoint:text", checkPtParams);
			
			Boolean resultBool = Boolean.valueOf(result.toString());
			Assert.assertTrue(resultBool, "Expected to see hover items but couldn't find");
			
			//verify the sign out button
			Map<String, Object> signOutParams = new HashMap<>();
			signOutParams.put("content", "Sign Out");
			signOutParams.put("screen.top", "0%");
			signOutParams.put("screen.height", "50%");
			signOutParams.put("screen.left", "73%");
			signOutParams.put("screen.width", "25%");
			signOutParams.put("inverse", "yes");
			driver.executeScript("mobile:checkpoint:text", signOutParams);
			
			Boolean signOutBool = Boolean.valueOf(result.toString());
			Assert.assertTrue(signOutBool, "Expected to see hover item for Sign Out but couldn't find");
			takeSafeScreenshot();			
		}
		
		@Step("Verify Business Center")
		protected void verifyBusinessCenter()
		{	
			reportiumClient.testStep("step: Verify Business Center");
			System.out.println("- - Verify Business Center " + getDeviceModel());
			
			handleChromePopupIfNeeded();

			String winHandleBefore = driver.getWindowHandle();
				
			Map<String, Object> busCenterParams = new HashMap<>();
			busCenterParams.put("label", "Visit Business Center");
			busCenterParams.put("screen.top", "0%");
			busCenterParams.put("screen.height", "30%");
			busCenterParams.put("screen.left", "60%");
			busCenterParams.put("screen.width", "40%");
			busCenterParams.put("timeout", 40);
			busCenterParams.put("inverse", "yes");
			driver.executeScript("mobile:button-text:click", busCenterParams);
		
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			driver.close();
			
			for(String winHandle : driver.getWindowHandles()){
	            
				if(winHandle != winHandleBefore)
					driver.switchTo().window(winHandle);
	        }
			
			driver.manage().window().maximize();
	
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			//verify the top links are on the page
			Map<String, Object> checkPtParams = new HashMap<>();
			checkPtParams.put("content", "\"How It Works\" \"Testimonials\" \"News\" \"LeadFeed\" \"Contact Us\" ");
			checkPtParams.put("screen.top", "0%");
			checkPtParams.put("screen.height", "30%");
			checkPtParams.put("screen.left", "30%");
			checkPtParams.put("screen.width", "70%");
			checkPtParams.put("timeout", 40);
			checkPtParams.put("target", "all");
			busCenterParams.put("inverse", "yes");
			Object result = driver.executeScript("mobile:checkpoint:text", checkPtParams);
			
			Boolean busLinksBool = Boolean.valueOf(result.toString());
			Assert.assertTrue(busLinksBool, "Expected to see Business Center Links but couldn't find");
			takeSafeScreenshot();
			
			//verify the middle links are on the page
			Map<String, Object> claimParams = new HashMap<>();
			claimParams.put("content", "Claim Your Profile");
			claimParams.put("screen.top", "40%");
			claimParams.put("screen.height", "34%");
			claimParams.put("screen.left", "26%");
			claimParams.put("screen.width", "52%");
			claimParams.put("timeout", 40);
			result = driver.executeScript("mobile:checkpoint:text", claimParams);
			
			
			Boolean claimLinkBool = Boolean.valueOf(result.toString());
			Assert.assertTrue(claimLinkBool, "Expected to see Claim Your Profile but couldn't find");
			
			//Now for Sign In			
			Map<String, Object> signInParams = new HashMap<>();
			signInParams.put("content", "Sign In >");
			signInParams.put("inverse", "yes");
			signInParams.put("screen.top", "27%");
			signInParams.put("screen.height", "40%");
			signInParams.put("screen.left", "29%");
			signInParams.put("screen.width", "43%");
			result = driver.executeScript("mobile:checkpoint:text", signInParams);
			
			Boolean signInLinkBool = Boolean.valueOf(result.toString());
			Assert.assertTrue(signInLinkBool, "Expected to see Sign In Link but couldn't find");
			
			takeSafeScreenshot();
			
		}
		
		private void handleChromePopupIfNeeded()
		{
			if(!getDeviceModel().toLowerCase().contains("chrome"))
			{
				return;
			}
			
			Boolean resultBool = false;
			
			//deal with popup after signin
			Map<String, Object> savePopup = new HashMap<>();
			savePopup.put("content", "Do you want Google Chrome");
			savePopup.put("screen.top", "0%");
			savePopup.put("screen.height", "31%");
			savePopup.put("screen.left", "56%");
			savePopup.put("screen.width", "44%");
			Object saveResult = driver.executeScript("mobile:text:find", savePopup);
			resultBool = Boolean.valueOf(saveResult.toString());
			
			if(resultBool) //found the popup so dismiss
			{
				Map<String, Object> dismissSave = new HashMap<>();
				dismissSave.put("label", "Never");
				dismissSave.put("screen.top", "0%");
				dismissSave.put("screen.height", "29%");
				dismissSave.put("screen.left", "71%");
				dismissSave.put("screen.width", "29%");
				driver.executeScript("mobile:button-text:click", dismissSave);
			}
		}
		
}
