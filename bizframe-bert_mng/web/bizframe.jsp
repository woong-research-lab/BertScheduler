<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
String browser[] = { "android", "blackberry", "googlebot-mobile", "iemobile", "iphone", "ipod", "opera mobile", "palmos", "webos" };
String userAgent = request.getHeader("user-agent");
String userAgent1 = userAgent.toLowerCase();

String flag = "";

for (int i = 0; i < browser.length; i++) {
	if (userAgent1.matches(".*" + browser[i] + ".*")) {
		flag = "Y"; // 모바일 브라우저
	}
}

%>
<title>BizFrame Monitoring</title>
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link href="css/responsive-style.css" rel="stylesheet" type="text/css">
<link href="css/responsive-tabs.css" rel="stylesheet" type="text/css">
<link href="css/fa-svg-with-js.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery.responsiveTabs.js"></script>
<script type="text/javascript" src="js/fontawesome-all.js"></script>
<script type="text/javascript" src="js/default.js"></script>
<script type="text/javascript" src="js/login.js"></script>