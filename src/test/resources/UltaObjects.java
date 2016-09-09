package test.resources;

public class UltaObjects {

	//Native objects
	public static final String NativeMenuMore = "//*[@label='More']";
	public static final String NativeMenuHome = "//*[@label='Home'] | //*[@text='Home']";
	public static final String NativeSubmenuShop = "//*[@label='SHOP'] | //*[@resource-id='com.ulta:id/shopTextView']";
	public static final String NativeViewAccountLink = "//*[@label='View Account Details']";
	public static final String NativeAndroidMenuMyAccount = "//*[@text='My Account']";
	public static final String NativeMenuFindStores = "//*[@label='Stores'] | //*[@resource-id='com.ulta:id/menu_string' and @text='Find Store']";
	public static final String NativeSignOutConfirmButton = "//*[@resource-id='com.ulta:id/btnAgree'] | //UIAButton[@label='Sign Out']";
	public static final String NativeAndroidMenuButton = "//*[@class='android.widget.ImageButton']";
	public static final String NativeLoginUsername = "//*[@value='Email Address or Username'] | //*[@resource-id='com.ulta:id/editUsername']";
	public static final String NativeLoginPassword = "//*[@value='Password'] | //*[@resource-id='com.ulta:id/editPasswordLogin']";
	public static final String NativeSignInButton = "//*[@label='SIGN IN'] | //*[@resource-id='com.ulta:id/btnLogin']";
	public static final String NativeCloseSignInButton = "//UIANavigationBar/UIAButton[1] | //*[@resource-id='com.ulta:id/close_button']";
	
	public static final String NativeSearchBeginButton = "//*[@label='Search'] | //*[@resource-id='com.ulta:id/action_search']";
	public static final String NativeSearchTextBox = "//*[@value='Search Ulta Beauty'] | //*[@resource-id='com.ulta:id/txtSearch'] | //*[@text='Search Ulta Beauty']";
	public static final String NativeSearchSubmitButton = "//*[@label='Search']";
	
	public static final String NativeNavBagLink = "//*[@label='Bag'] | //*[@resource-id='com.ulta:id/bagImageView']";
	public static final String NativeBagCountObjectiOS = "//*[@label='Bag']/following-sibling::UIAStaticText[1]";
	public static final String NativeBagCountObjectAndroid = "//*[@resource-id='com.ulta:id/actionbar_notifcation_textview']";
	
	
	//Web objects
	
	//signin
	public static final String WebSignInLink = "//a[@class='signin-drop-arrow']";
	public static final String WebSignInUsername = "//input[@id='login']";
	public static final String WebSignInPassword = "//input[@id='password']";
	public static final String WebSignInSubmitButton = "//input[@value='SIGN IN']";
	public static final String WebSignInName = "//span[@id='signin-name']";
	
	
	public static final String WebBagCountIndicator = "//div[@id='hdrCartCnt']";
	public static final String WebAddToBagSubmit = "//*[@id='submitButton']";
	public static final String WebRemove = "//*[@class='links']";
	
	//search
	public static final String WebSearchTextBoxMobile = "//*[@id='searchTextmobile']";
	public static final String WebSearchSubmitMobile = "//*[@id='mob-search-cont']//*[@class='search-ulta-icon']";
	public static final String WebSearchTextBox = "//input[@id='searchText']";
	public static final String WebSearchSubmit = "//form[contains(@onsubmit, 'getUrl()')]/input[@class='search-ulta-icon']";
	public static final String WebQuantitySelect = "//select[@id='dropdown-quantity-select']";
	public static final String WebProductSubmit = "//*[@id='submitButton']";
		
	
	public UltaObjects() {
		// Nothing to do here
	}

	
	
}
