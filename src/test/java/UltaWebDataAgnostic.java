package test.java;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.TestResult;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;
import ru.yandex.qatools.allure.annotations.Step;
import test.resources.Constants;
import test.resources.UltaObjects;
import test.resources.WindTunnelUtils;

public class UltaWebDataAgnostic extends UltaDemoWeb {
		
		@Parameters({ "context" })
		@Test
		public void agnosticTest(String context) throws Exception {
			
			try{
				System.out.println("Starting Ulta Web Agnostic test");
				reportiumClient.testStart("Ulta Web Agnostic", new TestContext());
				testStarted = true;
				openURL();
				logout();
				emptyBag();
				productSearchAddToBag();
				verifyBag();
				takeSafeScreenshot();
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
				takeSafeScreenshot();
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
		
}
