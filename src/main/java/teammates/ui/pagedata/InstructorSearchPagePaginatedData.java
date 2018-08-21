package teammates.ui.pagedata;

import java.util.ArrayList;
import java.util.List;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.ui.template.PaginationLink;
import teammates.ui.template.SearchStudentsTable;

public class InstructorSearchPagePaginatedData extends InstructorSearchPageData {

	private int itemsPerPage = 0;
	private int pageNumber = 1;
	
	private List<PaginationLink> paginationLinks = new ArrayList<>();
	
	
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
	
	public List<PaginationLink> getPaginationLinks() {
		return paginationLinks;
	}

	@Override
	public List<SearchStudentsTable> getSearchStudentsTables() {
		return searchStudentsTables;
	}
	
	

}
