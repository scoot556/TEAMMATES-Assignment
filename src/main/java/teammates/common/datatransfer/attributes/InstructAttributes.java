package teammates.common.datatransfer.attributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Strings;

import teammates.common.datatransfer.InstructorUpdateStatus;
import teammates.common.util.Assumption;
import teammates.common.util.Config;
import teammates.common.util.Const;
import teammates.common.util.FieldValidator;
import teammates.common.util.JsonUtils;
import teammates.common.util.SanitizationHelper;
import teammates.common.util.StringHelper;
import teammates.storage.entity.CourseInstructor;

public class InstructAttributes extends InstructEntityAttributes<CourseInstructor> {
    // Required fields
    public String email;
    public String course;
    public String name;

    // Optional values
    public String googleId;
    public String lastName;
    public String comments;
    public String team;
    public String section;
    public String key;

    public transient InstructorUpdateStatus updateStatus;

    /*
     * Creation and update time stamps.
     * Updated automatically in Student.java, jdoPreStore()
     */
    private transient Instant createdAt;
    private transient Instant updatedAt;

    InstructAttributes() {
        googleId = "";
        section = Const.DEFAULT_SECTION;
        updateStatus = InstructorUpdateStatus.UNKNOWN;
        createdAt = Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP;
        updatedAt = Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP;
    }

    public static InstructAttributes valueOf(CourseInstructor student) {
        return builder(student.getCourseId(), student.getName(), student.getEmail())
                .withLastName(student.getLastName())
                .withComments(student.getComments())
                .withTeam(student.getTeamName())
                .withSection(student.getSectionName())
                .withGoogleId(student.getGoogleId())
                .withKey(student.getRegistrationKey())
                .withCreatedAt(student.getCreatedAt())
                .withUpdatedAt(student.getUpdatedAt())
                .build();
    }

    /**
     * Return new builder instance with default values for optional fields.
     *
     * <p>Following default values are set to corresponding attributes:
     * <ul>
     * <li>{@code googleId = ""}</li>
     * <li>{@code section = Const.DEFAULT_SECTION}</li>
     * <li>{@code updateStatus = InstructorUpdateStatus.UNKNOWN}</li>
     * <li>{@code createdAt = Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP_DATE}</li>
     * <li>{@code updatedAt = Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP_DATE}</li>
     * </ul>
     */
    public static Builder builder(String courseId, String name, String email) {
        return new Builder(courseId, name, email);
    }

    public InstructAttributes getCopy() {
        InstructAttributes instructAttributes = valueOf(toEntity());

        instructAttributes.updateStatus = updateStatus;
        instructAttributes.key = key;
        instructAttributes.createdAt = createdAt;
        instructAttributes.updatedAt = updatedAt;

        return instructAttributes;
    }

    public String toEnrollmentString() {
        String enrollmentStringSeparator = "|";

        return this.section + enrollmentStringSeparator
             + this.team + enrollmentStringSeparator
             + this.name + enrollmentStringSeparator
             + this.email + enrollmentStringSeparator
             + this.comments;
    }

    public boolean isRegistered() {
        return googleId != null && !googleId.isEmpty();
    }

    public String getRegistrationUrl() {
        return Config.getAppUrl(Const.ActionURIs.STUDENT_COURSE_JOIN_NEW)
                                           .withRegistrationKey(StringHelper.encrypt(key))
                                           .withStudentEmail(email)
                                           .withCourseId(course)
                                           .toString();
    }

    public String getPublicProfilePictureUrl() {
        return Config.getAppUrl(Const.ActionURIs.STUDENT_PROFILE_PICTURE)
                           .withStudentEmail(StringHelper.encrypt(email))
                           .withCourseId(StringHelper.encrypt(course))
                           .toString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    /**
     * Format: email%courseId e.g., adam@gmail.com%cs1101.
     */
    public String getId() {
        return email + "%" + course;
    }

    public String getSection() {
        return section;
    }

    public String getTeam() {
        return team;
    }

    public String getComments() {
        return comments;
    }
    
    //Returns course
    public String getCourse() {
    	return course;
    }

    public boolean isEnrollInfoSameAs(InstructAttributes otherStudent) {
        return otherStudent != null && otherStudent.email.equals(this.email)
               && otherStudent.course.equals(this.course)
               && otherStudent.name.equals(this.name)
               && otherStudent.comments.equals(this.comments)
               && otherStudent.team.equals(this.team)
               && otherStudent.section.equals(this.section);
    }

    @Override
    public List<String> getInvalidityInfo() {
        // id is allowed to be null when the student is not registered
        Assumption.assertNotNull(team);
        Assumption.assertNotNull(comments);

        FieldValidator validator = new FieldValidator();
        List<String> errors = new ArrayList<>();

        if (isRegistered()) {
            addNonEmptyError(validator.getInvalidityInfoForGoogleId(googleId), errors);
        }

        addNonEmptyError(validator.getInvalidityInfoForCourseId(course), errors);

        addNonEmptyError(validator.getInvalidityInfoForEmail(email), errors);

        addNonEmptyError(validator.getInvalidityInfoForTeamName(team), errors);

        addNonEmptyError(validator.getInvalidityInfoForSectionName(section), errors);

        addNonEmptyError(validator.getInvalidityInfoForStudentRoleComments(comments), errors);

        addNonEmptyError(validator.getInvalidityInfoForPersonName(name), errors);

        return errors;
    }

    public static void sortBySectionName(List<InstructAttributes> students) {
        students.sort(Comparator.comparing((InstructAttributes student) -> student.section)
                .thenComparing(student -> student.team)
                .thenComparing(student -> student.name));
    }

    public static void sortByTeamName(List<InstructAttributes> students) {
        students.sort(Comparator.comparing((InstructAttributes student) -> student.team)
                .thenComparing(student -> student.name));
    }

    public static void sortByNameAndThenByEmail(List<InstructAttributes> students) {
        students.sort(Comparator.comparing((InstructAttributes student) -> student.name)
                .thenComparing(student -> student.email));
    }

    public void updateWithExistingRecord(InstructAttributes originalStudent) {
        if (this.email == null) {
            this.email = originalStudent.email;
        }

        if (this.name == null) {
            this.name = originalStudent.name;
        }

        if (this.googleId == null) {
            this.googleId = originalStudent.googleId;
        }

        if (this.team == null) {
            this.team = originalStudent.team;
        }

        if (this.comments == null) {
            this.comments = originalStudent.comments;
        }

        if (this.section == null) {
            this.section = originalStudent.section;
        }
    }

    @Override
    public CourseInstructor toEntity() {
        return new CourseInstructor(email, name, googleId, comments, course, team, section);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        String indentString = StringHelper.getIndent(indent);
        StringBuilder sb = new StringBuilder();
        sb.append(indentString + "Student:" + name + "[" + email + "]" + System.lineSeparator());

        return sb.toString();
    }

    @Override
    public String getIdentificationString() {
        return this.course + "/" + this.email;
    }

    @Override
    public String getEntityTypeAsString() {
        return "Student";
    }

    @Override
    public String getBackupIdentifier() {
        return Const.SystemParams.COURSE_BACKUP_LOG_MSG + course;
    }

    @Override
    public String getJsonString() {
        return JsonUtils.toJson(this, InstructAttributes.class);
    }

    @Override
    public void sanitizeForSaving() {
        googleId = SanitizationHelper.sanitizeGoogleId(googleId);
        name = SanitizationHelper.sanitizeName(name);
        comments = SanitizationHelper.sanitizeTextField(comments);
    }

    public String getStudentStatus() {
        if (isRegistered()) {
            return Const.STUDENT_COURSE_STATUS_JOINED;
        }
        return Const.STUDENT_COURSE_STATUS_YET_TO_JOIN;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns true if section value has changed from its original value.
     */
    public boolean isSectionChanged(InstructAttributes originalStudentAttribute) {
        return this.section != null && !this.section.equals(originalStudentAttribute.section);
    }

    /**
     * Returns true if team value has changed from its original value.
     */
    public boolean isTeamChanged(InstructAttributes originalStudentAttribute) {
        return this.team != null && !this.team.equals(originalStudentAttribute.team);
    }

    /**
     * Returns true if email value has changed from its original value.
     */
    public boolean isEmailChanged(InstructAttributes originalStudentAttribute) {
        return this.email != null && !this.email.equals(originalStudentAttribute.email);
    }

    /**
     * A Builder class for {@link InstructAttributes}.
     */
    public static class Builder {
        private static final String REQUIRED_FIELD_CANNOT_BE_NULL = "Required field cannot be null";

        private final InstructAttributes instructAttributes;

        public Builder(String courseId, String name, String email) {
            instructAttributes = new InstructAttributes();

            Assumption.assertNotNull(REQUIRED_FIELD_CANNOT_BE_NULL, courseId, name, email);

            instructAttributes.course = courseId;
            instructAttributes.name = SanitizationHelper.sanitizeName(name);
            instructAttributes.email = email;
            instructAttributes.lastName = processLastName(null);
        }

        public Builder withGoogleId(String googleId) {
            if (googleId != null) {
                instructAttributes.googleId = SanitizationHelper.sanitizeGoogleId(googleId);
            }

            return this;
        }

        public Builder withLastName(String lastName) {
            instructAttributes.lastName = processLastName(lastName);
            return this;
        }

        private String processLastName(String lastName) {
            if (lastName != null) {
                return lastName;
            }

            if (Strings.isNullOrEmpty(instructAttributes.name)) {
                return "";
            }

            String[] nameParts = StringHelper.splitName(instructAttributes.name);
            return nameParts.length < 2 ? "" : SanitizationHelper.sanitizeName(nameParts[1]);
        }

        public Builder withComments(String comments) {
            instructAttributes.comments = SanitizationHelper.sanitizeTextField(comments);
            return this;
        }

        public Builder withTeam(String team) {
            if (team != null) {
                instructAttributes.team = team;
            }
            return this;
        }

        public Builder withSection(String section) {
            instructAttributes.section = section == null ? Const.DEFAULT_SECTION : section;
            return this;
        }

        public Builder withKey(String key) {
            if (key != null) {
                instructAttributes.key = key;
            }
            return this;
        }

        public Builder withUpdateStatus(InstructorUpdateStatus updateStatus) {
            instructAttributes.updateStatus = updateStatus == null
                    ? InstructorUpdateStatus.UNKNOWN
                    : updateStatus;
            return this;
        }

        public Builder withCreatedAt(Instant createdAt) {
            Instant dateToAdd = createdAt == null
                    ? Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP
                    : createdAt;
            instructAttributes.setCreatedAt(dateToAdd);
            return this;
        }

        public Builder withUpdatedAt(Instant updatedAt) {
            Instant dateToAdd = updatedAt == null
                    ? Const.TIME_REPRESENTS_DEFAULT_TIMESTAMP
                    : updatedAt;
            instructAttributes.setUpdatedAt(dateToAdd);
            return this;
        }

        public InstructAttributes build() {
            return instructAttributes;
        }
    }
}
