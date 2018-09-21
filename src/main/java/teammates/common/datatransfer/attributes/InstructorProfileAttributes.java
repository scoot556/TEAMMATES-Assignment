package teammates.common.datatransfer.attributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Text;

import teammates.common.datatransfer.attributes.StudentProfileAttributes.Builder;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.FieldValidator;
import teammates.common.util.JsonUtils;
import teammates.common.util.SanitizationHelper;
import teammates.common.util.StringHelper;
import teammates.storage.entity.InstructorProfile;
import teammates.storage.entity.StudentProfile;

public class InstructorProfileAttributes extends EntityAttributes<InstructorProfile> {
	
	public String googleId;
	
	public String shortName;
	public String email;
	public String gender;
	public String moreInfo;
	public String pictureKey;
	public Instant modifiedDate;
	
	InstructorProfileAttributes(String googleId) {
		this.googleId = googleId;
		this.shortName = "";
		this.email = "";
		this.gender = "other";
		this.moreInfo = "";
		this.pictureKey = "";
		this.modifiedDate = Instant.now();
	}
	
	 public static InstructorProfileAttributes valueOf(InstructorProfile ip) {
	        return builder(ip.getGoogleId())
	                .withShortName(ip.getShortName())
	                .withEmail(ip.getEmail())
	                .withGender(ip.getGender())
	                .withMoreInfo(ip.getMoreInfo().getValue())
	                .withPictureKey(ip.getPictureKey().getKeyString())
	                .withModifiedDate(ip.getModifiedDate())
	                .build();
	    }
	 
	 public static Builder builder(String googleId) {
	        return new Builder(googleId);
	    }
	 
	 public InstructorProfileAttributes getCopy() {
	        return builder(googleId)
	                .withShortName(shortName)
	                .withEmail(email)
	                .withGender(gender)
	                .withMoreInfo(moreInfo)
	                .withPictureKey(pictureKey)
	                .withModifiedDate(modifiedDate)
	                .build();
	    }
	 
	 
	 

	    @Override
	    public List<String> getInvalidityInfo() {
	        FieldValidator validator = new FieldValidator();
	        List<String> errors = new ArrayList<>();
	        
	        addNonEmptyError(validator.getInvalidityInfoForGoogleId(googleId), errors);
	        
	        if(!StringHelper.isEmpty(shortName)) {
	            addNonEmptyError(validator.getInvalidityInfoForPersonName(shortName), errors);
	        }
	        
	        if(!StringHelper.isEmpty(email)) {
	            addNonEmptyError(validator.getInvalidityInfoForEmail(email), errors);
	        }
	        
	        addNonEmptyError(validator.getInvalidityInfoForGender(gender), errors);
	        
	        Assumption.assertNotNull(this.pictureKey);
	        
	        return errors;
	    }

	    @Override
	    public InstructorProfile toEntity() {
	        return new InstructorProfile(googleId, shortName, email, gender, 
	                new Text(moreInfo), new BlobKey(this.pictureKey));
	    }

	    @Override
	    public String getIdentificationString() {
	        return this.googleId;
	    }

	    @Override
	    public String getEntityTypeAsString() {
	        return "InstructorProfile";
	    }

	    @Override
	    public String getBackupIdentifier() {
	        return "Instructor profile modified";
	    }

	    @Override
	    public String getJsonString() {
	        return JsonUtils.toJson(this, InstructorProfileAttributes.class);
	    }

	    @Override
	    public void sanitizeForSaving() {
	        this.googleId = SanitizationHelper.sanitizeGoogleId(this.googleId);
	    }
	 

	 private static class Builder {
	     private static final String REQUIRED_FIELD_CANNOT_BE_NULL = "Required field cannot be null";
	     
	     private final InstructorProfileAttributes insprofAttributes;
	     
	     public Builder(String googleId) {
	         Assumption.assertNotNull(REQUIRED_FIELD_CANNOT_BE_NULL, googleId);
	         insprofAttributes = new InstructorProfileAttributes(googleId);
	     }
	     
	     public Builder withShortName(String shortName) {
	         if (shortName != null) {
	             insprofAttributes.shortName = SanitizationHelper.sanitizeName(shortName);
	         }
	         return this;
	     }
	     
	     public Builder withEmail(String email) {
	         if(email != null) {
	             insprofAttributes.email = SanitizationHelper.sanitizeEmail(email);
	         }
	         return this;
	     }
	     
	     public Builder withGender(String gender) {
	         insprofAttributes.gender = isGenderValid(gender) ? gender : "other";
	         return this;
	     }
	     
	     public Builder withMoreInfo(String moreInfo) {
	         if(moreInfo != null) {
	             insprofAttributes.moreInfo = moreInfo;
	         }
	         return this;
	     }
	     
	     public Builder withPictureKey(String pictureKey) {
	         if(pictureKey != null) {
	             insprofAttributes.pictureKey = pictureKey;
	         }
	         return this;
	     }
	     
	     public Builder withModifiedDate(Instant modifiedDate) {
	         insprofAttributes.modifiedDate = modifiedDate == null ? Instant.now() : modifiedDate;
	         return this;
	     }
	     
	     public InstructorProfileAttributes build() {
	         return insprofAttributes;
	     }
	     
	     private boolean isGenderValid(String gender) {
	         return "male".equals(gender) || "female".equals(gender) || "other".equals(gender);
	     }
	 }

}
