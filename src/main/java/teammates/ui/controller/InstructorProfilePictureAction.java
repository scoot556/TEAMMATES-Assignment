package teammates.ui.controller;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.InstructorProfileAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.attributes.StudentProfileAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Logger;
import teammates.common.util.StringHelper;


public class InstructorProfilePictureAction extends Action {
    
    private static final Logger log = Logger.getLogger();
    
    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
        boolean isRequestFromStudent = getRequestParamValue(Const.ParamsNames.BLOB_KEY) != null;
        boolean isRequestFromInstructorOrOtherStudent =
                                        getRequestParamValue(Const.ParamsNames.INSTRUCTOR_EMAIL) != null;

        if (!isRequestFromStudent && !isRequestFromInstructorOrOtherStudent) {
            Assumption.fail("expected blob-key, or student email with courseId");
        }

        ActionResult result = null;
        if (isRequestFromStudent) {
            result = handleRequestWithBlobKey();
            statusToAdmin = "Requested Profile Picture by instructor directly";
        } else if (isRequestFromInstructorOrOtherStudent) {
            result = handleRequestWithEmailAndCourse();
            statusToAdmin = "Requested Profile Picture by instructor/other students";
        }

        return result;
    }

    private ActionResult handleRequestWithBlobKey() {
        String blobKey = getBlobKeyFromRequest();
        log.info("blob-key given: " + blobKey);
        return createImageResult(blobKey);
    }

    private ActionResult handleRequestWithEmailAndCourse()
            throws EntityDoesNotExistException {
        String email;
        String courseId;
        try {
            email = getInstructorEmailFromRequest();
            courseId = getCourseIdFromRequest();
        } catch (InvalidParametersException e) {
            log.warning("Attempting to decrypt malformed ciphertext when retrieving email or course id from request.");
            throw new EntityDoesNotExistException(e);
        }

        log.info("email: " + email + ", course: " + courseId);

        InstructorAttributes instructor = getInstructorForGivenParameters(courseId, email);
        gateKeeper.verifyAccessibleForCurrentUserAsInstructorOrTeamMemberOrAdmin(account, courseId, student.section, email);

        return createImageResult(getPictureKeyForInstructor(instructor));
    }

    private InstructorAttributes getInstructorForGivenParameters(String courseId, String email)
            throws EntityDoesNotExistException {
        InstructorAttributes instructor = logic.getInstructorForEmail(courseId, email);
        if (student == null) {
            throw new EntityDoesNotExistException("student with " + courseId + "/" + email);
        }
        return instructor;
    }

    private String getBlobKeyFromRequest() {
        String blobKey = getRequestParamValue(Const.ParamsNames.BLOB_KEY);
        Assumption.assertPostParamNotNull(Const.ParamsNames.BLOB_KEY, blobKey);
        return blobKey;
    }

    private String getCourseIdFromRequest() throws InvalidParametersException {
        String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        Assumption.assertPostParamNotNull(Const.ParamsNames.COURSE_ID, courseId);
        courseId = StringHelper.decrypt(courseId);
        return courseId;
    }

    private String getInstructorEmailFromRequest() throws InvalidParametersException {
        String email = getRequestParamValue(Const.ParamsNames.INSTRUCTOR_EMAIL);
        Assumption.assertPostParamNotNull(Const.ParamsNames.INSTRUCTOR_EMAIL, email);
        email = StringHelper.decrypt(email);
        return email;
    }

    private String getPictureKeyForInstructor(InstructorAttributes instructor) {
        // picture request is only relevant for registered student
        if (!instructor.googleId.isEmpty()) {
            InstructorProfileAttributes profile = logic.getInstructorProfile(instructor.googleId);

            // TODO: remove the null check once all legacy data has been ported
            if (profile != null) {
                return profile.pictureKey;
            }
        }
        return "";
    }
}
