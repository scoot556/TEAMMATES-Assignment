package teammates.ui.template;

import teammates.common.datatransfer.attributes.InstructorProfileAttributes;

public class InstructorProfile {
	private String pictureUrl;
	private String name;
	private String shortName;
	private String gender;
	private String email;
	private String moreInfo;
	
	public InstructorProfile(String fullName, InstructorProfileAttributes instructor, String pictureUrl) {
	    this.pictureUrl = pictureUrl;
	    this.name = fullName;
	    this.shortName = instructor.shortName;
	    this.gender = instructor.gender;
	    this.email = instructor.email;
	    this.moreInfo = instructor.moreInfo;
	}
	
	public String getPictureUrl() {
	    return pictureUrl;
	}
	
	public String getName() {
	    return name;
	}
	
	public String getShortName() {
	    return shortName;
	}
	
	public String getGender() {
	    return gender;
	}
	
	public String getEmail() {
	    return email;
	}
	
	public String getMoreInfo() {
	    return moreInfo;
	}
}
