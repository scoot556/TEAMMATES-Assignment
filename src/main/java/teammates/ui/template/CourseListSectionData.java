package teammates.ui.template;

import java.util.Map;

import teammates.common.datatransfer.SectionDetailsBundle;


public class CourseListSectionData {
    private String sectionName;
    private boolean isAllowedToViewCourseInSection;
    private boolean isAllowedToModifyCourse;
    
    public CourseListSectionData(SectionDetailsBundle section, boolean isAllowedToViewCourseInSection,
            boolean isAllowedToModifyCourse, Map<String, String> emailPhotoUrlMapping, String googleId, String sessionToken,
            String previousPage) {
        this.sectionName = section.name;
        this.isAllowedToViewCourseInSection = isAllowedToViewCourseInSection;
        this.isAllowedToModifyCourse = isAllowedToModifyCourse;
    }
    
    public String getSectionName() {
        return sectionName;
    }
    
    public boolean isAllowedToViewCourseInSection() {
        return isAllowedToViewCourseInSection;
    }
    
    public boolean isAllowedToModifyCourse() {
        return isAllowedToModifyCourse;
    }
}
