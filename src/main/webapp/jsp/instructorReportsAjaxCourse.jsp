<%@ page trimDirectiveWhitespaces="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags/instructor/reports" prefix="reports" %>
<reports:coursePanel courseTable="${data.courseTable}" index="${data.index}">
  <reports:courseTable sessionRows="${data.courseTable.rows}" />
</reports:coursePanel>
