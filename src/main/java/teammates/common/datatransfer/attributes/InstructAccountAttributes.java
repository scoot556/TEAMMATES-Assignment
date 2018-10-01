package teammates.common.datatransfer.attributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import teammates.common.util.Assumption;
import teammates.common.util.FieldValidator;
import teammates.common.util.JsonUtils;
import teammates.common.util.SanitizationHelper;
import teammates.common.util.StringHelper;
import teammates.storage.entity.InstructAccount;
import teammates.storage.entity.InstructorProfile;

/**
 * A data transfer object for Account entities.
 */
public class InstructAccountAttributes extends EntityAttributes<InstructAccount> {

    //Note: be careful when changing these variables as their names are used in *.json files.

    public String googleId;
    public String name;
    public boolean isInstructor;
    public String email;
    public String institute;
    public Instant createdAt;
    public InstructorProfileAttributes instructorProfile;

    InstructAccountAttributes() {
        // Empty constructor for builder to construct object
    }

    public static InstructAccountAttributes valueOf(InstructAccount a) {
        return builder()
                .withGoogleId(a.getGoogleId())
                .withName(a.getName())
                .withIsInstructor(a.isInstructor())
                .withInstitute(a.getInstitute())
                .withEmail(a.getEmail())
                .withCreatedAt(a.getCreatedAt())
                .withInstructorProfile(a.getInstructorProfile())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * A Builder class for {@link InstructAccountAttributes}.
     */
    public static class Builder {
        private InstructAccountAttributes accountAttributes;

        public Builder() {
            accountAttributes = new InstructAccountAttributes();
        }

        public Builder withCreatedAt(Instant createdAt) {
            accountAttributes.createdAt = createdAt;
            return this;
        }

        public Builder withInstructorProfile(InstructorProfile instructorProfile) {
            accountAttributes.instructorProfile =
            		instructorProfile == null ? null : InstructorProfileAttributes.valueOf(instructorProfile);
            return this;
        }

        public Builder withInstructorProfileAttributes(InstructorProfileAttributes instructorProfileAttributes) {
            accountAttributes.instructorProfile = instructorProfileAttributes;

            return this;
        }

        public Builder withDefaultStudentProfileAttributes(String googleId) {
            accountAttributes.instructorProfile = InstructorProfileAttributes.builder(googleId)
                    .build();

            return this;
        }

        public Builder withGoogleId(String googleId) {
            accountAttributes.googleId = googleId;
            return this;
        }

        public Builder withName(String name) {
            accountAttributes.name = name;
            return this;
        }

        public Builder withIsInstructor(boolean isInstructor) {
            accountAttributes.isInstructor = isInstructor;
            return this;
        }

        public Builder withEmail(String email) {
            accountAttributes.email = email;
            return this;
        }

        public Builder withInstitute(String institute) {
            accountAttributes.institute = institute;
            return this;
        }

        public InstructAccountAttributes build() {
            accountAttributes.googleId = SanitizationHelper.sanitizeGoogleId(accountAttributes.googleId);
            accountAttributes.name = SanitizationHelper.sanitizeName(accountAttributes.name);
            accountAttributes.email = SanitizationHelper.sanitizeEmail(accountAttributes.email);
            accountAttributes.institute = SanitizationHelper.sanitizeTitle(accountAttributes.institute);
            if (accountAttributes.instructorProfile != null) {
                accountAttributes.instructorProfile.sanitizeForSaving();
            }

            return accountAttributes;
        }

    }

    /**
     * Gets a deep copy of this object.
     */
    public InstructAccountAttributes getCopy() {
        return InstructAccountAttributes.builder()
                .withGoogleId(googleId)
                .withName(name)
                .withEmail(email)
                .withInstitute(institute)
                .withIsInstructor(isInstructor)
                .withInstructorProfileAttributes(this.instructorProfile == null ? null : this.instructorProfile.getCopy())
                .build();

    }

    public boolean isInstructor() {
        return isInstructor;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTruncatedGoogleId() {
        return StringHelper.truncateLongId(googleId);
    }

    public String getInstitute() {
        return institute;
    }

    @Override
    public List<String> getInvalidityInfo() {
        FieldValidator validator = new FieldValidator();
        List<String> errors = new ArrayList<>();

        addNonEmptyError(validator.getInvalidityInfoForPersonName(name), errors);

        addNonEmptyError(validator.getInvalidityInfoForGoogleId(googleId), errors);

        addNonEmptyError(validator.getInvalidityInfoForEmail(email), errors);

        addNonEmptyError(validator.getInvalidityInfoForInstituteName(institute), errors);

        Assumption.assertNotNull("Non-null value expected for instructorProfile", this.instructorProfile);
        // only check profile if the account is proper
        if (errors.isEmpty()) {
            errors.addAll(this.instructorProfile.getInvalidityInfo());
        }

        //No validation for isInstructor and createdAt fields.
        return errors;
    }

    @Override
    public InstructAccount toEntity() {
        Assumption.assertNotNull(this.instructorProfile);
        return new InstructAccount(googleId, name, isInstructor, email, institute, instructorProfile.toEntity());
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this, InstructAccountAttributes.class);
    }

    @Override
    public String getIdentificationString() {
        return this.googleId;
    }

    @Override
    public String getEntityTypeAsString() {
        return "Account";
    }

    @Override
    public String getBackupIdentifier() {
        return "Account";
    }

    @Override
    public String getJsonString() {
        return JsonUtils.toJson(this, InstructAccountAttributes.class);
    }

    @Override
    public void sanitizeForSaving() {
        this.googleId = SanitizationHelper.sanitizeForHtml(googleId);
        this.name = SanitizationHelper.sanitizeForHtml(name);
        this.institute = SanitizationHelper.sanitizeForHtml(institute);
        if (instructorProfile == null) {
            return;
        }
        this.instructorProfile.sanitizeForSaving();
    }

    public boolean isUserRegistered() {
        return googleId != null && !googleId.isEmpty();
    }

}
