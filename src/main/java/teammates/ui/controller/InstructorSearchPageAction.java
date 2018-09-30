package teammates.ui.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teammates.common.datatransfer.FeedbackResponseCommentSearchResultBundle;
import teammates.common.datatransfer.StudentSearchResultBundle;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.common.util.StatusMessage;
import teammates.common.util.StatusMessageColor;
import teammates.ui.pagedata.InstructorSearchPagePaginatedData;

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
        
        // get from search request "items-per-page"
        int itemsPerPage = InstructorSearchPagePaginatedData.GIVEN_ITEMS_PER_PAGE[0]; // 5 items per page
        String itemsPerPageString = getRequestParamValue("items-per-page");
        if (itemsPerPageString != null && !itemsPerPageString.isEmpty()) {
            try { 
                itemsPerPage = Integer.parseInt(itemsPerPageString); 
            } catch (NumberFormatException e) { 
                itemsPerPage = 0; 
            }
        }
        
        // get from search request "page"
        int pageNumber = 1;
        String pageNumberString = getRequestParamValue("page");
        if (pageNumberString != null && !pageNumberString.isEmpty()) {
            try { 
                pageNumber = Integer.parseInt(pageNumberString); 
            } catch (NumberFormatException e) { 
                pageNumber = 1; 
            }
            pageNumber = pageNumber <= 0 ? 1 : pageNumber;
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
        
        boolean isSearchForCourses = getRequestParamAsBoolean("searchcourses");
        if (isSearchForCourses) {
            numberOfSearchOptions++;
        }
        

        FeedbackResponseCommentSearchResultBundle frCommentSearchResults = new FeedbackResponseCommentSearchResultBundle();
        StudentSearchResultBundle studentSearchResults = new StudentSearchResultBundle();
        List<CourseAttributes> courses = new ArrayList<>();
        
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
                courses = logic.searchCourses(searchKey);
            }

            totalResultsSize = frCommentSearchResults.numberOfResults + studentSearchResults.numberOfResults;

            Set<String> instructorEmails = new HashSet<>();

            for (InstructorAttributes instructor : instructors) {
                instructorEmails.add(instructor.email);
            }

            if (totalResultsSize == 0) {
                statusToUser.add(new StatusMessage(Const.StatusMessages.INSTRUCTOR_SEARCH_NO_RESULTS,
                                                   StatusMessageColor.WARNING));
            }
        }
        

        //InstructorSearchPageData data = new InstructorSearchPageData(account, sessionToken);
        InstructorSearchPagePaginatedData data = new InstructorSearchPagePaginatedData(account, sessionToken, itemsPerPage, pageNumber);
        data.init(frCommentSearchResults, studentSearchResults, courses, searchKey, 
                isSearchFeedbackSessionData, isSearchForStudents, isSearchForCourses);
        return createShowPageResult(Const.ViewURIs.INSTRUCTOR_SEARCH, data);
    }
}
