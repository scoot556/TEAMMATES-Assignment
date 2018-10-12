<%@ page trimDirectiveWhitespaces="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="teammates.common.util.FrontEndLibrary" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor" prefix="ti" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/courseStudentDetails" prefix="csd" %>
<c:set var="jsIncludes">
  <script type="text/javascript" src="/js/instructorCourseStudentDetails.js"></script>
</c:set>
<ti:instructorPage title="Student Details" jsIncludes="${jsIncludes}">
  <t:statusMessage statusMessagesToUser="${data.statusMessagesToUser}" />
  <c:if test="${not empty data.studentProfile}">
    <csd:studentProfile student="${data.studentProfile}"/>
  </c:if>
  <div class="col-xs-12 col-md-6">
    <csd:studentInformationTable studentInfoTable="${data.studentInfoTable}" />
  </div>
  <div class="col-xs-12 col-md-6">
    <c:if test="${not empty data.studentProfile}">
      <ti:moreInfo student="${data.studentProfile}" />
    </c:if>
  </div>
</ti:instructorPage>
<script>
	function likeEnabled(){
		document.getElementById("like-button").className = "fa fa-thumbs-up disabled";
	}
</script>
