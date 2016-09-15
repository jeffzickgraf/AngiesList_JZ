package test.java;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ru.yandex.qatools.allure.annotations.Step;
import test.resources.ALObjects;
import test.resources.Constants;
import test.resources.WindTunnelUtils;

public class AngiesListSeleniumBase extends SeleniumBase{

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
	protected void login(Integer timeoutInSeconds) throws InterruptedException
	{	
		WebElement element;
		
		reportiumClient.testStep("step: Login");
		System.out.println("- - Logging In " + getDeviceModel());
		
		if(isMobile)
		{
			if(textCheckpoint("Welcome. " + Constants.ALACCOUNTFIRSTNAME, 8))
			{
				System.out.println("- - Already logged in - returning " + getDeviceModel());
				return;
			}
		}
		else
		{ 
			//For desktop - reduce the haystack size
			Map<String, Object> checkPtParams = new HashMap<>();
			checkPtParams.put("content", "Welcome. " + Constants.ALACCOUNTFIRSTNAME);
			checkPtParams.put("screen.top", "0%");
			checkPtParams.put("screen.height", "50%");
			checkPtParams.put("screen.left", "0%");
			checkPtParams.put("screen.width", "100%");
			checkPtParams.put("timeout", 30);;
			Object result = driver.executeScript("mobile:checkpoint:text", checkPtParams);
			
			Boolean resultBool = Boolean.valueOf(result.toString());
			if(resultBool)
			{
				System.out.println("- - Already logged in - returning " + getDeviceModel());
				return;
			}
		}
		
		
		element = driver.findElementByXPath(ALObjects.WebSignInUsername);
		element.clear();
		element.sendKeys(Constants.ALUSERNAME);
		
		element = driver.findElementByXPath(ALObjects.WebSignInPassword);
		element.clear();
		element.sendKeys(Constants.ALPASSWORD);
		
		driver.findElementByXPath(ALObjects.WebSignInSubmitButton).click();
		
		//verify on post login landing screen
		Assert.assertTrue(textCheckpoint("Welcome. " + Constants.ALACCOUNTFIRSTNAME, timeoutInSeconds, true, false),"Expected to be signed in but couldn't find Welcome message.");
		takeWindTunnelTimer("Logged In", 8000);
		takeSafeScreenshot();			
		softAssert.assertAll();
		takeWindTunnelPointOfInterest("Successfully logged in.", WindTunnelUtils.SUCCESS);
	}
	
	@Step("Open Angies List Site")
	protected void openALSite() {
		reportiumClient.testStep("step: Open web URL");
		System.out.println("- - Opening URL " + getDeviceModel());
		driver.get("https://member.angieslist.com/member");
		takeSafeScreenshot();
	}
	
}
