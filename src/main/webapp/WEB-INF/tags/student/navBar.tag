<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="Student Navigation Bar" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<c:set var="isUnregistered" value="${data.unregisteredStudent}" />
<div id="NavTop">
  <div class="container clearfix">
  
    <ul id="NavTop-left">
      <li<c:if test="${fn:contains(data.getClass(), 'StudentHelp')}"> class="active"</c:if>>
        <a id="studentHelpLink" class="nav" href="/studentHelp.jsp" target="_blank" rel="noopener noreferrer">HELP</a>
      </li>
    </ul>
    
      <ul id="NavTop-right" class="pull-right">
        <li>
          <c:if test="${not empty data.account && not empty data.account.googleId}">
          <span id="LoggedInAs">
            Logged in as 
            <span class="text-info" data-toggle="tooltip" title="${data.account.googleId}" data-placement="bottom">
              ${data.account.truncatedGoogleId}
            </span>
          </span>
          </c:if>
          <a id="btnLogout" class="nav logout btn btn-xs btn-danger" href="<%= Const.ActionURIs.LOGOUT %>">Logout</a>
        </li>
      </ul>
    
  </div>
</div>
<div class="navbar navbar-inverse" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#contentLinks">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <t:teammatesLogo/>
    </div>
    <div class="collapse navbar-collapse" id="contentLinks">
      <ul class="nav navbar-nav">
        <li<c:if test="${fn:contains(data.getClass(), 'StudentHome')}"> class="active"</c:if>>
          <a class="navLinks" id="studentHomeNavLink" href="${data.studentHomeLink}"
              <c:if test="${isUnregistered}">data-unreg="true"</c:if>>
            Home
          </a>
        </li>
        <li<c:if test="${fn:contains(data.getClass(), 'StudentProfilePage')}"> class="active"</c:if>>
          <a class="navLinks" id="studentProfileNavLink" href="${data.studentProfileLink}"
              <c:if test="${isUnregistered}">data-unreg="true"</c:if>>
            Profile
          </a>
        </li>
      </ul>
    </div>
  </div>
</div>
