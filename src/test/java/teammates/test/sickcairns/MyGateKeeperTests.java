package teammates.test.sickcairns;

import org.testng.annotations.Test;

import teammates.common.exception.UnauthorizedAccessException;
import teammates.test.cases.logic.GateKeeperTest;

public class MyGateKeeperTests extends GateKeeperTest {

	@Test
    public void test_validRMITEmail() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3516805@student.rmit.edu.au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertTrue(isRMIT);
    }
    
    @Test
    public void test_validRMITEmail_2() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3516805@rmit.edu.au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertTrue(isRMIT);
    }

    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidRMITEmail() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("abcde@student.rmit.edu");
    	String user = gateKeeper.getCurrentUser().id;

    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	assertFalse(isRMIT);
    }
    
    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidRMITEmail_2() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("sampleEmail@gmail.com");
    	String user = gateKeeper.getCurrentUser().id;

    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	assertFalse(isRMIT);
    }
    
	@Test
    public void test_caseSensitiveValidRMITEmail() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3516805@sTuDenT.RmiT.eDu.Au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertTrue(isRMIT);
    }
	
	@Test
    public void test_caseSensitiveValidRMITEmail_2() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3537805@STuDEnT.RMiT.EDu.Au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertTrue(isRMIT);
    }
	
	@Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidCaseSensitiveValidRMITEmail() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3516805@sTuDen.RmiT.eDu.Au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertFalse(isRMIT);
    }
	
	@Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidCaseSensitiveValidRMITEmail_2() throws Exception{
    	boolean isRMIT = false;
    	gaeSimulation.loginUser("s3537805@sTUDen.RmiT.eDu.Au");
    	String user = gateKeeper.getCurrentUser().id;
    	
    	if(gateKeeper.isRMIT(user)) {
    		isRMIT = true;
    	}
    	
    	assertFalse(isRMIT);
    }
	
}
