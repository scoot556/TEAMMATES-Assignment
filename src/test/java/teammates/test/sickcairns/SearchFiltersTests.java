package teammates.test.sickcairns;

import java.util.List;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.logic.core.CoursesLogic;
import teammates.test.cases.action.BaseActionTest;
import teammates.ui.controller.InstructorSearchPageAction;
import teammates.ui.controller.ShowPageResult;
import teammates.ui.pagedata.InstructorSearchPagePaginatedData;

public class SearchFiltersTests extends BaseActionTest {
    
    private void createMultipleCourses(String instructorId) throws Exception {
        CoursesLogic.inst().createCourseAndInstructor(instructorId, "new-course", "New course", "UTC");
        
    }
    
    private InstructorAttributes createMockInstructor() {
        InstructorAttributes instructor1OfCourse1 = typicalBundle.instructors.get("instructor1OfCourse1");
        return typicalBundle.instructors.get("instructor1OfCourse1");
    }
    
    private void setupAndLoginAsInstructor() throws Exception {
        InstructorAttributes instructor1ofCourse1 = createMockInstructor();
        String instructorId = instructor1ofCourse1.googleId;
        createMultipleCourses(instructorId);
        gaeSimulation.loginAsInstructor(instructorId);
    }

    @Test
    @Override
    protected void testExecuteAndPostProcess() throws Exception {
        String[] submissionParams = new String[] {
                "searchCourses", "true"
        };
        
        setupAndLoginAsInstructor();
        
        InstructorSearchPageAction action = getAction(submissionParams);
        ShowPageResult pageResult = getShowPageResult(action);
        InstructorSearchPagePaginatedData pageData = (InstructorSearchPagePaginatedData) pageResult.data;
        
        List<CourseAttributes> courses = pageData.getCourses();
        assertNotNull(courses);
        assertTrue(courses.size() > 0);
        
    }
    
    @Override
    protected String getActionUri() {
        return Const.ActionURIs.INSTRUCTOR_SEARCH_PAGE;
    }

    @Override
    protected InstructorSearchPageAction getAction(String... params) {
        return (InstructorSearchPageAction) gaeSimulation.getActionObject(getActionUri(), params);
    }
    
    @Override
    protected void testAccessControl() throws Exception {
        String[] submissionParams = new String[] {};
        verifyOnlyInstructorsCanAccess(submissionParams);
    }



    
    
}
