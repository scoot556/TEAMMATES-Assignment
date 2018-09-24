package teammates.storage.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;

import teammates.common.datatransfer.attributes.InstructorProfileAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Logger;
import teammates.common.util.ThreadHelper;
import teammates.storage.entity.Account;
import teammates.storage.entity.InstructorProfile;


/**
 * Handles CRUD operations for student profiles.
 *
 * @see InstructorProfile
 * @see InstructorProfileAttributes
 */
public class InstructorProfilesDb extends EntitiesDb<InstructorProfile, InstructorProfileAttributes> {

    private static final Logger log = Logger.getLogger();

    /**
     * Gets the datatransfer (*Attributes) version of the profile
     * corresponding to the googleId given. Returns null if the
     * profile was not found
     */
    public InstructorProfileAttributes getInstructorProfile(String accountGoogleId) {
        return makeAttributesOrNull(getInstructorProfileEntityFromDb(accountGoogleId));
    }

    /**
     * Updates the entire profile based on the given new profile attributes.
     * Assumes that the googleId remains the same and so updates the profile
     * with the given googleId.
     */
    // TODO: update the profile with whatever given values are valid and ignore those that are not valid.
    public void updateInstructorProfile(InstructorProfileAttributes newSpa)
            throws InvalidParametersException, EntityDoesNotExistException {
        validateNewProfile(newSpa);

        InstructorProfile profileToUpdate = getCurrentProfileFromDb(newSpa.googleId);
        if (hasNoNewChangesToProfile(newSpa, profileToUpdate)) {
            return;
        }

        updateProfileWithNewValues(newSpa, profileToUpdate);
    }

    private void validateNewProfile(InstructorProfileAttributes newSpa) throws InvalidParametersException {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, newSpa);

        if (!newSpa.isValid()) {
            throw new InvalidParametersException(newSpa.getInvalidityInfo());
        }
    }

    private boolean hasNoNewChangesToProfile(InstructorProfileAttributes newSpa, InstructorProfile profileToUpdate) {
        InstructorProfileAttributes newSpaCopy = newSpa.getCopy();
        InstructorProfileAttributes existingProfile = InstructorProfileAttributes.valueOf(profileToUpdate);

        newSpaCopy.modifiedDate = existingProfile.modifiedDate;
        return existingProfile.toString().equals(newSpaCopy.toString());
    }

    private void updateProfileWithNewValues(InstructorProfileAttributes newSpa, InstructorProfile profileToUpdate) {
        newSpa.sanitizeForSaving();

        profileToUpdate.setShortName(newSpa.shortName);
        profileToUpdate.setEmail(newSpa.email);
        profileToUpdate.setGender(newSpa.gender);
        profileToUpdate.setMoreInfo(new Text(newSpa.moreInfo));
        profileToUpdate.setModifiedDate(Instant.now());

        boolean hasNewNonEmptyPictureKey = !newSpa.pictureKey.isEmpty()
                && !newSpa.pictureKey.equals(profileToUpdate.getPictureKey().getKeyString());
        if (hasNewNonEmptyPictureKey) {
            profileToUpdate.setPictureKey(new BlobKey(newSpa.pictureKey));
        }

        saveEntity(profileToUpdate);
    }

    /**
     * Updates the pictureKey of the profile with given GoogleId.
     * Deletes existing picture if key is different and updates
     * modifiedDate
     */
    public void updateInstructorProfilePicture(String googleId, String newPictureKey) throws EntityDoesNotExistException {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, googleId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, newPictureKey);
        Assumption.assertNotEmpty("GoogleId is empty", googleId);
        Assumption.assertNotEmpty("PictureKey is empty", newPictureKey);

        InstructorProfile profileToUpdate = getCurrentProfileFromDb(googleId);

        boolean hasNewNonEmptyPictureKey = !newPictureKey.isEmpty()
                && !newPictureKey.equals(profileToUpdate.getPictureKey().getKeyString());
        if (hasNewNonEmptyPictureKey) {
            profileToUpdate.setPictureKey(new BlobKey(newPictureKey));
            profileToUpdate.setModifiedDate(Instant.now());
        }

        saveEntity(profileToUpdate);
    }

    @Override
    public void deleteEntity(InstructorProfileAttributes entityToDelete) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entityToDelete);

        Key<InstructorProfile> keyToDelete = getEntityQueryKeys(entityToDelete).first().now();
        if (keyToDelete == null) {
            ofy().delete().keys(getEntityQueryKeysForLegacyData(entityToDelete)).now();
        } else {
            ofy().delete().key(keyToDelete).now();
        }

        log.info(entityToDelete.getBackupIdentifier());
    }

    @Override
    public void deleteEntities(Collection<InstructorProfileAttributes> entitiesToDelete) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entitiesToDelete);

        ArrayList<Key<InstructorProfile>> keysToDelete = new ArrayList<>();
        for (InstructorProfileAttributes entityToDelete : entitiesToDelete) {
            Key<InstructorProfile> keyToDelete = getEntityQueryKeys(entityToDelete).first().now();
            if (keyToDelete == null) {
                keyToDelete = getEntityQueryKeysForLegacyData(entityToDelete).first().now();
            }
            if (keyToDelete == null) {
                continue;
            }
            keysToDelete.add(keyToDelete);
            log.info(entityToDelete.getBackupIdentifier());
        }

        ofy().delete().keys(keysToDelete).now();
    }

    /**
     * Deletes the profile picture from GCS and
     * updates the profile entity:
     *     empties the key and updates the modifiedDate.
     */
    public void deleteInstructorProfilePicture(String googleId) throws EntityDoesNotExistException {
        InstructorProfile sp = getCurrentProfileFromDb(googleId);

        if (!sp.getPictureKey().equals(new BlobKey(""))) {
            deletePicture(sp.getPictureKey());
            sp.setPictureKey(new BlobKey(""));
            sp.setModifiedDate(Instant.now());
        }

        saveEntity(sp);
    }

    /**
     * This method is not scalable. Not to be used unless for admin features.
     *
     * @return the list of all student profiles in the database.
     */
    @Deprecated
    public List<InstructorProfileAttributes> getAllInstructorProfiles() {
        return makeAttributes(getInstructorProfileEntities());
    }

    //-------------------------------------------------------------------------------------------------------
    //-------------------------------------- Helper Functions -----------------------------------------------
    //-------------------------------------------------------------------------------------------------------

    private InstructorProfile getCurrentProfileFromDb(String googleId) throws EntityDoesNotExistException {
        InstructorProfile profileToUpdate = getInstructorProfileEntityFromDb(googleId);

        if (profileToUpdate == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT_STUDENT_PROFILE + googleId
                    + ThreadHelper.getCurrentThreadStack());
        }

        return profileToUpdate;
    }

    /**
     * Checks if an account entity exists for the given googleId and creates
     * a profile entity for this account. This is only used for porting
     * legacy account entities on the fly.
     */
    // TODO: remove this function once legacy data have been ported over
    private InstructorProfile getInstructorProfileEntityForLegacyData(String googleId) {
        Account account = ofy().load().type(Account.class).id(googleId).now();

        if (account == null) {
            return null;
        }

        InstructorProfile profile = new InstructorProfile(account.getGoogleId());
        account.setInstructorProfile(profile);

        return profile;
    }

    /**
     * Gets the profile entity associated with given googleId.
     * If the profile does not exist, it tries to get the
     * profile from the function
     * 'getInstructorProfileEntityForLegacyData'.
     */
    // TODO: update this function once legacy data have been ported over
    private InstructorProfile getInstructorProfileEntityFromDb(String googleId) {
        Key<Account> parentKey = Key.create(Account.class, googleId);
        Key<InstructorProfile> childKey = Key.create(parentKey, InstructorProfile.class, googleId);
        InstructorProfile profile = ofy().load().key(childKey).now();

        if (profile == null) {
            return getInstructorProfileEntityForLegacyData(googleId);
        }

        return profile;
    }

    @Override
    protected LoadType<InstructorProfile> load() {
        return ofy().load().type(InstructorProfile.class);
    }

    @Override
    protected InstructorProfile getEntity(InstructorProfileAttributes attributes) {
        // this method is never used and is here only for future expansion and completeness
        return getInstructorProfileEntityFromDb(attributes.googleId);
    }

    @Override
    protected QueryKeys<InstructorProfile> getEntityQueryKeys(InstructorProfileAttributes attributes) {
        Key<Account> parentKey = Key.create(Account.class, attributes.googleId);
        Key<InstructorProfile> childKey = Key.create(parentKey, InstructorProfile.class, attributes.googleId);
        return load().filterKey(childKey).keys();
    }

    private QueryKeys<InstructorProfile> getEntityQueryKeysForLegacyData(InstructorProfileAttributes attributes) {
        Key<InstructorProfile> legacyKey = Key.create(InstructorProfile.class, attributes.googleId);
        return load().filterKey(legacyKey).keys();
    }

    @Override
    public boolean hasEntity(InstructorProfileAttributes attributes) {
        if (getEntityQueryKeys(attributes).first().now() == null) {
            return getEntityQueryKeysForLegacyData(attributes).first().now() != null;
        }
        return true;
    }

    /**
     * Retrieves all student profile entities. This function is not scalable.
     */
    @Deprecated
    private List<InstructorProfile> getInstructorProfileEntities() {
        return load().list();
    }

    @Override
    protected InstructorProfileAttributes makeAttributes(InstructorProfile entity) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entity);

        return InstructorProfileAttributes.valueOf(entity);
    }
}
