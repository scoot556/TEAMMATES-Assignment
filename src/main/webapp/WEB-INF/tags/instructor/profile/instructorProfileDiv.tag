<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="instructorProfile - Edit Profile div" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ attribute name="profile" type="teammates.ui.template.InstructorProfileEditBox" required="true" %>
<%@ attribute name="sessionToken" required="true" %>
<c:set var="MALE" value="<%= Const.GenderTypes.MALE %>"
<c:set var="FEMALE" value="<%= Const.GenderTypes.FEMALE %>"
<c:set var="OTHER" value="<%= Const.GenderTypes.OTHER %>"

<div id="editProfileDiv" class="well well-plain well-narrow well-sm-wide">
  <h3 id="instructorName">
    <strong>${profile.name}</strong>
  </h3>
  <br>
  <div class="form-group row">
    <div class="col-xs-6 col-sm-5 col-md-3 cursor-pointer"
        title="<%= Const.Tooltips.INSTRUCTOR_PROFILE_PICTURE %>"
        data-toggle="tooltip"
        data-placement="top">
      <img id="profilePic"
          src="${profile.pictureUrl}"
          class="profile-pic"
          data-toggle="modal"
          data-target="#instructorPhotoUploader"
          data-edit="${profile.editingPhoto}">
     </div>
     <div class="">
       <button id="uploadEditPhoto"
           class="btn btn-primary"
           type="button"
           data-toggle="modal"
           data-target="#instructorPhotoUploader">
           Upload/Edit Photo
        </button>
     </div>
</div>

<form class="form center-block"
    role="form"
    method="post"
    action="<%= Const.ActionURIs.INSTRUCTOR_PROFILE_PICTURE_EDIT_SAVE %>">
  <div class="form-group"
      title="<%= Const.Tooltips.INSTRUCTOR_PROFILE_SHORTNAME %>"
      data-toggle="tootltip"
      data-placement="top">
    <label for="instructorNickname">
    	The name you prefer to be called
    </label>
    <input id="instructorShortname"
    	name="<%= Const.ParamsNames.INSTRUCTOR_SHORT_NAME %>"
    	class="form-control"
    	type="text"
    	data-actual-value="<c:out value="${profile.shortName}"/>"
    	value="<c:out value="${profile.shortName}"/>"
    	placeholder="How you should be called">
    </div>
    <div class="form-group"
        title="<%= Const.Tooltips.INSTRUCTOR_PROFILE_EMAIL %>"
        data-toggle="tooltip"
        data-placement="top">
      <label for="instructorEmail">
        Long term contact email <em class="font-weight-normal emphasis text-muted small"></em>
      </label>
      <input id="instructorEmail"
          name="<%= Const.ParamsNames.INSTRUCTOR_PROFILE_EMAIL %>"
          class="form-control"
          type="email"
          data-actual-value="<c:out value="${profile.email}"/>"
          value="<c:out value="${profile.email}"/>"
          placeholder="Contact Email">
    </div>
    <div class="form-group">
      <label for="intstructorGender">
        Gender
      </label>
      <div id="instructorGender">
        <label for="genderMale" class="radio-inline">
          <input id="genderMale"
              name="<%= Const.ParamsNames.INSTRUCTOR_GENDER %>"
              class="radio"
              type="radio"
              value="<%= Const.GenderTypes.MALE %>"
              <c:if test="${profile.gender == MALE}">checked=""</c:if>> Male
        </label>
        <label for="genderFemale" class="radio-inline">
          <input id="genderFemale"
              name="<%= Const.ParamsNames.INSTRUCTORT_GENDER %>"
              class="radio"
              type="radio"
              value="<%= Const.GenderTypes.FEMALE %>"
              <c:if test="${profile.gender == FEMALE}">checked=""</c:if>> Female
        </label>
        <label class="radio-inline" for="genderOther">
          <input id="genderOther"
              name="<%= Const.ParamsNames.INSTRUCTOR_GENDER %>"
              class="radio"
              type="radio"
              value="<%= Const.GenderTypes.OTHER %>"
              <c:if test="${profile.gender == OTHER}">checked=""</c:if>> Not Specified
        </label>
      </div>
    </div>
    <div class="form-group"
        title="<%= Const.Tooltips.INSTRUCTOR_PROFILE_MOREINFO %>"
        data-toggle="tooltip"
        data-placement="top">
      <label for="instructorNationality">
        More info about yourself
      </label>
      <%-- Do not add whitespace between the opening and closing tags --%>
      <textarea id="instructorMoreInfo"
          name="<%= Const.ParamsNames.INSTRUCTOR_PROFILE_MOREINFO %>"
          rows="4"
          class="form-control"
          placeholder="<%= Const.Tooltips.INSTRUCTOR_PROFILE_MOREINFO %>">${profile.moreInfo}</textarea>
    </div>
    <br>
    <button type="submit" id="profileEditSubmit" class="btn btn-primary center-block">
      Save Profile
    </button>
    <br>
    <p class="text-muted text-color-disclaimer">
      <i>* This profile will be visible to all your Instructors and Coursemates</i>
    </p>
    <input type="hidden" name="<%= Const.ParamsNames.USER_ID %>" value="${profile.googleId}">
    <input type="hidden" name="<%= Const.ParamsNames.SESSION_TOKEN %>" value="${sessionToken}">
  </form>
</div>
    
    
    
    
    
    
    
    
    