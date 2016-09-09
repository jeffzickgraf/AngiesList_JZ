package test.resources;

public class Constants {
	public static final String USERNAME = System.getProperty("PerfectoUsername");
	public static final String PASSWORD = System.getProperty("PerfectoPassword");
	public static final String BUILDCONFIGURATION = System.getProperty("BuildConfiguration");
	public static final String BUILDNUMBER = System.getProperty("BuildNumber");
	public static final String HOST = "demo.perfectomobile.com";
	public static final Integer IMPLICIT_WAIT = 15;
	public static final String APP_NAME = "Ulta Beauty";
	public static final String ULTAUSERNAME = System.getProperty("UltaUsername");
	public static final String ULTAPASSWORD = System.getProperty("UltaPassword");
	public static final String ULTAACCOUNTFULLNAME = "Jeff Zickgraf";
	public static final String ULTAACCOUNTFIRSTNAME = "Jeff";
	
	public static final String ULTAPRODUCTFORSEARCHNATIVE = "Oil Wonders Egyptian Hibiscus Color";
	public static final String BAGVERIFYPRODUCT = "Matrix";
	
	public static final String RETURNINGGUESTS = "Returning Guests";
	
	public static final String ULTAPRODUCTSEARCHWEB = "Light Blue Eau de Toilette Rollerball";	
	public static final String ULTAPRODUCTSEARCHWEBSKU = "2253338";
	
	public static final String LOGOUTURL = "https://www.ulta.com/ulta/myaccount/login.jsp?_DARGS=/ulta/global/inc/header_signIn_content.jsp_A&_DAV=logout";
		
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