package teammates.test.sickcairns;

import org.testng.annotations.Test;

import teammates.common.exception.UnauthorizedAccessException;
import teammates.test.cases.logic.GateKeeperTest;

public class MyGateKeeperTests extends GateKeeperTest {

    @Test
    public void test_validRmitEmail() throws Exception{
        boolean isRMIT = false;
        gaeSimulation.loginUser("s3516805@student.rmit.edu.au");
        String user = gateKeeper.getCurrentUser().id;
        
        if(gateKeeper.isRmit(user)) {
            isRMIT = true;
        }
        
        assertTrue(isRMIT);
    }
    
    @Test
    public void test_validRmitEmail_2() throws Exception{
        boolean isRMIT = false;
        gaeSimulation.loginUser("s3516805@rmit.edu.au");
        String user = gateKeeper.getCurrentUser().id;
        
        if(gateKeeper.isRmit(user)) {
            isRMIT = true;
        }
        
        assertTrue(isRMIT);
    }

    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidRmitEmail() throws Exception{
        boolean isRMIT = false;
        gaeSimulation.loginUser("abcde@student.rmit.edu");
        String user = gateKeeper.getCurrentUser().id;

        if(gateKeeper.isRmit(user)) {
            isRMIT = true;
        }
        assertFalse(isRMIT);
    }
    
    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void test_invalidRmitEmail_2() throws Exception{
        boolean isRMIT = false;
        gaeSimulation.loginUser("sampleEmail@gmail.com");
        String user = gateKeeper.getCurrentUser().id;

        if(gateKeeper.isRmit(user)) {
            isRMIT = true;
        }
        assertFalse(isRMIT);
    }
    
}
