<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="studentsSearchResults.tag - Display search students table for a course" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor" prefix="ti" %>
<%@ attribute name="studentTable" type="teammates.ui.template.SearchStudentsTable" required="true" %>
<%@ attribute name="courseIdx" required="true" %>

<div class="panel panel-info" style="margin-bottom: 10px">
  <div class="panel-heading clearfix">
    <strong>[${studentTable.courseId}]</strong> 
    
    
    <form action="${data.instructorSearchLink}" class="pull-right" style="font-size: 13px; display: inline-block;">
      <input type="hidden" name="searchkey" value="${data.searchKey}" >
      <input type="hidden" name="user" value="${data.account.googleId}">
      <input type="hidden" name="searchstudents" value="true" />
      <label for="StudentResultsItemsPerPage">Items per page</label>
      <select name="items-per-page" id="StudentResultsItemsPerPage" style="width: 50px">
        <c:forEach items="${data.givenItemsPerPage}" var="perPage">
          <option value="${perPage}"
            <c:if test="${perPage == data.itemsPerPage}">selected="selected"</c:if>
          >${perPage}</option>
        </c:forEach>
      </select>
      <input type="hidden" name="page" value="1" />
      <button type="submit">Go</button>
    </form>
    
  </div>

  <div class="panel-body padding-0">
    <ti:studentList courseId="${studentTable.courseId}" courseIndex="${courseIdx}" hasSection="${studentTable.hasSection}"
        sections="${studentTable.sections}" fromStudentListPage="${false}" />
  </div>
</div>
