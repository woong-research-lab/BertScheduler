<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="logincheck.jsp"%>
<div class="appHeader">
	<a href="devices.jsp" style="color:#FFFFFF;" class="logoTitle">BizFrame Monitoring</a>
	<div class="appmenu" style="display: inline-block; float: right; margin-right: 20px;">
		<a href="dashboard.jsp">Dashboard</a>
		<a href="query.jsp">Analysis</a>
		<%
			if("admin".equals(session_userid)) {

		%>
			<a href="users.jsp">User</a>
		<% } %>
		<a href="javascript:logout()"><i class="fas fa-sign-out-alt"></i></a>
	</div>
</div>