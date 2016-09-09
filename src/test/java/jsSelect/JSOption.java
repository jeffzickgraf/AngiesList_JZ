package test.java.jsSelect;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JSOption {
	private String jsScript;
	private RemoteWebDriver driver;
	public JSOption(RemoteWebDriver driver, String jsScript) {
		this.jsScript = jsScript;
		this.driver = driver;
	}
	public void selectOptionByIndex(int optionIndex){
		String jsScript = this.jsScript;
		jsScript +=  "element.selectedIndex = '" + String.valueOf(optionIndex) + "';";
		if (driver instanceof JavascriptExecutor) {
		    ((JavascriptExecutor)driver).executeScript(jsScript + getRefereshString());
		}
	}
	public void selectOptionByValue(String optionValue){
		String jsScript = this.jsScript;
		jsScript += "element.value = '" + optionValue + "';";
		if (driver instanceof JavascriptExecutor) {
		    ((JavascriptExecutor)driver).executeScript(jsScript + getRefereshString());
		}
	}
	public void selectOptionByDisplayedText(String displayedText){
		String jsScript = this.jsScript;
		jsScript += "for (i=0; i < element.length; i++){"
				+ "if (element.options[i].text.localeCompare('" + displayedText + "')==0)"
				+ "{"
				+ "element.selectedIndex = i;"
				+ "}}";
		if (driver instanceof JavascriptExecutor) {
		    ((JavascriptExecutor)driver).executeScript(jsScript + getRefereshString());
		}
	}
		
	private static String getRefereshString(){
		return "if ('createEvent' in document) " 
            	   + " {var evt = document.createEvent('HTMLEvents');" 
            	   + " evt.initEvent('change', false, true);"
            	   + " element.dispatchEvent(evt);}"
            	   + " else"
            	   + " element.fireEvent('onchange');"; 
	}
}
