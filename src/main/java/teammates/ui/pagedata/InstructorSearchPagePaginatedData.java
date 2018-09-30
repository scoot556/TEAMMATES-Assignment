package teammates.ui.pagedata;

import java.util.ArrayList;
import java.util.List;
import teammates.common.datatransfer.StudentSearchResultBundle;
import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.ui.template.SearchStudentsTable;

public class InstructorSearchPagePaginatedData extends InstructorSearchPageData {
	public static final int[] GIVEN_ITEMS_PER_PAGE = new int[] { 5, 10, 25, 50 };

	private int itemsPerPage = 1;
	private int pageNumber = 1;
	
	private int numPages = 1;
	
	
	public InstructorSearchPagePaginatedData(AccountAttributes account, String sessionToken, int itemsPerPage, int pageNumber) {
		super(account, sessionToken);
		this.itemsPerPage = itemsPerPage;
		this.pageNumber = pageNumber;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int getPageNumber() {
		return pageNumber;
	}
	
	public int[] getGivenItemsPerPage() {
		return GIVEN_ITEMS_PER_PAGE;
	}
	
	public int getNumPages() {
		return numPages;
	}

	@Override
	public List<SearchStudentsTable> getSearchStudentsTables() {
		return searchStudentsTables;
	}
	
	@Override
	protected List<StudentAttributes> filterStudentsByCourse(
            String courseId,
            StudentSearchResultBundle studentSearchResultBundle) {
		
		List<StudentAttributes> students = super.filterStudentsByCourse(courseId, studentSearchResultBundle);
		return paginateStudents(students, this.pageNumber, this.itemsPerPage);
	}
	
	
	public List<StudentAttributes> paginateStudents(List<StudentAttributes> studentsInCourse, int pageNumber,
			int itemsPerPage) {
		
		int numPages = (int) Math.ceil((double)studentsInCourse.size() / (double) itemsPerPage);
		int startIndex = itemsPerPage * (pageNumber - 1);
		int lastIndex = startIndex + itemsPerPage - 1;
		lastIndex = lastIndex >= studentsInCourse.size() ? studentsInCourse.size() - 1 : lastIndex;
		
		List<StudentAttributes> paginatedStudents = new ArrayList<>();
		for (int i = startIndex; i <= lastIndex; i++) {
			paginatedStudents.add(studentsInCourse.get(i));
		}
		
		this.numPages = numPages;
		
		return paginatedStudents;
	}
	
	
	
	

}
