package teammates.test.cases.logic;

import org.testng.annotations.Test;

import teammates.common.datatransfer.UserType;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.logic.api.GateKeeper;
import teammates.logic.api.Logic;

/**
 * SUT: {@link GateKeeper}.
 */
public class MyGateKeeperTest extends GateKeeperTest {

    private static GateKeeper gateKeeper = new GateKeeper();

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
}
