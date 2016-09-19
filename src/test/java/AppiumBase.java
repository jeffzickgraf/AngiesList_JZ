package test.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
import test.resources.Constants;
import test.resources.Resources;
import test.resources.ALObjects;
import test.resources.Constants.OSType;

public class AppiumBase extends TestBase {
		AppiumDriver driver;
		Boolean driverWasClosed = false;
		ReportiumClient reportiumClient;
		Boolean testStarted = false;
		Constants.OSType osType; 
		Boolean networkVirtualizationStarted = false;
		Boolean useWindTunnel;
	
		@Parameters({ "targetEnvironment", "nvProfile", "context", "windTunnel" })
		@BeforeTest
		protected void beforeTest(String targetEnvironment, String nvProfile, String context, String windTunnel) throws IOException{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			try {
				switch (targetEnvironment) {	
				case "iPhone 6s":  //use for omni-channel
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "AA7EEEAADD92242C665D2807B538BDACFAA5A0DB"); //Jeff iPhone
					break;
				case "iPhone 6s shared":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "E20DE68F3D3554B90AB5503E57A28BE0270AF70D"); // iPhone 6s shared
					break;	
				case "iPhone 6 Plus":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "8F46591E26620649A03F66F3693FC3DBC719F01F"); //shared iphone 6 plus
				break;			
				case "iPhone 5":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName","EBB620E023B3FA0A72CAF31B46A21A51213C590E");
					break;
				case "iPad Air 2":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "BA77631CDD65B0124F241488209BB741F30AA98A"); //Shared iPad 2 Air
					break;
				case "iPad Pro":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "2FC00D086052C606D2D57EA757B7450A216D3832"); //Shared iPad Pro
					break;
				case "iPad Mini":
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "A7493312BB4627B1F1D79F6426581A84AE273F9C"); //shared mini
					break;
				case "Android N":
					capabilities.setCapability("platformName","Android");
					capabilities.setCapability("deviceName", "HT58ZJT00147");  //tablet
					break;
				case "Galaxy Note Pro":
					capabilities.setCapability("platformName", "Android");
					capabilities.setCapability("deviceName", "52003C354EAE122D"); // Galaxy  Note Pro				
					break;
				case "Galaxy S7 Edge":
					capabilities.setCapability("platformName","Android");				
					capabilities.setCapability("deviceName", "C538899C"); //shared
					break;			
				case "Nexus 5":
					capabilities.setCapability("platformName", "Android");
					capabilities.setCapability("deviceName","067C0546439C8F54");				
					break;	
				case "Galaxy S6":
					capabilities.setCapability("platformName","Android");				
					capabilities.setCapability("deviceName", "05157DF53B1BA11F"); //Jeffs galaxy 6
					break;
				}
				
				capabilities.setCapability("user", Constants.USERNAME);
				capabilities.setCapability("password", Constants.PASSWORD);			
				capabilities.setCapability("newCommandTimeout", "120");
				capabilities.setCapability("automationName", "Appium");
				capabilities.setCapability("scriptName", "AL Appium");
				
				if(windTunnel.toString() != null && !windTunnel.isEmpty())
				{	
					capabilities.setCapability("windTunnelPersona", windTunnel);
				}
				
				if(capabilities.getCapability("platformName").toString() == "iOS") {
					osType = osType.IOS;
					driver = new IOSDriver(new Resources().getCloudUrl(), capabilities);
				
				} 
				else {				
					osType = osType.ANDROID;
					driver = new AndroidDriver(new Resources().getCloudUrl(), capabilities);
				}
				
				driver.context(context);
				driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT, TimeUnit.SECONDS);
				
				// Reporting client setup
				Job job = new Job();
				job.setName(Constants.BUILDCONFIGURATION);
				job.setNumber(Integer.parseUnsignedInt(Constants.BUILDNUMBER));
				
				PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project(getTestName(), "1.0"))
				    .withContextTags(getTags(windTunnel))
				    .withWebDriver(driver)
				    .withJob(job)
				    .build();
				reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
				
				Map<String, Object> nvProfileOptions = new HashMap<>();
				switch (nvProfile.toString()) {
					case "none":
						break;
					case "unstable":			
						nvProfileOptions.put("packetLoss", "3");
						nvProfileOptions.put("packetCorruption", "3");
						nvProfileOptions.put("packetDuplication", "2");
						nvProfileOptions.put("packetReordering", "10");
						driver.executeScript("mobile:vnetwork:start", nvProfileOptions);
						networkVirtualizationStarted = true;
						break;
					case "veryunstable":
						nvProfileOptions.put("packetLoss", "5");
						nvProfileOptions.put("packetCorruption", "5");
						nvProfileOptions.put("packetDuplication", "5");
						nvProfileOptions.put("packetReordering", "20");
						driver.executeScript("mobile:vnetwork:start", nvProfileOptions);
						networkVirtualizationStarted = true;
						break;				
				}			
				
			} catch (Exception e) {
				System.out.println("Error trying to acquire driver for device " + targetEnvironment + " Error: " + getStackTraceAsString(e));	
			}		
			
			try
			{
				System.out.println("Close and Open App " + getDeviceModel());	
				
				closeApp(context);
				openApp(context);
				
				Thread.sleep(3000);
				//if the "Rate AL" popup appears, dismiss
				if(isAndroid() && textCheckpoint("Rate Angies List", 6))
				{
					buttonClick("No Thanks", false, false);
				}
				else
				{
					if(textCheckpoint("Rate Angies List", 6))
					{
						buttonClick("No, Thanks", false, false);
					}
				}				
			}
			catch(Exception ex)
			{
				System.out.println("Error trying to close or open app for: " + targetEnvironment + " Error:" + getStackTraceAsString(ex));
			}			
		}
		
		protected String getTestName()
		{
			return "AL Native Demo";
		}
	
		protected String[] getTags(String windTunnel) {
			List<String> tags = getAdditionalTags(new ArrayList<String>()); 
			tags.add("AL Native");
			
			if(windTunnel.toString() != null && !windTunnel.isEmpty())
			{				
				tags.add(windTunnel);
			}
			
			String[] tagsArray = new String[tags.size()];

			for (int i = 0; i < tags.size(); i++) {
			    tagsArray[i] = tags.get(i);
			}
			
			return tagsArray;
		}
		
		//Override this in derived classes
		protected List<String> getAdditionalTags(List<String> tagListToAddTo)
		{
			return tagListToAddTo;
		}


		protected void openApp(String context) {
			if (context.equals("NATIVE_APP")) {
				Map<String, Object> params9 = new HashMap<>();
				params9.put("name", Constants.APP_NAME);
				driver.executeScript("mobile:application:open", params9);
			}
		}
		
		@AfterTest
		protected void closeWebDriver() throws SessionNotFoundException, IOException {
			// make sure web driver is closed
			try{
				if ( ((RemoteWebDriver) driver).getSessionId() != null && !driverWasClosed) {
					driver.close();
					driverWasClosed = true;
					}
					driver.quit();
				}	
			catch (SessionNotFoundException e) {}
		}
		
		@Step("Tear Down and Download Report")
		protected void testTearDown(String context) throws Exception {
			if (driver != null) {								
				try {
					closeApp(context);	
					
					if(networkVirtualizationStarted)
					{
						Map<String, Object> params2 = new HashMap<>();
						driver.executeScript("mobile:vnetwork:stop", params2);			
					}
					
					driver.close();
					driverWasClosed = true;
					if(testStarted)
					{	
						downloadWTReport();
						//downloadReport("html");
						downloadReportiumReportLink();
					}
				} catch (Exception e) {}
				driver.quit();
			}
		}
		
		 @Attachment
         protected byte[] downloadWTReport() {
                try
                {
				 	String reportUrl = (String)driver.getCapabilities().getCapability("windTunnelReportUrl");
	                String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
	                returnString = returnString + reportUrl + "\"></head><body /></html>";
	
	                return returnString.getBytes();
                }
                catch(Exception ex)
    			{
    				System.out.println("Error trying to download WindTunnel report link. " + ex.getMessage());	
    				String empty = "";
    				return empty.getBytes();
    			}
        }
		
		@Attachment
		protected byte[] downloadReport(String type) throws IOException
		{	try{
				String command = "mobile:report:download";
				Map<String, String> params = new HashMap<>();
				params.put("type", type);
				String report = (String)((RemoteWebDriver) driver).executeScript(command, params);
				byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
				return reportBytes;
			}
			catch(Exception ex)
			{
				System.out.println("Error trying to download regular report. " + ex.getMessage());				
				String empty = "";
				return empty.getBytes();
			}
		}	
		
		@Attachment
		protected byte[] downloadReportiumReportLink() throws IOException
		{	
			try
			{
				String reportURL = reportiumClient.getReportUrl();			
				String reportHTML = "<html><body><a href='"+ reportURL + "'>" + reportURL + "</a></body></html>";			
				return reportHTML.getBytes();
			}
			catch(Exception ex)
			{
				String empty = "";
				return empty.getBytes();
			}
		}
		
		protected void closeApp(String context) {
			if (context.equals("NATIVE_APP")) {
			
				Map<String, Object> params8 = new HashMap<>();
				params8.put("name", Constants.APP_NAME);
				try{
					driver.executeScript("mobile:application:close", params8);
				} catch (WebDriverException e) { }
			}
		}
		
		public Boolean textCheckpoint(String textToFind, Integer timeout) {
			perfectoCommand.clear();
			perfectoCommand.put("content", textToFind);
			perfectoCommand.put("timeout", timeout);
			Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
			Boolean resultBool = Boolean.valueOf(result.toString());
			perfectoCommand.clear();
			return resultBool;
		}	
		
		public Boolean textCheckpointWithScroll(String textToFind) {
			perfectoCommand.clear();
			perfectoCommand.put("content", textToFind);
			perfectoCommand.put("scrolling", "scroll");
			perfectoCommand.put("next", "SWIPE_UP");
			Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
			Boolean resultBool = Boolean.valueOf(result.toString());
			perfectoCommand.clear();
			return resultBool;
		}	
		
		public void buttonClick(String textToClick, Boolean scroll, Boolean scrollUp ) 
		{			
			perfectoCommand.clear();
			perfectoCommand.put("label", textToClick);
			if(scroll)
			{
				perfectoCommand.put("scrolling", "scroll");
				if(scrollUp)
				{
					perfectoCommand.put("next", "SWIPE_UP");			
				}
			}
			driver.executeScript("mobile:button-text:click", perfectoCommand);
			
			perfectoCommand.clear();
		}
		
		public String getStackTraceAsString(Exception ex)
		{
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			return stringWriter.toString();
		}

		
		public String getDeviceModel()
		{
			Map params = new HashMap<>();         
			params.put("property", "model");
			String properties = (String) driver.executeScript("mobile:handset:info", params);
			return properties + " Native";
		}
		
		// Wind Tunnel: Gets the user experience (UX) timer
		public long timerGet(String timerType) {
			 String command = "mobile:timer:info";
			 Map<String,String> params = new HashMap<String,String>();
			 params.put("type", timerType);
			 long result = (long)driver.executeScript(command, params);
			 	return result;
		}
	    
		//Wrapping in try catch so we can continue execution if it fails.
		@Attachment
		public byte[] takeSafeScreenshot()
		{
			try
			{
				if(!testStarted)
					return null;
				
			     return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			}
			catch(Exception ex)
			{
				System.out.println(getStackTraceAsString(ex));
			}
			return null;
		}
	    
	    /*protected void openNavMenuIfNeeded()
		{
			if(osType==OSType.ANDROID)
			{
				driver.findElementByXPath(ALObjects.NativeAndroidMenuButton).click();
			}
		}*/
	    
		public Boolean isAndroid()
	    {
	    	return osType==OSType.ANDROID;
	    }
	    
	    //This should get overridden in our derived data agnostic classes
	    //leaving empty here as if empty it just uses TestNG default
		public String getWindTunnelPersona(){
	    	return "";
	    }
}
