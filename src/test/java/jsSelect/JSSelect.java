package test.java.jsSelect;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JSSelect {

	private final String XPATH_SELECT="function findElementsByXpath(path){"
			+ "elements = document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
			+ "return elements;}";
	
	public JSOption findByXpath(RemoteWebDriver driver, String xpath){
		String jsScript=XPATH_SELECT + " element = findElementsByXpath('" + xpath + "');";
		return new JSOption(driver, jsScript);
	}
	public JSOption findByIndex(RemoteWebDriver driver, int selectIndex){
		String jsScript = "elements = document.getElementsByTagName('select');"
				+ "element = elements["+ String.valueOf(selectIndex) + "];";
		return new JSOption(driver, jsScript);
	}
	public JSOption findById(RemoteWebDriver driver, String selectId){
		String jsScript = "element = document.getElementById('" + selectId + "');";
		return new JSOption(driver, jsScript);
	}
	public JSOption findByName(RemoteWebDriver driver, String selectName){
		String jsScript = "element = document.getElementByName('" + selectName + "');";
		return new JSOption(driver, jsScript);
	}	
}
