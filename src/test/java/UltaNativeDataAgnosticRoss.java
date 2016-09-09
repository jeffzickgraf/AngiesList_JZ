package test.java;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UltaNativeDataAgnosticRoss extends UltaNativeDataAgnostic {

	@Parameters({"targetEnvironment", "nvProfile", "context", "windTunnel"})
	@Test
	public void agnosticTestRoss(String targetEnvironment, String nvProfile, String context, String windTunnel) throws Exception {			
		beforeTest(targetEnvironment, nvProfile, context, windTunnel);	
		agnosticTest(context);
	}
	
	protected String getWindTunnelPersona()
	{
		return "Ross";
	}
	
	
}
