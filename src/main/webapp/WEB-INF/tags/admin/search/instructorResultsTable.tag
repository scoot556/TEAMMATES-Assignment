<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="adminSearch.jsp - instructor results table" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ taglib tagdir="/WEB-INF/tags/admin/search" prefix="adminSearch" %>
<%@ attribute name="instructorResultsTable" type="teammates.ui.template.AdminSearchInstructorTable" required="true" %>

<div class="panel panel-primary">
  <div class="panel-heading">
    <strong>Instructors Found </strong>
    <span class="pull-right">
    
	   	<form action="/admin/adminSearchPage"  id="AdminSearchItemsPerPageForm" 
	    	style="display: inline-block; font-size: 13px; margin-right: 15px">
	    	<input type="hidden" name="searchkey" value="${data.searchKey}" >
				<label for="InstructorResultsItemsPerPage">Items per page</label>
				<select name="instructor-items-per-page" id="InstructorResultsItemsPerPage" style="width: 50px; color: black">
					<c:forEach items="${data.givenItemsPerPage}" var="instructorsPerPage">
						<option value="${instructorsPerPage}"
							<c:if test="${instructorsPerPage == data.instructorItemsPerPage}">selected="selected"</c:if>
						>${instructorsPerPage}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="instructor-page-number" value="${data.instructorPageNumber}" />
				<input type="hidden" name="student-page-number" value="${data.studentPageNumber}" />
	  		<input type="hidden" name="student-items-per-page" value="${data.studentItemsPerPage}" />
				<button class="btn btn-xs btn-info type="submit">Go</button>
	    </form>
    
      <button class="btn btn-primary btn-xs" type="button" id="btn-disclose-all-instructors">Disclose All</button>
      <button class="btn btn-primary btn-xs" type="button" id="btn-collapse-all-instructors">Collapse All</button>
    </span>
  </div>

  <div class="table-responsive">
    <table class="table table-striped data-table" id="search_table_instructor">
      <thead>
        <tr>
          <th>Course</th>
          <th>Name</th>
          <th>Google ID</th>
          <th>Institute</th>
          <th>Options</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach items="${instructorResultsTable.instructorRows}" var="instructor">
          <adminSearch:instructorRow instructor="${instructor}"/>
        </c:forEach>
      </tbody>
    </table>
    
    <form action="/admin/adminSearchPage" style="font-size: 13px; display: inline-block;" class="pagination-links">
	  <input type="hidden" name="searchkey" value="${data.searchKey}" >
	  <input type="hidden" name="instructor-items-per-page" value="${data.instructorItemsPerPage}" />
	  <input type="hidden" name="student-page-number" value="${data.studentPageNumber}" />
	  <input type="hidden" name="student-items-per-page" value="${data.studentItemsPerPage}" />
	  <nav>
	    <ul>
	      <c:forEach begin="0" end="${data.instructorNumPages - 1}" step="1" var="i">
	        <li <c:if test="${i+1 == data.instructorPageNumber}">class="active"</c:if>>
	          <input type="submit" name="instructor-page-number" class="btn btn-small btn-link" value="${i + 1}" />
	        </li>
	      </c:forEach>
	    </ul>
	  </nav>
	</form>
    
  </div>
</div>
