package teammates.common.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.CourseAttributes;

public class CourseSearchResultBundle extends SearchResultBundle{
    public List<CourseAttributes> courseList = new ArrayList<>();
    public Map<String, InstructorAttributes> courseIdInstructorMap = new HashMap<>();
}
