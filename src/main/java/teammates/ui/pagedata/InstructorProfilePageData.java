package teammates.ui.pagedata;

import teammates.common.datatransfer.attributes.InstructAccountAttributes;
import teammates.common.datatransfer.attributes.InstructorProfileAttributes;
import teammates.common.util.Const;
import teammates.ui.template.InstructorProfileEditBox;
import teammates.ui.template.InstructorProfileUploadPhotoModal;

public class InstructorProfilePageData extends InstructPageData {

    private InstructorProfileEditBox profileEditBox;
    private InstructorProfileUploadPhotoModal uploadPhotoModal;

    public InstructorProfilePageData(InstructAccountAttributes account, String sessionToken, String isEditingPhoto) {
        super(account, sessionToken);
        InstructorProfileAttributes profile = account.instructorProfile;
        String pictureUrl;
        if (profile.pictureKey.isEmpty()) {
            pictureUrl = Const.SystemParams.DEFAULT_PROFILE_PICTURE_PATH;
        } else {
            pictureUrl = Const.ActionURIs.INSTRUCTOR_PROFILE_PICTURE
                       + "?" + Const.ParamsNames.BLOB_KEY + "=" + profile.pictureKey
                       + "&" + Const.ParamsNames.USER_ID + "=" + account.googleId;
        }
        this.profileEditBox = new InstructorProfileEditBox(account.name, isEditingPhoto, profile,
                                                        account.googleId, pictureUrl);
        this.uploadPhotoModal = new InstructorProfileUploadPhotoModal(account.googleId, pictureUrl, profile.pictureKey);

    }

    public InstructorProfileEditBox getProfileEditBox() {
        return profileEditBox;
    }

    public InstructorProfileUploadPhotoModal getUploadPhotoModal() {
        return uploadPhotoModal;
    }

}
