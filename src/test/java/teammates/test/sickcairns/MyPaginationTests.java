package teammates.test.sickcairns;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.util.Const;
import teammates.logic.core.CoursesLogic;
import teammates.test.cases.action.BaseActionTest;
import teammates.ui.controller.InstructorSearchPageAction;
import teammates.ui.controller.ShowPageResult;
import teammates.ui.pagedata.InstructorSearchPagePaginatedData;
import teammates.ui.template.SearchStudentsTable;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class MyPaginationTests extends BaseActionTest {
	
	

	@BeforeClass
	public void beforeClass() {
		
	}
	
	@BeforeTest
	public void beforeTest() {
	}
	
	private List<StudentAttributes> createMockStudents(int number) {
		List<StudentAttributes> mockStudents = new ArrayList<>();
		String courseId = Integer.toString(1);
		for (int i = 0; i < number; i++) {
			String studentName = String.format("student%d", i);
			String studentEmail = String.format("student%d@student.rmit.edu.au", i);
			StudentAttributes student = new StudentAttributes.Builder(courseId, studentName, studentEmail).build();
			mockStudents.add(student);
		}
		return mockStudents;
	}
	
	@Test
	public void test_pagination_20students_5itemPerPage_page1_displays_5items() throws Exception {
		int itemsPerPage = 5;
		int pageNumber = 1;
		List<StudentAttributes> students = createMockStudents(20);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(itemsPerPage, paginatedStudents.size());
	}
	
	
	@Test
	public void test_pagination_21students_10itemsPerPage_page1_returns_10items() {
		int itemsPerPage = 10;
		int pageNumber = 1;
		List<StudentAttributes> students = createMockStudents(21);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(itemsPerPage, paginatedStudents.size());
	}
	
	@Test
	public void test_pagination_23students_5itemsPerPage_page5_returns_3items() {
		int itemsPerPage = 5;
		int pageNumber = 5;
		List<StudentAttributes> students = createMockStudents(23);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(3, paginatedStudents.size());
	}
	
	@Test
	public void test_pagination_25students_5itemsPerPage_page5_returns_3items() {
		int itemsPerPage = 5;
		int pageNumber = 5;
		List<StudentAttributes> students = createMockStudents(25);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(5, paginatedStudents.size());
	}
	
	@Test
	public void test_pagination_25_students_5itemsPerPage_page6_returns_0items() {
		int itemsPerPage = 5;
		int pageNumber = 6;
		List<StudentAttributes> students = createMockStudents(25);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(0, paginatedStudents.size());
	}
	
	@Test
	public void test_pagination_25students_50itemsPerPage_page1_returns_25items() {
		int itemsPerPage = 50;
		int pageNumber = 1;
		List<StudentAttributes> students = createMockStudents(25);
		InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(null, null, itemsPerPage, pageNumber);
		List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
		assertEquals(25, paginatedStudents.size());
	}
	
	
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
		int itemsPerPage = 1;
		int currentPage = 1;
		
		String[] submissionParams = new String[] {
			"items-per-page", Integer.toString(itemsPerPage),
			"page", Integer.toString(currentPage),
		};
		
        setupAndLoginAsInstructor();
        
        InstructorSearchPageAction action = getAction(submissionParams);
        ShowPageResult pageResult = getShowPageResult(action);
        InstructorSearchPagePaginatedData pageData = (InstructorSearchPagePaginatedData) pageResult.data;
        
        List<SearchStudentsTable> table = pageData.getSearchStudentsTables();
        assertEquals(itemsPerPage, table.size());
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
