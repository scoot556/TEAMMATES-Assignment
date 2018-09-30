package teammates.test.sickcairns;

import java.util.ArrayList;
import java.util.List;

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


/**
 * Class that attempts to test the pagination logic.
 */
public class MyPaginationTests extends BaseActionTest {

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
    public void testpagination20students5itemPerPagepage1displays5items() throws Exception {
        int itemsPerPage = 5;
        int pageNumber = 1;
        List<StudentAttributes> students = createMockStudents(20);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(itemsPerPage, paginatedStudents.size());
    }
    
    
    @Test
    public void testpagination21students10itemsPerPagepage1returns10items() {
        int itemsPerPage = 10;
        int pageNumber = 1;
        List<StudentAttributes> students = createMockStudents(21);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(itemsPerPage, paginatedStudents.size());
    }
    
    @Test
    public void testpagination23students5itemsPerPagepage5returns3items() {
        int itemsPerPage = 5;
        int pageNumber = 5;
        List<StudentAttributes> students = createMockStudents(23);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(3, paginatedStudents.size());
    }
    
    @Test
    public void testpagination25students5itemsPerPagepage5returns3items() {
        int itemsPerPage = 5;
        int pageNumber = 5;
        List<StudentAttributes> students = createMockStudents(25);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(5, paginatedStudents.size());
    }
    
    @Test
    public void testpagination25students5itemsPerPagepage6returns0items() {
        int itemsPerPage = 5;
        int pageNumber = 6;
        List<StudentAttributes> students = createMockStudents(25);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(0, paginatedStudents.size());
    }
    
    @Test
    public void testpagination25students50itemsPerPagepage1returns25items() {
        int itemsPerPage = 50;
        int pageNumber = 1;
        List<StudentAttributes> students = createMockStudents(25);
        InstructorSearchPagePaginatedData pageData = new InstructorSearchPagePaginatedData(
                null, null, itemsPerPage, pageNumber);
        List<StudentAttributes> paginatedStudents = pageData.paginateStudents(students, pageNumber, itemsPerPage);
        assertEquals(25, paginatedStudents.size());
    }
    
    
    private void createMultipleCourses(String instructorId) throws Exception {
        CoursesLogic.inst().createCourseAndInstructor(instructorId, "new-course", "New course", "UTC");
    }
    
    private InstructorAttributes createMockInstructor() {
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
