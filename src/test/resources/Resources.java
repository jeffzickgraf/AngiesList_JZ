package test.resources;

import java.net.MalformedURLException;
import java.net.URL;

public class Resources {

	URL cloudUrl;
	
	public Resources() throws MalformedURLException {
		cloudUrl = new URL("https://" + Constants.HOST + "/nexperience/perfectomobile/wd/hub");
	}
	
	public URL getCloudUrl() {
		return cloudUrl;
	}

}
