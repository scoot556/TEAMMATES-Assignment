<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="instructorSearch.tag - Search courses" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/search" prefix="search" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ attribute name="courses" type="java.util.Collection" required="true" %>
<br>
<div class="panel panel-primary">
  <div class="panel-heading">
    <strong><jsp:doBody/></strong>
  </div>

  <div class="panel-body">
  	<ul>
	  	<c:forEach items="${courses}" var="course">
	  		<li>
	  			<b>
	  				<a href="<%= Const.ActionURIs.INSTRUCTOR_COURSE_DETAILS_PAGE %>?courseid=${course.id}">${course.name}</a>
	  			</b>
	 		</li>
	  	</c:forEach>
  	</ul>
  </div>
</div>