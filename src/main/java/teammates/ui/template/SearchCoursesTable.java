package teammates.ui.template;

import java.util.List;

public class SearchCoursesTable {
    private String courseId;
    private String courseName;
    private List<CourseListSectionData> sections;
    private boolean hasSection;
    
    public SearchCoursesTable(String courseId, List<CourseListSectionData> sections) {
        this.courseId = courseId;
        this.sections = sections;
        if (sections.size() == 1) {
            CourseListSectionData section = sections.get(0);
            this.hasSection = !"None".equals(section.getSectionName());
        } else {
            this.hasSection = true;
        }
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public List<CourseListSectionData> getSections() {
        return sections;
    }
    
    public boolean isHasSection() {
        return hasSection;
    }
}
