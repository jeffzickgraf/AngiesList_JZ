package test.java;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import ru.yandex.qatools.allure.annotations.Step;
import test.java.jsSelect.JSSelect;
import test.resources.Constants;
import test.resources.UltaObjects;
import test.resources.WindTunnelUtils;

public class UltaDemoWebOmnichannel extends UltaDemoWeb {
		Integer bagCount = 0;
		Integer itemCountToAddToBag = 1;
		
		//Override this in derived classes
		protected List<String> getAdditionalTags(List<String> tagListToAddTo)
		{
			tagListToAddTo.add("Omnichannel");
			return tagListToAddTo;
		}
		
		@Parameters({ "context" })
		@Test
		public void omniTestStart(String context) throws Exception {			
			try{
				reportiumClient.testStart("Ulta Omni-Channel Add Product Web", new TestContext());
				testStarted = true;
				openURL();
				login();
				emptyBag();
				checkBag();
				productSearchAddToBag();
				verifyBag();												
				reportiumClient.testStop(TestResultFactory.createSuccess());
				System.out.println("- - Test Completed Successfully " + getDeviceModel());		
			} catch (NoSuchElementException e){
				if(testStarted)
				{
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", e));
				}
				takeSafeScreenshot();
				System.out.println(getStackTraceAsString(e));
				
			}
			catch (Exception ex)
			{				
				if(testStarted)
				{
					reportiumClient.testStop(TestResultFactory.createFailure("Test stop failure.", ex));
				}
				System.out.println(getStackTraceAsString(ex));
				takeSafeScreenshot();
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
		public void omniVerifyBagUpdate(String context) throws Exception {			
			try{
				reportiumClient.testStart("Ulta Omni-Channel Verify Bag Updates", new TestContext());
				testStarted = true;
				openURL();
				login();
				verifyWebBagUpdatesCorrectly();
				reportiumClient.testStop(TestResultFactory.createSuccess());
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
		
		
		@Step("Open URL")
		protected void openURL() {
			reportiumClient.testStep("step: Open web URL");
			System.out.println("- - Opening URL " + getDeviceModel());
			driver.get("http://www.ulta.com");
			takeSafeScreenshot();
		}

		@Step("Logout")
		protected void logout() throws InterruptedException
		{
			reportiumClient.testStep("step: Logout");
			System.out.println("- - Logging Out " + getDeviceModel());
			try{
				//reduce the timer so this is quicker
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				WebElement element = driver.findElementByXPath(UltaObjects.WebSignInName);
				if(element.getText().isEmpty())
				{
					//Not logged in
					return;
				}
			
				driver.get(Constants.LOGOUTURL);
				//verify:
				Assert.assertTrue(textCheckpoint(Constants.RETURNINGGUESTS, 15), "Expected to see Returning Guests and login page.");
			}catch(NoSuchElementException nsee)
			{
				//Its ok - we weren't logged in
				System.out.println("SignInName not found " + getDeviceModel());
			}
			finally {
				//set back implicit timer to default
				driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT, TimeUnit.SECONDS);
			}
						
			driver.get("http://www.ulta.com");
			Thread.sleep(2000);
		}
		
		@Step("Login")
		protected void login() throws InterruptedException
		{	
			WebElement element;
			
			driver.get(Constants.LOGOUTURL);
			reportiumClient.testStep("step: Login");
			System.out.println("- - Loging In " + getDeviceModel());
			
			Thread.sleep(3000); //need just a little time for the page to load.
			//verify on login screen and then login
			softAssert.assertTrue(textCheckpoint(Constants.RETURNINGGUESTS, 15), "Expected to see Returning Guests and login page.");
			
			element = driver.findElementByXPath(UltaObjects.WebSignInUsername);
			element.clear();
			element.sendKeys(Constants.ULTAUSERNAME);
			
			element = driver.findElementByXPath(UltaObjects.WebSignInPassword);
			element.clear();
			element.sendKeys(Constants.ULTAPASSWORD);
			
			driver.findElementByXPath(UltaObjects.WebSignInSubmitButton).click();
			
			//verify on post login landing screen
			Assert.assertTrue(textCheckpoint("Welcome " + Constants.ULTAACCOUNTFIRSTNAME, 25, true, false),"Expected to be signed in but couldn't find.");
			takeWindTunnelTimer("Logged In", 8000);
			takeSafeScreenshot();			
			softAssert.assertAll();
			takeWindTunnelPointOfInterest("Successfully logged in.", WindTunnelUtils.SUCCESS);
		}
		
		@Step("Check Bag")
		protected void checkBag() throws InterruptedException
		{	
			reportiumClient.testStep("step: checkBag");
			System.out.println("- - Checking bag count " + getDeviceModel());
			
			String bagCountText = driver.findElementByXPath(UltaObjects.WebBagCountIndicator).getText();			
			bagCount = Integer.valueOf(bagCountText);
			
			System.out.println("- - Bag count: " + bagCount + " " + getDeviceModel());	
			
			if(bagCount == 5)
			{
				//Only 5 of our product are allowed so remove
				driver.get("http://www.ulta.com/ulta/cart/cart.jsp");
				Thread.sleep(1500);
				buttonClick("remove", true, true, 3);
				bagCount = 0;
			}			
		}
		
		@Step("Product Search Add To Bag")
		protected void productSearchAddToBag() throws Exception
		{
			reportiumClient.testStep("step: Product Search Add to Bag");
			System.out.println("- - Product Search Add to Bag " + getDeviceModel());			
			
			String searchTextbox = UltaObjects.WebSearchTextBox;
			String searchSubmit = UltaObjects.WebSearchSubmit;
			if(isMobile && !isTablet)
			{
				searchTextbox = UltaObjects.WebSearchTextBoxMobile;
				searchSubmit = UltaObjects.WebSearchSubmitMobile;
			}
			
			driver.findElementByXPath(searchTextbox).click();
			driver.findElementByXPath(searchTextbox).sendKeys(Constants.ULTAPRODUCTSEARCHWEB);
			driver.findElementByXPath(searchSubmit).click();
			
			Thread.sleep(1500);
			Boolean checkPointResult;
			
			checkPointResult = textCheckpoint(Constants.ULTAPRODUCTSEARCHWEB, 15, false, true);
			softAssert.assertTrue(checkPointResult,	"Unable to identify results of product search.");
			if(checkPointResult)
				System.out.println(String.format("Found product %s %s",Constants.ULTAPRODUCTSEARCHWEB, getDeviceModel()));
			
			/*mini and pro
			checkPointResult = textCheckpoint(Constants.ULTAPRODUCTSEARCHWEBSKU, 15, false, true);
			softAssert.assertTrue(checkPointResult, String.format("Unable to identify product sku: %s %s", 
					Constants.ULTAPRODUCTSEARCHWEBSKU, getDeviceModel()));
			if(checkPointResult)
				System.out.println(String.format("Found product sku %s %s",Constants.ULTAPRODUCTSEARCHWEBSKU, getDeviceModel()));
			*/
			
			if(isMobile)
			{
				JSSelect quantitySelect = new JSSelect();
				quantitySelect.findById(driver, "dropdown-quantity-select").selectOptionByValue(itemCountToAddToBag.toString());
			}
			else
			{
				String script = String.format("document.getElementById('%s').value='%s';", "dropdown-quantity-select", itemCountToAddToBag.toString());
				driver.executeScript(script);
			}
			
			System.out.println("Adding to bag " + getDeviceModel());
			
			driver.findElementByXPath(UltaObjects.WebAddToBagSubmit).click();
			//buttonClick("add to bag", true, true, 3);
									
			//verify prompt to add to bag
			softAssert.assertTrue(textCheckpoint("View Bag", 35), 
					"Expected 'View Bag' to be found on Add to Bag prompt but did not find prompt.");
			
			if(isMobile)
			{
				if(isTablet)
				{
					rotateDevice(Constants.Rotation.PORTRAIT);
				}
				else{
					rotateDevice(Constants.Rotation.LANDSCAPE);
				}
			}
			
			buttonClick("View Bag & Checkout", false, false, 0);	
			softAssert.assertAll();

			//revert back
			if(isMobile)
			{
				if(isTablet)
				{
					rotateDevice(Constants.Rotation.LANDSCAPE);
				}
				else{
					rotateDevice(Constants.Rotation.PORTRAIT);
				}
			}		
		}
		
		@Step("Verify Bag")
		protected void verifyBag() throws InterruptedException 
		{			
			System.out.println("- - Verify bag " + getDeviceModel());	
			reportiumClient.testStep("step: Verify bag");
			//Page load assert
			
			softAssert.assertTrue(textCheckpoint("My Bag", 10),"Bag Checkout Page Verification");
			//Take time to load
			
			takeWindTunnelTimer("My Bag - checkout page found", 8000);
			
			//verify top nav bag counter
			String bagCountText = driver.findElementByXPath(UltaObjects.WebBagCountIndicator).getText();	
			Integer expectedCount = bagCount + itemCountToAddToBag;
			softAssert.assertEquals(Integer.valueOf(bagCountText), expectedCount); 
			bagCount = expectedCount;
			String itemWord = "item";
			if(expectedCount > 1)
				itemWord = "items";
			
			//Verify contents
			softAssert.assertTrue(textCheckpoint(expectedCount + " " + itemWord, 10),
					"Unexpected bag count. Expected " + expectedCount);
			
			takeSafeScreenshot();
			softAssert.assertAll();
			takeWindTunnelPointOfInterest("Bag verified successfully", WindTunnelUtils.SUCCESS);
		}			

		@Step("Verify Web Bag Updates")
		protected void verifyWebBagUpdatesCorrectly() throws InterruptedException 
		{			
			System.out.println("- - Verify Web Bag Updates " + getDeviceModel());	
			reportiumClient.testStep("step: Verify Web Checkout Bag Updates");

			String bagCountText = driver.findElementByXPath(UltaObjects.WebBagCountIndicator).getText();	
			
			driver.get("http://www.ulta.com/ulta/cart/cart.jsp");
			Thread.sleep(3500);
			
			Assert.assertTrue(textCheckpoint("My Bag", 10),"Bag Checkout Page Verification");
						
			//verify top nav bag counter
			bagCountText = driver.findElementByXPath(UltaObjects.WebBagCountIndicator).getText();	
			Integer expectedCount = 2;
			Assert.assertEquals(Integer.valueOf(bagCountText), expectedCount); 
			bagCount = expectedCount;
			String itemWord = "item";
			if(expectedCount > 1)
				itemWord = "items";
			
			//Verify contents
			Assert.assertTrue(textCheckpoint(expectedCount + " " + itemWord, 10),
					"Unexpected bag count. Expected " + expectedCount);
			
			if(!isMobile)
			{
				//scroll products into view
				WebElement element = driver.findElementByXPath("//h4[contains(text(),'ORDER SUMMARY')]");
				Actions actions = new Actions(driver);
	            actions.moveToElement(element);
	            actions.perform();
	            Thread.sleep(1000);
			}
            
			takeSafeScreenshot();			
			takeWindTunnelPointOfInterest("Updated Desktop Web bag verified successfully", WindTunnelUtils.SUCCESS);
		}			
		
	
}
