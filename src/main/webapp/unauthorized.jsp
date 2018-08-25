<%@ page trimDirectiveWhitespaces="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<% response.setStatus(401);%>
<t:errorPage>
  <br><br>
  <div class="row">
    <div id="alertRMIT" class="alert alert-danger" role="alert">
	  <strong>Uh Oh!</strong> Invalid RMIT Email. Please try again.
	</div>
    
    <div class="alert alert-warning col-md-4 col-md-offset-4">
      <img src="/images/angry.png" style="float: left; height: 90px; margin: 0 10px 10px 0;">
      <p>
        You are not authorized to view this page. <br> <br>
        <a href="/logout">Logout and return to main page.</a>
      </p>
      <br>
    </div>
  </div>
  
  <script>
	window.onload = function isStudent(){
		var alert = document.getElementById("alertRMIT");
		
		if(window.location.href.indexOf("studentHomePage") > -1) {
		    alert.style.display = "block";
		}else{
			alert.style.display = "none";
		}
	}
  </script>
</t:errorPage>
