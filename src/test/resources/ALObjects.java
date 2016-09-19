package test.resources;

public class ALObjects {

	//Native objects
	
	//Sign In
	public static final String NativeSignInLink = "//*[@resource-id='com.angieslist.android.activity:id/button_sign_in'] | //*[@label='Sign In'] ";
	public static final String NativeUsername = "//*[@resource-id='com.angieslist.android.activity:id/email_input'] | //*[@label='Email']";
	public static final String NativePassword = "//*[@resource-id='com.angieslist.android.activity:id/password_input'] | //*[@label='Password']";
	public static final String NativeSignInButton = "//*[@resource-id='com.angieslist.android.activity:id/sign_in_button']| //*[@label='Sign In Submit']";
	
	//
	public static final String NativeSearchtheList = "//*[@text='Search the List'] | //*[@label='Search the List']";
	public static final String NativePlumbing = "//*[@text='Plumbing']| //*[@label='Plumbing']";
	public static final String NativeChangeLocationLink = "//*[@resource-id='com.angieslist.android.activity:id/action_location'] | //*[@label='Address Bar Button'] ";
	public static final String NativeUseCurrentLocation = "//*[@resource-id='com.angieslist.android.activity:id/use_current_location_btn'] | //*[@label='Use Current Location']";
	
	
	
	//Web objects
	
	//signin
	public static final String WebSignOutButton = "//button[contains(text(), 'SIGN OUT')]";
	public static final String WebSignInUsername = "//input[@id='email']";
	public static final String WebSignInPassword = "//input[@id='password']";
	public static final String WebSignInSubmitButton = "//button[@type='submit']";
	public static final String WebSignInLabel = "//*[text()[contains(.,'Sign In')]]";	
	//search
	public static final String WebSearchInput = "//input[@class='main-nav_autocomplete-input']";
	
	//hover
	public static final String WebHoverProfile = "//li[@class='main-menu_tab main-menu-user_dropdown-tab hasMsgs']";
	
	public ALObjects() {
		// Nothing to do here
	}

	
	
}
