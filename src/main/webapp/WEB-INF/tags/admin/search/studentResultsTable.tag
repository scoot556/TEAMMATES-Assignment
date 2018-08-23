<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="adminSearch.jsp - student results table" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ taglib tagdir="/WEB-INF/tags/admin/search" prefix="adminSearch" %>
<%@ attribute name="studentResultsTable" type="teammates.ui.template.AdminSearchStudentTable" required="true" %>

<div class="panel panel-primary">
  <div class="panel-heading clearfix">
    <strong>Students Found </strong>
    
    
    <span class="pull-right">
    
	    <form action="/admin/adminSearchPage"  id="AdminSearchItemsPerPageForm" 
	    	style="display: inline-block; font-size: 13px; margin-right: 15px">
	    	<input type="hidden" name="searchkey" value="${data.searchKey}" >
				<label for="StudentResultsItemsPerPage">Items per page</label>
				<select name="student-items-per-page" id="StudentResultsItemsPerPage" style="width: 50px; color: black">
					<c:forEach items="${data.givenItemsPerPage}" var="studentsPerPage">
						<option value="${studentsPerPage}"
							<c:if test="${studentsPerPage == data.studentItemsPerPage}">selected="selected"</c:if>
						>${studentsPerPage}</option>
					</c:forEach>
				</select>
			
				<input type="hidden" name="student-page-number" value="${data.studentPageNumber}" />
				<input type="hidden" name="instructor-page-number" value="${data.instructorPageNumber}" />
				<input type="hidden" name="instructor-items-per-page" value="${data.instructorItemsPerPage}" />
				<button class="btn btn-xs btn-info type="submit">Go</button>
	    </form>
	    
      <button class="btn btn-primary btn-xs" type="button" id="btn-disclose-all-students">Disclose All</button>
      <button class="btn btn-primary btn-xs" type="button" id="btn-collapse-all-students">Collapse All</button>
    </span>
  </div>

  <div class="table-responsive">
    <table class="table table-striped data-table" id="search_table">
      <thead>
        <tr>
          <th>Institute </th>
          <th>Course[Section](Team)</th>
          <th>Name</th>
          <th>Google ID[Details]</th>
          <th>Comments</th>
          <th>Options</th>

        </tr>
      </thead>

      <tbody>
        <c:forEach items="${studentResultsTable.studentRows}" var="student">
          <adminSearch:studentRow student="${student}"/>
        </c:forEach>
      </tbody>
    </table>
    
    <form action="/admin/adminSearchPage" style="font-size: 13px; display: inline-block;" class="pagination-links">
	  <input type="hidden" name="searchkey" value="${data.searchKey}" >
	  <input type="hidden" name="student-items-per-page" value="${data.studentItemsPerPage}" />
	  <input type="hidden" name="instructor-page-number" value="${data.instructorPageNumber}" />
	  <input type="hidden" name="instructor-items-per-page" value="${data.instructorItemsPerPage}" />
	  <nav>
	    <ul>
	      <c:forEach begin="0" end="${data.studentNumPages - 1}" step="1" var="i">
	        <li <c:if test="${i+1 == data.studentPageNumber}">class="active"</c:if>>
	          <input type="submit" name="student-page-number" class="btn btn-small btn-link" value="${i + 1}" />
	        </li>
	      </c:forEach>
	    </ul>
	  </nav>
	</form>
    
    
    
    
  </div>
</div>
