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
	 


}
