package teammates.storage.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;

import teammates.common.datatransfer.attributes.InstructAccountAttributes;
import teammates.common.datatransfer.attributes.InstructorProfileAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.ThreadHelper;
import teammates.storage.entity.InstructAccount;
import teammates.storage.entity.InstructorProfile;

/**
 * Handles CRUD operations for InstructAccounts.
 *
 * @see InstructAccount
 * @see InstructAccountAttributes
 */
public class InstructAccountsDb  extends EntitiesDb<InstructAccount, InstructAccountAttributes> {
    private InstructProfilesDb InstructProfilesDb = new InstructProfilesDb();

    /**
     * Preconditions:
     * <br> * {@code InstructAccountToAdd} is not null and has valid data.
     */
    public void createInstructAccount(InstructAccountAttributes InstructAccountToAdd)
            throws InvalidParametersException {
        // TODO: use createEntity once there is a proper way to add instructor InstructAccounts.
        try {
            // this is for legacy code to be handled
            if (InstructAccountToAdd != null && InstructAccountToAdd.instructorProfile == null) {
                InstructAccountToAdd.instructorProfile = InstructorProfileAttributes.builder(InstructAccountToAdd.googleId).build();
            }
            createEntity(InstructAccountToAdd);

        } catch (EntityAlreadyExistsException e) {
            // We update the InstructAccount instead if it already exists. This is due to how
            // adding of instructor InstructAccounts work.
            try {
                updateInstructAccount(InstructAccountToAdd, true);
            } catch (EntityDoesNotExistException edne) {
                // This situation is not tested as replicating such a situation is
                // difficult during testing
                Assumption.fail("InstructAccount found to be already existing and not existing simultaneously");
            }
        }

        try {
            InstructProfilesDb.createEntity(InstructAccountToAdd.instructorProfile);
        } catch (EntityAlreadyExistsException e) {
            try {
                InstructProfilesDb.updateInstructorProfile(InstructAccountToAdd.instructorProfile);
            } catch (EntityDoesNotExistException edne) {
                // This situation is not tested as replicating such a situation is
                // difficult during testing
                Assumption.fail("InstructorProfile found to be already existing and not existing simultaneously");
            }
        }
    }

    @Override
    public List<InstructAccount> createEntitiesDeferred(Collection<InstructAccountAttributes> InstructAccountsToAdd)
            throws InvalidParametersException {
        List<InstructorProfileAttributes> profilesToAdd = new LinkedList<>();
        for (InstructAccountAttributes InstructAccountToAdd : InstructAccountsToAdd) {
            profilesToAdd.add(InstructAccountToAdd.instructorProfile);
        }
        InstructProfilesDb.createEntitiesDeferred(profilesToAdd);
        return super.createEntitiesDeferred(InstructAccountsToAdd);
    }

    /**
     * Gets the data transfer version of the InstructAccount. Does not retrieve the profile
     * if the given parameter is false<br>
     * Preconditions:
     * <br> * All parameters are non-null.
     * @return Null if not found.
     */
    public InstructAccountAttributes getInstructAccount(String googleId, boolean retrieveInstructorProfile) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, googleId);
        return googleId.isEmpty() ? null : makeAttributesOrNull(getInstructAccountEntity(googleId, retrieveInstructorProfile));
    }

    public InstructAccountAttributes getInstructAccount(String googleId) {
        return getInstructAccount(googleId, false);
    }

    /**
     * Returns {@link InstructAccountAttributes} objects for all InstructAccounts with instructor privileges.
     *         Returns an empty list if no such InstructAccounts are found.
     */
    public List<InstructAccountAttributes> getInstructorInstructAccounts() {
        return makeAttributes(
                load().filter("isInstructor =", true).list());
    }

    /**
     * Preconditions:
     * <br> * {@code InstructAccountToAdd} is not null and has valid data.
     */
    public void updateInstructAccount(InstructAccountAttributes a, boolean updateInstructorProfile)
            throws InvalidParametersException, EntityDoesNotExistException {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, a);

        if (!a.isValid()) {
            throw new InvalidParametersException(a.getInvalidityInfo());
        }

        InstructAccount InstructAccountToUpdate = getInstructAccountEntity(a.googleId, updateInstructorProfile);

        if (InstructAccountToUpdate == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT_ACCOUNT + a.googleId
                + ThreadHelper.getCurrentThreadStack());
        }

        a.sanitizeForSaving();
        InstructAccountToUpdate.setName(a.name);
        InstructAccountToUpdate.setEmail(a.email);
        InstructAccountToUpdate.setIsInstructor(a.isInstructor);
        InstructAccountToUpdate.setInstitute(a.institute);

        if (updateInstructorProfile) {
            InstructorProfile existingProfile = InstructAccountToUpdate.getInstructorProfile();
            if (existingProfile == null) {
                existingProfile = new InstructorProfile(a.instructorProfile.googleId);
            }

            InstructorProfileAttributes existingProfileAttributes = InstructorProfileAttributes.valueOf(existingProfile);
            a.instructorProfile.modifiedDate = existingProfileAttributes.modifiedDate;

            // if the student profile has changed then update the store
            // this is to maintain integrity of the modified date.
            if (!existingProfileAttributes.toString().equals(a.instructorProfile.toString())) {
                InstructorProfile updatedProfile = a.instructorProfile.toEntity();
                InstructAccountToUpdate.setInstructorProfile(updatedProfile);
                InstructProfilesDb.saveEntity(updatedProfile);
            }
        }
        saveEntity(InstructAccountToUpdate, a);
    }

    public void updateInstructAccount(InstructAccountAttributes a)
            throws InvalidParametersException, EntityDoesNotExistException {
        if (a != null && a.instructorProfile == null) {
            a.instructorProfile = InstructorProfileAttributes.builder(a.googleId).build();
        }
        updateInstructAccount(a, false);
    }

    /**
     * Note: This is a non-cascade delete. <br>
     *   <br> Fails silently if there is no such InstructAccount.
     * <br> Preconditions:
     * <br> * {@code googleId} is not null.
     */
    public void deleteInstructAccount(String googleId) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, googleId);

        InstructAccount InstructAccountToDelete = getInstructAccountEntity(googleId, true);

        if (InstructAccountToDelete == null) {
            return;
        }

        InstructorProfile InstructorProfile = InstructAccountToDelete.getInstructorProfile();
        if (InstructorProfile != null) {
            BlobKey pictureKey = InstructorProfile.getPictureKey();
            if (!pictureKey.getKeyString().isEmpty()) {
                deletePicture(pictureKey);
            }
            InstructProfilesDb.deleteEntityDirect(InstructorProfile);
        }

        deleteEntityDirect(InstructAccountToDelete);
    }

    public void deleteAccounts(Collection<InstructAccountAttributes> accounts) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, accounts);

        for (InstructAccountAttributes InstructAccountToDelete : accounts) {
            deleteInstructAccount(InstructAccountToDelete.googleId);
        }
    }

    private InstructAccount getInstructAccountEntity(String googleId, boolean retrieveInstructorProfile) {
        InstructAccount InstructAccount = load().id(googleId).now();
        if (InstructAccount == null) {
            return null;
        }

        InstructAccount.setIsInstructorProfileEnabled(retrieveInstructorProfile);

        return InstructAccount;
    }

    private InstructAccount getInstructAccountEntity(String googleId) {
        return getInstructAccountEntity(googleId, false);
    }

    @Override
    protected LoadType<InstructAccount> load() {
        return ofy().load().type(InstructAccount.class);
    }

    @Override
    protected InstructAccount getEntity(InstructAccountAttributes entity) {
        return getInstructAccountEntity(entity.googleId);
    }

    @Override
    protected QueryKeys<InstructAccount> getEntityQueryKeys(InstructAccountAttributes attributes) {
        Key<InstructAccount> keyToFind = Key.create(InstructAccount.class, attributes.googleId);
        return load().filterKey(keyToFind).keys();
    }

    @Override
    protected InstructAccountAttributes makeAttributes(InstructAccount entity) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entity);

        return InstructAccountAttributes.valueOf(entity);
    }
}
