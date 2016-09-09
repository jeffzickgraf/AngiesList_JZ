package test.java;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import ru.yandex.qatools.allure.annotations.Step;
import test.resources.Constants;
import test.resources.UltaObjects;

public class UltaDemoNativeOmnichannel extends UltaDemoNative {
		Integer bagCount = 0;
		Integer itemCountToAddToBag = 1;
				
		//Overriding to add omni tag
		protected List<String> getAdditionalTags(List<String> tagListToAddTo)
		{
			tagListToAddTo.add("Omnichannel");
			return tagListToAddTo;
		}
		
		//Overriding test name
		protected String getTestName()
		{
			return "Ulta Native Omnichannel";
		}
		
		@Parameters({ "context" })
		@Test
		public void omniTest(String context) throws Exception {			
			try{
				reportiumClient.testStart("Ulta Omni-Channel Add Product Native", new TestContext());
				testStarted = true;
				navigateToAccountLogin();
				login();
				navigateToShop();
				nativeVerifyBag();
				productSearchAddToBag();
				nativeVerifyBagCheckout();				
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
				
		@Step("Verify Bag Native")
		private void nativeVerifyBag()
		{
			reportiumClient.testStep("step: Verify Bag");
			System.out.println("- - Verify Bag " + getDeviceModel());	
			
			String bagCountCheck = "";
						
			//Verify only 1 item in bag
			if(isAndroid())
			{
				bagCountCheck = driver.findElementByXPath(UltaObjects.NativeBagCountObjectAndroid).getText();
			}
			else
			{				
				bagCountCheck = driver.findElementByXPath(UltaObjects.NativeBagCountObjectiOS).getAttribute("value");			
			}
			
			Integer expectedBagCount = itemCountToAddToBag;
						
			Assert.assertTrue(bagCountCheck.equals(expectedBagCount.toString()), "Unexpected bag count of:" + bagCountCheck + "expected 1");		
		}		
		
		@Step("Verify Checkout Native")
		private void nativeVerifyBagCheckout()
		{
			reportiumClient.testStep("step: Verify Checkout Native");
			System.out.println("- - Verify Checkout Native " + getDeviceModel());
			textCheckpointWithScroll(Constants.ULTAPRODUCTFORSEARCHNATIVE);		
		}		
}
