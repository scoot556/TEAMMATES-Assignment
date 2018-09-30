package teammates.test.sickcairns;

import org.testng.annotations.Test;

import teammates.common.exception.UnauthorizedAccessException;
import teammates.test.cases.logic.GateKeeperTest;

public class MyGateKeeperTests extends GateKeeperTest {

    @Test
    public void testvalidRmitEmail() throws Exception {
        boolean isRmit = false;
        gaeSimulation.loginUser("s3516805@student.rmit.edu.au");
        String user = gateKeeper.getCurrentUser().id;
        
        if (gateKeeper.isRmit(user)) {
            isRmit = true;
        }
        
        assertTrue(isRmit);
    }
    
    @Test
    public void testvalidRmitEmail2() throws Exception {
        boolean isRmit = false;
        gaeSimulation.loginUser("s3516805@rmit.edu.au");
        String user = gateKeeper.getCurrentUser().id;
        
        if (gateKeeper.isRmit(user)) {
            isRmit = true;
        }
        
        assertTrue(isRmit);
    }

    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void testinvalidRmitEmail() throws Exception {
        boolean isRmit = false;
        gaeSimulation.loginUser("abcde@student.rmit.edu");
        String user = gateKeeper.getCurrentUser().id;

        if (gateKeeper.isRmit(user)) {
            isRmit = true;
        }
        assertFalse(isRmit);
    }
    
    @Test(expectedExceptions = UnauthorizedAccessException.class)
    public void testinvalidRmitEmail2() throws Exception {
        boolean isRmit = false;
        gaeSimulation.loginUser("sampleEmail@gmail.com");
        String user = gateKeeper.getCurrentUser().id;

        if (gateKeeper.isRmit(user)) {
            isRmit = true;
        }
        assertFalse(isRmit);
    }
    
}
