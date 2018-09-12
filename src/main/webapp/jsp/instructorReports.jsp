<%@ page trimDirectiveWhitespaces="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor" prefix="ti" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/reports" prefix="reports" %>

<c:set var="jsIncludes">
  <script type="text/javascript" src="/js/instructorReports.js"></script>
</c:set>

<ti:instructorPage title="Reports" jsIncludes="${jsIncludes}">
	<div class="col-sm-4">
		<nav>
			<ul class="nav tabs">
				<li class="active"><a href="#SummaryTab" data-toggle="tab">Summary</a></li>
				<li><a href="#CoursesTab" data-toggle="tab">Courses</a></li>
				<li><a href="#StudentsTab" data-toggle="tab">Students</a></li>
			</ul>
		</nav>
	</div>
	<div class="col-sm-8">
		<div class="tab-content">
			<div class="tab-pane active" id="SummaryTab">
				<h3>Summary</h3>
				<p>
					Number of courses: ${data.numberOfCourses}<br/>
					Number of students enrolled: ${data.studentsThatAcceptedInvitation}<br/>
					Number of students not enrolled: ${data.numStudentNotAcceptedInvitation} <br/>
				</p>
				<p>
					Active sessions this week: ${data.numActiveSessions}<br/>
					Feedback rate: ${data.feedbackRate} %
				</p>
			</div>
			
			<div class="tab-pane" id="CoursesTab">
				<h3>Courses</h3>
			    <c:forEach items="${data.coursesTabTableData}" var="tableData" varStatus="i">
			      <c:set var="courseDetails" value="${tableData.courseDetails}" />
			      <c:set var="course" value="${courseDetails.course}" />
			      <div class="panel panel-primary">
			      	<div class="panel-heading">
			      		<var>${course.id}</var><br/>
			      		${course.name}
			      	</div>
			      	<div class="panel-body">
			      		<table class="course-details-table">
			      			<tr>
			      				<td>Created: </td>
			      				<td data-date-stamp="${course.createdAtDateStamp}"
							        data-toggle="tooltip"
							        data-original-title="${course.createdAtFullDateTimeString}">
							          ${course.createdAtDateString}
							    </td>
			      			</tr>
			      			<tr>
			      				<td>Sections: </td>
			      				<td>${courseDetails.stats.sectionsTotal}</td>
			      			</tr>
			      			<tr>
			      				<td>Teams </td>
			      				<td>${courseDetails.stats.teamsTotal}</td>
			      			</tr>
			      			<tr>
			      				<td>Total Students</td>
			      				<td>${courseDetails.stats.studentsTotal}</td>
			      			</tr>
			      			<tr>
			      				<td>Unregistered students</td>
			      				<td>${courseDetails.stats.unregisteredTotal}</td>
			      			</tr>
			      		</table>
			      		<br/>
			      		<br/>
			      		
			      		<b>Feedback sessions</b><br/>
			      		<table class="course-feedbacksession-table">
			      			<tr>
			      				<th>Session name</th>
			      				<th>Responses</th>
			      			</tr>
			      			<c:forEach items="${tableData.feedbackSessions}" var="feedbackSession" varStatus="j">
				      			<tr>
				      				<td>${feedbackSession.name}</td>
				      				<td>${feedbackSession.responseRate}</td>
				      			</tr>
			      			</c:forEach>
			      		</table>
			      	</div>
			      </div>
			    </c:forEach>
				
			</div>
			
			<div class="tab-pane" id="StudentsTab">
			
				<h3>Students</h3>
				<div class="panel panel-primary">
					<div class="panel-heading">
				      		<p> Students List: ${data.studentsThatAcceptedInvitation} </p>
				    </div>
				    <div class="panel-body">
				    	<table class="student-details-table">
				    		<tr>
				    			<th>Name</th>
				    			<th>Email</th>
				    			<th>Courses</th>
				    		</tr>
				    		
				    		<!--  Loop through students not working -->
				    		<c:forEach items="${data.studentAttributes}" var="entry" varStatus="i">
								<tr>
					    			<td> ${entry.name } </td>
					    			<td> ${entry.email } </td>
					    			<td> ${entry.course } </td>
				    			</tr>
							</c:forEach>   		
				    		<!--  end Loop -->	
				    	</table>
				    </div>
			    </div>
			    
			    <div class="panel panel-primary">
					<div class="panel-heading">
				      		<p> Students not Enrolled: ${data.numStudentNotAcceptedInvitation} </p>
				    </div>
				    <div class="panel-body">
				    	<table class="student-details-table">
				    		<tr>
				    			<th>Name</th>
				    			<th>Email</th>
				    			<th>Courses</th>
				    		</tr>
				    		
				    		<!--  Loop through students not working -->
				    		<c:forEach items="${data.studentNotAcceptedInvitation}" var="entry" varStatus="i">
								<tr>
					    			<td> ${entry.name } </td>
					    			<td> ${entry.email } </td>
					    			<td> ${entry.course } </td>
				    			</tr>
							</c:forEach>   		
				    		<!--  end Loop -->	
				    	</table>
				    </div>
			    </div>
			</div>
			
		</div>
	</div>
</ti:instructorPage>
