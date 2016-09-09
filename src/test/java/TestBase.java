package test.java;


import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.asserts.SoftAssert;

public class TestBase {
	public DesiredCapabilities capabilities;
	Map<String, Object> perfectoCommand = new HashMap<>();
	protected SoftAssert softAssert = new SoftAssert();


}
