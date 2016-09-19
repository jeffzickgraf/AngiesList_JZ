package test.resources;

public class Constants {
	public static final String USERNAME = System.getProperty("PerfectoUsername");
	public static final String PASSWORD = System.getProperty("PerfectoPassword");
	public static final String BUILDCONFIGURATION = System.getProperty("BuildConfiguration");
	public static final String BUILDNUMBER = System.getProperty("BuildNumber");
	public static final String HOST = "demo.perfectomobile.com";
	public static final Integer IMPLICIT_WAIT = 15;
	public static final String APP_NAME = "Angie's List";
	public static final String ALUSERNAME = System.getProperty("ALUsername");
	public static final String ALPASSWORD = System.getProperty("ALPassword");
	public static final String ALACCOUNTFULLNAME = "Jeff Zickgraf";
	public static final String ALACCOUNTFIRSTNAME = "Jeff";
	
	public static final String MEMBERSIGNIN = "Member Sign In";
	
	public static final Integer TEXTCHECKWAIT = 8;
		
	public Constants() {
		// TODO Auto-generated constructor stub
	}

	public enum OSType
	{
		ANDROID, IOS
	}
	
	public enum Rotation
	{
		PORTRAIT, LANDSCAPE
	}
	
}