package test.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfectomobile.httpclient.Credentials;
import com.perfectomobile.httpclient.HttpClientException;
import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.ParameterValue;
import com.perfectomobile.httpclient.device.DeviceResult;
import com.perfectomobile.httpclient.device.DevicesHttpClient;
import com.perfectomobile.httpclient.execution.ExecutionsHttpClient;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
import test.resources.Constants;
import test.resources.Resources;
import test.resources.WindTunnelUtils;

public class SeleniumBase extends TestBase {
		
		RemoteWebDriver driver;	
		Boolean isMobile = true;
		Boolean isTablet = false;
		Boolean driverWasClosed = false;
		ReportiumClient reportiumClient;
		Boolean testStarted = false;
		String reportKey;
		Boolean useWindTunnel = true;
		//This is used as a flag to fail the test if a wind tunnel timer fails.
		Boolean windTimerFailed = false;
			
		@Parameters({ "targetEnvironment", "nvProfile", "context", "windTunnel" })
		@BeforeMethod
		public void beforeTest(String targetEnvironment, String nvProfile, String context, String windTunnel) throws IOException{
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
					isTablet = true;
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "BA77631CDD65B0124F241488209BB741F30AA98A"); //Shared iPad 2 Air
					break;
				case "iPad Pro":
					isTablet = true;
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "2FC00D086052C606D2D57EA757B7450A216D3832"); //Shared iPad Pro
					break;
				case "iPad Mini":
					isTablet = true;
					capabilities.setCapability("platformName", "iOS");
					capabilities.setCapability("deviceName", "59755674D0CCBC97A39C07E70527BD6B88B8ED79"); //shared mini
					break;
				case "Android N":
					isTablet = true;
					capabilities.setCapability("platformName","Android");
					capabilities.setCapability("deviceName", "HT58ZJT00147");  //tablet
					break;
				case "Galaxy Note Pro":
					isTablet = true;
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
				case "Desktop Chrome":
					capabilities.setCapability("platformName", "Windows");
					capabilities.setCapability("platformVersion", "7");
					capabilities.setCapability("browserName", "Chrome");
					capabilities.setCapability("browserVersion", "53");
					capabilities.setCapability("resolution", "1440x900");
					capabilities.setCapability("location", "US East");
					ChromeOptions options = new ChromeOptions();
				    options.addArguments("--disable-extensions");
				    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					isMobile = false;
					useWindTunnel = false;
					break;
				
				case "Desktop Chrome Beta":
					capabilities.setCapability("platformName", "Windows");
					capabilities.setCapability("platformVersion", "7");
					capabilities.setCapability("browserName", "Chrome");
					capabilities.setCapability("browserVersion", "beta");
					capabilities.setCapability("resolution", "1440x900");
					capabilities.setCapability("location", "US East");
					ChromeOptions betaOptions = new ChromeOptions();
				    betaOptions.addArguments("--disable-extensions");
				    capabilities.setCapability(ChromeOptions.CAPABILITY, betaOptions);
					isMobile = false;
					useWindTunnel = false;
								
				case "Desktop Win7 IE11":
					capabilities.setCapability("platformName", "Windows");
					capabilities.setCapability("platformVersion", "7");
					capabilities.setCapability("browserName", "Internet Explorer");
					capabilities.setCapability("browserVersion", "11");
					capabilities.setCapability("resolution", "1440x900");
					capabilities.setCapability("location", "US East");
					isMobile = false;
					useWindTunnel = false;
					break;
				case "Desktop Firefox": //omni-channel
					capabilities.setCapability("platformName", "Windows");
					capabilities.setCapability("platformVersion", "8.1");
					capabilities.setCapability("browserName", "Firefox");
					capabilities.setCapability("browserVersion", "47");
					capabilities.setCapability("resolution", "1920x1080");
					capabilities.setCapability("location", "US East");
					isMobile = false;
					useWindTunnel = false;
					break;
				}
				
				if(isMobile)
				{
					capabilities.setCapability("browserName", "mobileOS");
					capabilities.setCapability("automationName", "PerfectoMobile");
				}
				
				capabilities.setCapability("user", Constants.USERNAME);
				capabilities.setCapability("password", Constants.PASSWORD);			
				capabilities.setCapability("newCommandTimeout", "120");
				
				capabilities.setCapability("scriptName", "AL Web");
						
				if(windTunnel.toString() != null && !windTunnel.isEmpty() && isMobile && useWindTunnel)
				{
					capabilities.setCapability("windTunnelPersona", windTunnel);
				}
				
				if (context.equals("WEBVIEW")) {
					driver = new RemoteWebDriver (new Resources().getCloudUrl(), capabilities);
					
					reportKey = (String) driver.getCapabilities().getCapability("reportKey");
					
					if(isMobile)
					{
						switchToContext(driver, context);
					}
				}
				
				driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT, TimeUnit.SECONDS);
				
				// Reporting client setup
				Job job = new Job();
				job.setName(Constants.BUILDCONFIGURATION);
				job.setNumber(Integer.parseUnsignedInt(Constants.BUILDNUMBER));
				
				PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project("AL Demo", "1.0"))
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
						break;
					case "veryunstable":
						nvProfileOptions.put("packetLoss", "5");
						nvProfileOptions.put("packetCorruption", "5");
						nvProfileOptions.put("packetDuplication", "5");
						nvProfileOptions.put("packetReordering", "20");
						driver.executeScript("mobile:vnetwork:start", nvProfileOptions);
						break;				
				}
			} catch (Exception e) {
				System.out.println("Failed to get driver for " + targetEnvironment + " stacktrace: " + getStackTraceAsString(e));
			}				
		}
		
		private String[] getTags(String windTunnel) {
			List<String> tags = getAdditionalTags(new ArrayList<String>()); 
			tags.add("AL Web");
			if(windTunnel.toString() != null && !windTunnel.isEmpty() && useWindTunnel && isMobile)
			{				
				tags.add(windTunnel);
			}
			
			String[] tagsArray = new String[tags.size()];

			for (int i = 0; i < tags.size(); i++) {
			    tagsArray[i] = tags.get(i);
			}
			
			return tagsArray;
		}
		
		//Override this in derived classes to add additional report tags
		protected List<String> getAdditionalTags(List<String> tagListToAddTo)
		{
			return tagListToAddTo;
		}
			
		@Step("Tear Down and Download Report")
		protected void testTearDown(String context) throws Exception {
			if (driver != null) {				
				try {
					Map<String, Object> params2 = new HashMap<>();
					driver.executeScript("mobile:vnetwork:stop", params2);
				} catch (Exception e) {}
				
				driver.close();
				driverWasClosed = true;
				Thread.sleep(3000);
				if(testStarted)
				{	
					//downloadReport("pdf");
					downloadWTReport();
					downloadReportiumReportLink();
				}
				else
				{
					System.out.println("Test report action never started");
				}
				driver.quit();
			}
		}
		
		@Attachment
		protected byte[] saveImage(byte[] imageToSave) {
	        return imageToSave;
	    }

		
		@AfterMethod
		protected void closeWebDriver () throws SessionNotFoundException, IOException {
			// make sure web driver is closed
			try{
					if ( ((RemoteWebDriver) driver).getSessionId() != null && !driverWasClosed) 
					{
						System.out.println("Got To Driver Close - didn't close previously");
						driver.close();
						driverWasClosed = true;
					}
					driver.quit();
				}	
			catch (SessionNotFoundException e) {}
		}
		
		@Attachment
		protected byte[] downloadReport(String type) throws IOException
		{	
			try
			{
				String command = "mobile:report:download";
				Map<String, String> params = new HashMap<>();
				params.put("type", type);
				String report = (String)driver.executeScript(command, params);
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
				String reportUrl = reportiumClient.getReportUrl();		
				System.out.println("Report URL - " + reportUrl);
				String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
                returnString = returnString + reportUrl + "\"></head><body/></html>";
                return returnString.getBytes();
			}
			catch(Exception ex)
			{
				System.out.println("Error trying to download reportium link");
				System.out.println(getStackTraceAsString(ex));
				String empty = "";
				return empty.getBytes();
			}			
		}
		
		@Attachment
        protected byte[] downloadWTReport() {
               try
               {
				 	String reportUrl = (String)driver.getCapabilities().getCapability("windTunnelReportUrl");
				 	System.out.println("WindTunnel Report Link: " + reportUrl);
	                String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
	                returnString = returnString + reportUrl + "\"></head><body/></html>";
	
	                return returnString.getBytes();
               }
               catch(Exception ex)
   			{
   				System.out.println("Error trying to download WindTunnel report link. " + ex.getMessage());	
   				String empty = "";
				return empty.getBytes();
   			}
       }
		
		protected static void switchToContext(RemoteWebDriver driver, String context) {
			RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
			Map<String,String> params = new HashMap<String,String>();
			params.put("name", context);
			executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
		}	
		
		protected Boolean textCheckpoint(String textToFind, Integer timeout) {
			return textCheckpoint(textToFind, timeout, false, false);
		}
		
		protected Boolean textCheckpoint(String textToFind, Integer timeout, Boolean invertText, Boolean shouldScroll) {
			perfectoCommand.clear();
			perfectoCommand.put("content", textToFind);
			
			if(shouldScroll)
			{
				perfectoCommand.put("scrolling", "scroll");
				if(isMobile)
				{
					perfectoCommand.put("next", "SWIPE_UP");				
				}
			}
			else
			{
				perfectoCommand.put("timeout", timeout);
			}
			
			Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
			Boolean resultBool = Boolean.valueOf(result.toString());
			perfectoCommand.clear();
			return resultBool;
		}
		
		String deviceModel = "";
		protected String getDeviceModel()
		{
			if(deviceModel.isEmpty())
			{
				Map params = new HashMap<>();         
				params.put("property", "model");
				deviceModel = (String) driver.executeScript("mobile:handset:info", params);				
			}
			return deviceModel + " Web Browser";
		}
		
		// Wind Tunnel: Gets the user experience (UX) timer
		protected long timerGet(String timerType) {
			 String command = "mobile:timer:info";
			 Map<String,String> params = new HashMap<String,String>();
			 params.put("type", timerType);
			 long result = (long)driver.executeScript(command, params);
			 	return result;
		}
		 
	    protected String getStackTraceAsString(Exception ex)
		{
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			return stringWriter.toString();
		}
	    
	    @Attachment
		protected byte[] takeSafeScreenshot()
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
				return null;
			}
		}
		
		protected void buttonClick(String textToClick, Boolean scroll, Boolean scrollUp, Integer maxScroll ) 
		{			
			perfectoCommand.clear();
			perfectoCommand.put("label", textToClick);
			if(scroll)
			{
				perfectoCommand.put("scrolling", "scroll");
				perfectoCommand.put("maxscroll", maxScroll);
				if(scrollUp)
				{
					if(isMobile)
					{
						perfectoCommand.put("next", "SWIPE_UP");		
					}					
				}
			}
			driver.executeScript("mobile:button-text:click", perfectoCommand);
			
			perfectoCommand.clear();
		}
		
		protected void takeWindTunnelTimer(String timerText, long timeInMilliseconds) {
			if(useWindTunnel & isMobile)
			{
				try{
					long uxTimer1 = timerGet("ux");
					System.out.println("'Measured UX time is: " + uxTimer1);	
					
					// Wind Tunnel: Add timer to Wind Tunnel Report
					WindTunnelUtils.reportTimer(driver, uxTimer1, timeInMilliseconds, timerText, "uxTimer1");
					
					if(uxTimer1 > timeInMilliseconds)
					{
						windTimerFailed = true;
					}
				}
				catch(Exception ex)
				{
					System.out.println("Error taking WT Timer: " + getStackTraceAsString(ex));
				}
			}
		}
		
		protected void takeWindTunnelPointOfInterest(String interestText, String wtStatus) {
			if(useWindTunnel & isMobile)
			{
				try{
					WindTunnelUtils.pointOfInterest(driver, interestText, wtStatus);

				}
				catch(Exception ex)
				{
					System.out.println("Error taking WT Point of Interest: " + getStackTraceAsString(ex));
				}
			}
		}
		
		protected void rotateDevice(Constants.Rotation rotationType)
		{
			perfectoCommand.clear();
			String state = "portrait";
			if(rotationType == Constants.Rotation.LANDSCAPE)
			{
				state = "landscape";
			}
			perfectoCommand.put("state", state );
			
			driver.executeScript("mobile:handset:rotate", perfectoCommand);
			
			perfectoCommand.clear();
		}

}

