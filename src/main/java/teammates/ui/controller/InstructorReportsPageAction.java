package teammates.ui.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import teammates.common.datatransfer.CourseDetailsBundle;
import teammates.common.datatransfer.CourseSummaryBundle;
import teammates.common.datatransfer.FeedbackSessionDetailsBundle;
import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Const.StatusMessages;
import teammates.common.util.StatusMessage;
import teammates.common.util.StatusMessageColor;
import teammates.ui.datatransfer.InstructorStudentListPageCourseData;
import teammates.ui.pagedata.InstructorHomePageData;
import teammates.ui.pagedata.InstructorReportsAjaxPageData;
import teammates.ui.pagedata.InstructorReportsPageData;
import teammates.ui.pagedata.PageData;

public class InstructorReportsPageAction extends Action {
	
    @Override
    public ActionResult execute() throws EntityDoesNotExistException {
    	gateKeeper.verifyInstructorPrivileges(account);
    	
    	//Get instructor
        Boolean displayArchive = getRequestParamAsBoolean(Const.ParamsNames.DISPLAY_ARCHIVE);
    	Map<String, InstructorAttributes> instructors = new HashMap<>();
    	
    	
    	// get number of courses
    	List<CourseAttributes> courses = logic.getCoursesForInstructor(account.googleId);
    	int numberOfCourses = courses.size();    	
    	
    	// get number of student enrolled
    	List<StudentAttributes> studentsThatAcceptedInvitation = new ArrayList<>();
    	for (CourseAttributes course : courses) {
    		List<StudentAttributes> studentsForCourse = logic.getStudentsForCourse(course.getId());
    		studentsThatAcceptedInvitation.addAll(studentsForCourse);
    	}
    	int numStudentsThatAcceptedInvitation = studentsThatAcceptedInvitation.size();
    	
    	//get list of student names
    	//List<StudentAttributes> students = studentsThatAcceptedInvitation;
    	
    	// get number of students not enrolled
    	List<StudentAttributes> studentsNotAcceptedInvitation = new ArrayList<>();
    	for (CourseAttributes course : courses) {
    		List<StudentAttributes> studentsNotRegistered = logic.getUnregisteredStudentsForCourse(course.getId());
    		studentsNotAcceptedInvitation.addAll(studentsNotRegistered);
    	}
    	int numStudentNotAcceptedInvitation = studentsNotAcceptedInvitation.size();
    	
    	
    	// active sessions
    	List<FeedbackSessionAttributes> activeSessions = logic.getFeedbackSessionsListForInstructor(account.googleId, true);
    	int numActiveSessions = activeSessions.size();
    	
    	// calculate feedback rate
    	
    	//Get Instructor attributes
        List<InstructorAttributes> instructorList = logic.getInstructorsForGoogleId(account.googleId);

        for (InstructorAttributes instructor : instructorList) {
            instructors.put(instructor.courseId, instructor);
        }
    	
    	//Get courses to display
    	List<InstructorStudentListPageCourseData> coursesToDisplay = new ArrayList<>();
        for (CourseAttributes course : courses) {
            InstructorAttributes instructor = instructors.get(course.getId());
            boolean isInstructorAllowedToModify = instructor.isAllowedForPrivilege(
                                            Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_STUDENT);

            boolean isCourseDisplayed = displayArchive || !instructor.isArchived;
            if (isCourseDisplayed) {
                coursesToDisplay.add(new InstructorStudentListPageCourseData(course, instructor.isArchived,
                                                                             isInstructorAllowedToModify));
            }
        }
    	
    	
    	// get all courses
    	Map<String, CourseSummaryBundle> coursesBundle = logic.getCourseSummariesWithoutStatsForInstructor(account.googleId, true);
    	List<CourseSummaryBundle> coursesSummaries = new ArrayList<>(coursesBundle.values());
    	
    	
    	// build course details
    	List<CourseDetailsBundle> courseDetailsList = coursesSummaries.stream().map(b -> {
    		CourseDetailsBundle details = null;
    		String courseId = b.course.getId();
    		List<FeedbackSessionAttributes> feedbackSessions = logic.getFeedbackSessionsForCourse(courseId);
    		try {
    			// may throw and return null
    			details = logic.getCourseDetails(courseId); 
    			
    			// convert list of FeedbackSessionAttributes to FeedbackSessionDetailsBundle
    			details.feedbackSessions = feedbackSessions.stream().map(feedbackSession -> {
    				FeedbackSessionDetailsBundle feedbackSessionBundle = null;
    				try {
    					feedbackSessionBundle = logic.getFeedbackSessionDetails(feedbackSession.getFeedbackSessionName(), courseId);
    				} catch (EntityDoesNotExistException e) {
    					feedbackSessionBundle = new FeedbackSessionDetailsBundle(feedbackSession);
    				}
    				return feedbackSessionBundle;
    			}).collect(Collectors.toList());
    			
    			return details;
    		} 
    		catch(EntityDoesNotExistException e){
    			return null;
    		}
    	}).collect(Collectors.toList());
    	
    	
    	double totalStudentsReceivedFeedback = 0.0;
    	double totalStudentsSubmittedFeedback = 0.0;
    	for (CourseDetailsBundle bundle : courseDetailsList) {
    		totalStudentsReceivedFeedback += bundle.feedbackSessions.stream().map(fs -> fs.stats.expectedTotal).reduce(0, (prev, accum) -> prev + accum);
    		totalStudentsSubmittedFeedback += bundle.feedbackSessions.stream().map(fs -> fs.stats.submittedTotal).reduce(0, (prev, accum) -> prev + accum);
    	}
    	double feedbackRate = totalStudentsReceivedFeedback == 0 ? 0 : totalStudentsSubmittedFeedback / totalStudentsReceivedFeedback;
    	feedbackRate = Math.round(feedbackRate * 10000.0) / 10000.0; // 4 decimal places only
    	feedbackRate *= 100;
    	
    	// build viewmodel
    	InstructorReportsPageData data = new InstructorReportsPageData(account, sessionToken);
    	data.init(
			courseDetailsList,
			numberOfCourses, 
			numStudentsThatAcceptedInvitation, 
			numStudentNotAcceptedInvitation, 
			numActiveSessions,
			feedbackRate,
			studentsThatAcceptedInvitation,
			studentsNotAcceptedInvitation
    	);
    	
        return createShowPageResult(
        	Const.ViewURIs.INSTRUCTOR_REPORTS, data
        );
    }

    
}
