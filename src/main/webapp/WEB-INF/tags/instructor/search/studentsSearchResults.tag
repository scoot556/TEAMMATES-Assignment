<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="instructorSearch.tag - Search students" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/search" prefix="search" %>
<%@ attribute name="searchStudentsTables" type="java.util.Collection" required="true" %>

<br>
<div class="panel panel-primary">
  <div class="panel-heading">
    <strong><jsp:doBody/></strong>
  </div>

  <div class="panel-body">
  
    <div style="font-size: 13px;">
      <label for="StudentResultsItemsPerPage">Items per page</label>
      <select name="items-per-page" id="StudentResultsItemsPerPage" style="width: 50px">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
        <option value="4">4</option>
      </select>
      <button type="submit">Go</button>
    </div>
  
    <c:forEach items="${searchStudentsTables}" var="searchStudentsTable" varStatus="i">
      <search:searchStudentsTable studentTable="${searchStudentsTable}" courseIdx="${i.index}"/>
    </c:forEach>
  </div>
</div>
