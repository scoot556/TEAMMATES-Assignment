<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="instructorCourseStudentDetails / instructorStudentRecords - Student Information" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="studentInfoTable" type="teammates.ui.template.StudentInfoTable" required="true" %>
<%@ tag import="teammates.common.util.Const" %>
<div class="well well-plain">
  <div class="form form-horizontal" id="studentInfomationTable">
    <div class="form-group">
      <label class="control-label">Student Name:</label>
      <div id="<%=Const.ParamsNames.STUDENT_NAME%>">
        <p class="form-control-static">${fn:escapeXml(studentInfoTable.name)}</p>
      </div>
    </div>
    <c:if test="${studentInfoTable.hasSection}">
      <div class="form-group">
        <label class="control-label">Section Name:</label>
        <div class="" id="<%= Const.ParamsNames.SECTION_NAME %>">
          <p class="form-control-static">${fn:escapeXml(studentInfoTable.section)}</p>
        </div>
      </div>
    </c:if>
    <div class="form-group">
      <label class="control-label">Team Name:</label>
      <div class="" id="<%= Const.ParamsNames.TEAM_NAME %>">
        <p class="form-control-static">${fn:escapeXml(studentInfoTable.team)}</p>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label">Official Email Address:</label>
      <div class="" id="<%= Const.ParamsNames.STUDENT_EMAIL %>">
        <p class="form-control-static">${studentInfoTable.email}</p>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label">Comments: </label>
      <div class="" id="<%= Const.ParamsNames.COMMENTS %>">
        <p class="form-control-static">${fn:escapeXml(studentInfoTable.comments)}</p>
      </div>
      <button onclick="likeEnabled()"><i id="like-button" class="fa fa-thumbs-up"></i> like</button>
    </div>
  </div>
</div>
