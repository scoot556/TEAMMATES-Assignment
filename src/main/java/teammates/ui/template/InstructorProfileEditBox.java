package teammates.ui.template;

import teammates.common.datatransfer.attributes.InstructorProfileAttributes;
import teammates.common.util.StringHelper;

public class InstructorProfileEditBox {
    
    private String name;
    private String editingPhoto;
    private String shortName;
    private String email;
    private String gender;
    private String moreInfo;
    private String googleId;
    private String pictureUrl;
    
    public InstructorProfileEditBox(String name, String isEditingPhoto, 
            InstructorProfileAttributes profile, String googleId, String pictureUrl) {
        
        this.name = name;
        this.editingPhoto = isEditingPhoto;
        this.shortName = StringHelper.convertToEmptyStringIfNull(profile.shortName);
        this.email = StringHelper.convertToEmptyStringIfNull(profile.email);
        this.gender = profile.gender;
        this.moreInfo = StringHelper.convertToEmptyStringIfNull(profile.moreInfo);
        this.googleId = googleId;
        this.pictureUrl = pictureUrl;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEditingPhoto() {
        return editingPhoto;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getEmail() {
        return email;
    }
    
   public String getGender() {
       return gender;
   }
   
   public String getMoreInfo() {
       return moreInfo;
   }
   
   public String getGoogleId() {
       return googleId;
   }
   
   public String getPictureUrl() {
       return pictureUrl;
   }
   
}
