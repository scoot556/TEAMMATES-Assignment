package teammates.ui.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teammates.common.datatransfer.CourseSearchResultBundle;
import teammates.common.datatransfer.FeedbackResponseCommentSearchResultBundle;
import teammates.common.datatransfer.StudentSearchResultBundle;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.common.util.StatusMessage;
import teammates.common.util.StatusMessageColor;
import teammates.ui.pagedata.InstructorSearchPageData;

/**
 * Action: Showing the InstructorSearchPage for an instructor.
 */
public class InstructorSearchPageAction extends Action {

    @Override
    protected ActionResult execute() {
       gateKeeper.verifyInstructorPrivileges(account);
        String searchKey = getRequestParamValue(Const.ParamsNames.SEARCH_KEY);
        if (searchKey == null) {
            searchKey = "";
        }

        int numberOfSearchOptions = 0;

        boolean isSearchForStudents = getRequestParamAsBoolean(Const.ParamsNames.SEARCH_STUDENTS);
        if (isSearchForStudents) {
            numberOfSearchOptions++;
        }

        boolean isSearchFeedbackSessionData = getRequestParamAsBoolean(Const.ParamsNames.SEARCH_FEEDBACK_SESSION_DATA);
        if (isSearchFeedbackSessionData) {
            numberOfSearchOptions++;
        }
		
		boolean isSearchForCourses = getRequestParamAsBoolean(Const.ParamsNames.SEARCH_COURSES);
		if (isSearchForCourses){
			numberOfSearchOptions++;
		}

        FeedbackResponseCommentSearchResultBundle frCommentSearchResults = new FeedbackResponseCommentSearchResultBundle();
        StudentSearchResultBundle studentSearchResults = new StudentSearchResultBundle();
		CourseSearchResultBundle courseSearchResults = new CourseSearchResultBundle();
        int totalResultsSize = 0;

        if (searchKey.isEmpty() || numberOfSearchOptions == 0) {
            //display search tips and tutorials
            statusToUser.add(new StatusMessage(Const.StatusMessages.INSTRUCTOR_SEARCH_TIPS, StatusMessageColor.INFO));
        } else {
            //Start searching
            List<InstructorAttributes> instructors = logic.getInstructorsForGoogleId(account.googleId);
            if (isSearchFeedbackSessionData) {
                frCommentSearchResults = logic.searchFeedbackResponseComments(searchKey, instructors);
            }
            if (isSearchForStudents) {
                studentSearchResults = logic.searchStudents(searchKey, instructors);
            }
			if (isSearchForCourses) {
				courseSearchResults = logic.searchCourses(searchKey, instructors);
			}
				

            totalResultsSize = frCommentSearchResults.numberOfResults + studentSearchResults.numberOfResults + courseSearchResults.numberOfResults;

            Set<String> instructorEmails = new HashSet<>();

            for (InstructorAttributes instructor : instructors) {
                instructorEmails.add(instructor.email);
            }

            if (totalResultsSize == 0) {
                statusToUser.add(new StatusMessage(Const.StatusMessages.INSTRUCTOR_SEARCH_NO_RESULTS,
                                                   StatusMessageColor.WARNING));
            }
        }

        InstructorSearchPageData data = new InstructorSearchPageData(account, sessionToken);
        data.init(frCommentSearchResults, studentSearchResults, courseSearchResults, searchKey, isSearchFeedbackSessionData, isSearchForStudents, isSearchForCourses);

        return createShowPageResult(Const.ViewURIs.INSTRUCTOR_SEARCH, data);
    }
}
