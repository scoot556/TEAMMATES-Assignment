package teammates.ui.pagedata;

import java.util.List;
import java.util.stream.Collectors;

import teammates.common.datatransfer.CourseDetailsBundle;
import teammates.common.datatransfer.FeedbackSessionStats;
import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.util.Const;
import teammates.ui.template.CourseTable;

public class InstructorReportsPageData extends PageData {
    
    private boolean isSortingDisabled;
    private List<CourseTable> courseTables;
    private String sortCriteria;
    private List<CourseDetailsBundle> courseDetailsList;
    private List<InstructorReportsPageData.Table> coursesTabTableData;
    
    private int numberOfCourses;
    private int studentsThatAcceptedInvitation;
    private int numStudentNotAcceptedInvitation;
    private int totalStudents;
    private int numActiveSessions;
    private double feedbackRate;
    
    //Total Students list
    private List<StudentAttributes> studentAttributes; //Accepted students
    private List<StudentAttributes> studentNotAcceptedInvitation; //Not accepted Students

    public static class Table {
        public CourseDetailsBundle courseDetails;
        public List<FeedbackSessionTable> feedbackSessions;
        
        public static class FeedbackSessionTable {
            public String name;
            public FeedbackSessionStats stats;
            
            public String getName() {
                return name;
            }

            public String getResponseRate() {
                return String.format("%d / %d", stats.submittedTotal, stats.expectedTotal);
            }
        }
        
        public CourseDetailsBundle getCourseDetails() {
            return courseDetails;
        }

        public List<FeedbackSessionTable> getFeedbackSessions() {
            return feedbackSessions;
        }
    }
    
    public InstructorReportsPageData(AccountAttributes account, String sessionToken) {
        super(account, sessionToken);

        numberOfCourses = 0;
        studentsThatAcceptedInvitation = 0;
        numStudentNotAcceptedInvitation = 0;
        totalStudents = 0;
        numActiveSessions = 0;
        feedbackRate = 0.0;
    }
    
    public void init(
            List<CourseDetailsBundle> courseDetailsList,
            int numberOfCourses, 
            int studentsThatAcceptedInvitation,
            int numStudentNotAcceptedInvitation,
            int numActiveSessions,
            double feedbackRate,
            List<StudentAttributes> studentAttributes,
            List<StudentAttributes> studentNotAcceptedInvitation
    ) {
        
        this.numberOfCourses = numberOfCourses;
        this.studentsThatAcceptedInvitation = studentsThatAcceptedInvitation;
        this.numStudentNotAcceptedInvitation = numStudentNotAcceptedInvitation;
        this.numActiveSessions = numActiveSessions;
        this.courseDetailsList = courseDetailsList;
        this.feedbackRate = feedbackRate;
        this.studentAttributes = studentAttributes;
        this.studentNotAcceptedInvitation = studentNotAcceptedInvitation;
        
        this.coursesTabTableData = courseDetailsList.stream().map(courseDetails -> {
            InstructorReportsPageData.Table table = new InstructorReportsPageData.Table();
            table.courseDetails = courseDetails;
            table.feedbackSessions = courseDetails.feedbackSessions.stream().map(feedbackSession -> {
                Table.FeedbackSessionTable feedbackSessionTable = new Table.FeedbackSessionTable();
                feedbackSessionTable.name = feedbackSession.feedbackSession.getFeedbackSessionName();
                feedbackSessionTable.stats = feedbackSession.stats;
                return feedbackSessionTable;
            }).collect(Collectors.toList());
            return table;
        }).collect(Collectors.toList());
        
        
        
    }
    
    public List<StudentAttributes> getStudentAttributes() {
        return studentAttributes;
    }
    
    public List<StudentAttributes> getStudentNotAcceptedInvitation() {
        return studentNotAcceptedInvitation;
    }
    
    public double getFeedbackRate() {
        return feedbackRate;
    }

    public List<InstructorReportsPageData.Table> getCoursesTabTableData() {
        return coursesTabTableData;
    }

    public List<CourseDetailsBundle> getCourseDetailsList() {
        return courseDetailsList;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public int getStudentsThatAcceptedInvitation() {
        return studentsThatAcceptedInvitation;
    }

    public int getNumStudentNotAcceptedInvitation() {
        return numStudentNotAcceptedInvitation;
    }

    public int getNumActiveSessions() {
        return numActiveSessions;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }
    
    public int getTotalStudents() {
        totalStudents = this.studentsThatAcceptedInvitation + this.numStudentNotAcceptedInvitation;
        return totalStudents;
    }

    public boolean isSortingDisabled() {
        return isSortingDisabled;
    }
    
    public List<CourseTable> getCourseTables() {
        return courseTables;
    }
    
    public int getTotalCourses(List<CourseTable> courseTables) {
        return courseTables.size();
    }
    
    /**
     * Retrieves the link to submit the request to remind particular students.
     * Also contains home page link to return after the action.
     * @return form submit action link
     */
    public String getRemindParticularStudentsLink() {
        return getInstructorFeedbackRemindParticularStudentsLink(Const.ActionURIs.INSTRUCTOR_REPORTS_PAGE);
    }

    /**
     * Retrieves the link to submit the request for resending the session published email.
     * Also contains home page link to return to after the action.
     * @return form submit action link
     */
    public String getSessionResendPublishedEmailLink() {
        return getInstructorFeedbackResendPublishedEmailLink(Const.ActionURIs.INSTRUCTOR_REPORTS_PAGE);
    }

    /**
     * Retrieves the link to submit the request for copy of session.
     * Also contains home page link to return after the action.
     * @return form submit action link
     */
    public String getEditCopyActionLink() {
        return getInstructorFeedbackEditCopyActionLink(Const.ActionURIs.INSTRUCTOR_REPORTS_PAGE);
    }
}
