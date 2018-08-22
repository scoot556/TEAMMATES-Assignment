package teammates.ui.pagedata;

import java.util.ArrayList;
import java.util.List;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.ui.template.AdminSearchInstructorRow;
import teammates.ui.template.AdminSearchInstructorTable;
import teammates.ui.template.AdminSearchStudentRow;
import teammates.ui.template.AdminSearchStudentTable;

public class AdminSearchPagePaginatedData extends AdminSearchPageData {
	
	public static final int[] GIVEN_ITEMS_PER_PAGE = new int[] { 1,3,5 };
	
	private int studentItemsPerPage = 1;
	private int studentPageNumber = 1;
	private int studentNumPages = 1;
	
	private int instructorItemsPerPage = 1;
	private int instructorPageNumber = 1;
	private int instructorNumPages = 1;
	

	public AdminSearchPagePaginatedData(
			AccountAttributes account, 
			String sessionToken, 
			int studentItemsPerPage, 
			int studentPageNumber,
			int instructorItemsPerPage,
			int instructorPageNumber) {
		
		super(account, sessionToken);
		
		this.studentItemsPerPage = studentItemsPerPage;
		this.studentPageNumber = studentPageNumber;
		this.instructorItemsPerPage = instructorItemsPerPage;
		this.instructorPageNumber = instructorPageNumber;
	}
	

	public int getStudentItemsPerPage() {
		return studentItemsPerPage;
	}


	public int getStudentPageNumber() {
		return studentPageNumber;
	}


	public int getStudentNumPages() {
		return studentNumPages;
	}


	public int getInstructorItemsPerPage() {
		return instructorItemsPerPage;
	}


	public int getInstructorPageNumber() {
		return instructorPageNumber;
	}


	public int getInstructorNumPages() {
		return instructorNumPages;
	}


	public int[] getGivenItemsPerPage() {
		return GIVEN_ITEMS_PER_PAGE;
	}
	
	
	@Override
	protected AdminSearchInstructorTable createInstructorTable() {
		List<AdminSearchInstructorRow> rows = new ArrayList<>();
		List<InstructorAttributes> instructors = instructorResultBundle.instructorList;

		int size = instructors.size();
        int numPages = (int) Math.ceil((double) size / (double) instructorItemsPerPage);
		int startIndex = instructorItemsPerPage * (instructorPageNumber - 1);
		int lastIndex = startIndex + instructorItemsPerPage - 1;
		lastIndex = lastIndex >= size ? size - 1 : lastIndex;
		
		for (int i = startIndex; i <= lastIndex; i++) {
			InstructorAttributes instructor = instructors.get(i);
			AdminSearchInstructorRow row = createInstructorRow(instructor);
            rows.add(row);
        }
		
		this.instructorNumPages = numPages;
        return new AdminSearchInstructorTable(rows);
		
	}

	@Override
	protected AdminSearchStudentTable createStudentTable() {
        List<AdminSearchStudentRow> rows = new ArrayList<>();
        List<StudentAttributes> studentList = studentResultBundle.studentList;
        
        int size = studentList.size();
        int numPages = (int) Math.ceil((double) size / (double) studentItemsPerPage);
		int startIndex = studentItemsPerPage * (studentPageNumber - 1);
		int lastIndex = startIndex + studentItemsPerPage - 1;
		lastIndex = lastIndex >= size ? size - 1 : lastIndex;
		
		for (int i = startIndex; i <= lastIndex; i++) {
			StudentAttributes student = studentList.get(i);
			AdminSearchStudentRow row = createStudentRow(student);
			rows.add(row);
		}
		
		this.studentNumPages = numPages;
        
        return new AdminSearchStudentTable(rows);
    }
	

	
	
}
