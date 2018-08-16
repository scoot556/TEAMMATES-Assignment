package teammates.storage.search;

import java.util.List;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.util.Const;

public class CourseSearchQuery extends SearchCourseQuery{

	public CourseSearchQuery(List<CourseAttributes> courses, String queryString) {
		super(courses, queryString);
	}
	
	
	public CourseSearchQuery(String queryString) {
		super(queryString);
	}
	
	@Override
	protected String prepareVisibilityQueryString(List<CourseAttributes> courses) {
		StringBuilder courseIdLimit = new StringBuilder("(");
		String delim = "";
		for (CourseAttributes cour : courses) {
			courseIdLimit.append(delim).append(cour.id);
			delim = OR;
		}
		courseIdLimit.append(")");
		
		return Const.SearchDocumentField.COURSE_ID + ":" + courseIdLimit.toString();
	}
}
