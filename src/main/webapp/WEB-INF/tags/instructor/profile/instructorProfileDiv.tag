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
      title="<%= Const.Tooltips. %>
