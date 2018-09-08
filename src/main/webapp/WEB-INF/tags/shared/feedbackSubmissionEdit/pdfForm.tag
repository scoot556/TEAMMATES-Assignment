<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag description="Student PDF Upload Form" pageEncoding="UTF-8" %>
<%@ tag import="teammates.common.util.Const"%>

<div class="form-horizontal">
    <div class="panel panel-primary">
     <div class="panel-heading">
      <span class="text-preserve=space">
       Submit PDF
      </span>
     </div>
     
     <div class="panel-body">
      <p class="text-muted"> Only upload .PDF files
      <br>Other files will not be accepted</p>
      Select PDF to Upload:<input type="file" name="fileName" accept=".pdf">
      <br>
     </div>
    </div>
</div>