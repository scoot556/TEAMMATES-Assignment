package teammates.test.sickcairns;

import java.util.List;

import org.testng.annotations.Test;

import teammates.common.datatransfer.CourseDetailsBundle;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.logic.core.CoursesLogic;
import teammates.test.cases.action.BaseActionTest;
import teammates.ui.controller.Action;
import teammates.ui.controller.InstructorReportsPageAction;
import teammates.ui.controller.InstructorSearchPageAction;
import teammates.ui.controller.ShowPageResult;
import teammates.ui.pagedata.InstructorReportsPageData;
import teammates.ui.pagedata.InstructorSearchPageData;

public class MyInstructorReportsPageTests extends BaseActionTest {
	
	private InstructorAttributes createMockInstructor() {
		InstructorAttributes instructor1OfCourse1 = typicalBundle.instructors.get("instructor1OfCourse1");
        return typicalBundle.instructors.get("instructor1OfCourse1");
	}

	private void setupAndLoginAsInstructor() throws Exception {
		InstructorAttributes instructor1ofCourse1 = createMockInstructor();
        String instructorId = instructor1ofCourse1.googleId;
        gaeSimulation.loginAsInstructor(instructorId);
	}
	
	private void createMockFeedbacks() {
		
	}

	@Override
	protected String getActionUri() {
		return Const.ActionURIs.INSTRUCTOR_REPORTS_PAGE;
	}

	@Override
	protected InstructorReportsPageAction getAction(String... params) {
		return (InstructorReportsPageAction) gaeSimulation.getActionObject(getActionUri(), params);
	}
	
	@Override
	protected void testAccessControl() throws Exception {
		String[] submissionParams = new String[] {};
        verifyOnlyInstructorsCanAccess(submissionParams);
	}

	@Test
	@Override
	protected void testExecuteAndPostProcess() throws Exception {
		String[] submissionParams = new String[] {};
		setupAndLoginAsInstructor();
		
		InstructorReportsPageAction action = getAction(submissionParams);
		ShowPageResult pageResult = getShowPageResult(action);
        InstructorReportsPageData pageData = (InstructorReportsPageData) pageResult.data;
        
        verifyCourseTabTableData(pageData);
        verifySummaryTabData(pageData);
	}
	
	private void verifyCourseTabTableData(InstructorReportsPageData pageData) {
		List<CourseDetailsBundle> courseDetailsList = pageData.getCourseDetailsList();
		List<InstructorReportsPageData.Table> coursesTabTableData = pageData.getCoursesTabTableData();
		
		assertEquals(courseDetailsList.size(), coursesTabTableData.size());
	}
	
	private void verifySummaryTabData(InstructorReportsPageData pageData) {
		fail("Not yet implemented");
	}

	
  
	
	
}
