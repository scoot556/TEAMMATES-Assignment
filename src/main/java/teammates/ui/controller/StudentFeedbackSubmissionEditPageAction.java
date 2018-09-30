package teammates.ui.controller;


import com.google.appengine.api.blobstore.BlobstoreFailureException;

import teammates.common.datatransfer.FeedbackSessionQuestionsBundle;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;
import teammates.common.util.GoogleCloudStorageHelper;
import teammates.common.util.Logger;

public class StudentFeedbackSubmissionEditPageAction extends FeedbackSubmissionEditPageAction {
    
    private static final Logger log = Logger.getLogger();
    
    @Override
    protected boolean isSpecificUserJoinedCourse() {
        if (student == null) {
            return isJoinedCourse(courseId);
        }
        return student.course.equals(courseId);
    }

    @Override
    protected void verifyAccessibleForSpecificUser(FeedbackSessionAttributes fsa) {
        gateKeeper.verifyAccessible(getStudent(), fsa);
    }

    @Override
    protected String getUserEmailForCourse() {
        if (student == null) {
            // Not covered as this shouldn't happen since verifyAccessibleForSpecific user is always
            // called before this, calling getStudent() and making student not null in any case
            // This still acts as a safety net, however, and should stay
            return getStudent().email;
        }
        return student.email;
    }

    @Override
    protected FeedbackSessionQuestionsBundle getDataBundle(String userEmailForCourse) throws EntityDoesNotExistException {
        return logic.getFeedbackSessionQuestionsBundleForStudent(feedbackSessionName, courseId, userEmailForCourse);
    }

    @Override
    protected boolean isSessionOpenForSpecificUser(FeedbackSessionAttributes session) {
        return session.isOpened();
    }

    @Override
    protected void setStatusToAdmin() {
        statusToAdmin = "Show student feedback submission edit page<br>" + "Session Name: "
                        + feedbackSessionName + "<br>" + "Course ID: " + courseId;
    }

    @Override
    protected ShowPageResult createSpecificShowPageResult() {
        String uploadUrl = "";
        
        try {
            uploadUrl = GoogleCloudStorageHelper.getNewUploadUrl(Const.ActionURIs.STUDENT_FEEDBACK_SUBMISSION_EDIT_SAVE);
        } catch (BlobstoreFailureException e) {
            log.info(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        
        data.setSubmitAction(uploadUrl);

        return createShowPageResult(Const.ViewURIs.STUDENT_FEEDBACK_SUBMISSION_EDIT, data);
    }

    @Override
    protected RedirectResult createSpecificRedirectResult() throws EntityDoesNotExistException {
        if (!isRegisteredStudent()) {
            throw new EntityDoesNotExistException("unregistered student trying to access non-existent session");
        }
        return createRedirectResult(Const.ActionURIs.STUDENT_HOME_PAGE);
    }

    private StudentAttributes getStudent() {
        if (student == null) {
            // branch of student != null is not covered since student is not set elsewhere, but this
            // helps to speed up the process of 'getting' a student so we should leave it here
            student = logic.getStudentForGoogleId(courseId, account.googleId);
        }

        return student;
    }

    protected boolean isRegisteredStudent() {
        return account.isUserRegistered();
    }
}
