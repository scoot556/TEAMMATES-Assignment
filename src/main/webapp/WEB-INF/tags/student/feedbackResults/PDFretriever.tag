<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="studentFeedbackResults.jsp - Student feedback results question with responses" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/student/feedbackResults" prefix="feedbackResults" %>
<%@ attribute name="PDFretriever" type="teammates.ui.template.StudentFeedbackResultsQuestionWithResponses" required="true" %>

<div class="panel panel-default">
  <div class="panel-heading">
    <%-- Note: When an element has class text-preserve-space, do not insert HTML spaces --%>
    <h4>Student Feedback Files</h4>
   
    <iframe src="http://docs.google.com/gview?url=Http://File:///sickcairns/src/main/webapp/PDF/TuteWeek1.pdf&embedded=true" style="width:300px; height:100px;" frameborder="0"></iframe>
   <a href="File:///sickcairns/src/main/webapp/PDF/TuteWeek1.pdf">PDF</a>
   
   
   
  </div>
</div>
<br>
