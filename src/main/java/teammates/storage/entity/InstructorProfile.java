package teammates.storage.entity;

import java.time.Instant;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Unindex;

import teammates.common.util.TimeHelper;

@Entity
@Unindex
public class InstructorProfile extends BaseEntity {
	
	@Parent
	private Key<Account> account;
	
	@Id
	private String googleId;
	
	private String shortName;
	
	private String email;
	
	private String gender;
	
	private Text moreInfo;
	
	private BlobKey pictureKey;
	
	@Index
	private Date modifiedDate;
	
	@SuppressWarnings("unused")
	private InstructorProfile() {
		
	}
	
	
	
	private InstructorProfile(String googleId, String shortName, String email, 
			String gender, Text moreInfo, BlobKey pictureKey) {
		this.setGoogleId(googleId);
		this.setShortName(shortName);
        this.setEmail(email);
        this.setGender(gender);
        this.setMoreInfo(moreInfo);
        this.setModifiedDate(Instant.now());
        this.setPictureKey(pictureKey);
	}
	
	public InstructorProfile(String googleId) {
		this.setGoogleId(googleId);
		this.setShortName("");
        this.setEmail("");
        this.setGender("other");
        this.setMoreInfo(new Text(""));
        this.setModifiedDate(Instant.now());
        this.setPictureKey(new BlobKey(""));
	}
	
    public String getGoogleId() {
        return this.googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
        this.account = Key.create(Account.class, googleId);
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Text getMoreInfo() {
        return this.moreInfo;
    }

    public void setMoreInfo(Text moreInfo) {
        this.moreInfo = moreInfo;
    }

    public BlobKey getPictureKey() {
        return this.pictureKey;
    }

    public void setPictureKey(BlobKey pictureKey) {
        this.pictureKey = pictureKey;
    }

    public Instant getModifiedDate() {
        return TimeHelper.convertDateToInstant(this.modifiedDate);
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = TimeHelper.convertInstantToDate(modifiedDate);
    }

}
