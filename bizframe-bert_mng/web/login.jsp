<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="kr">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
<%@ include file="bizframe.jsp" %>
</head>
<style>
.center {
	width: 800px;
	height: 400px;
	position: absolute;
	top: 50%;
	left: 50%;
	margin: -200px 0 0 -400px;
	text-align: center;
}
</style>
<body style="background-color: #44505f;">
<div class="center" style="background-color: #64708B;">
	<div style="float: left;">
		<img src="img/BizFrame_ITRM_bg.png" style="height: 400px; width: 400px;">
	</div>
	<div style="float: left; height: 400px; width: 400px;"><br><br>
		<div class="sign-square" style="margin-left: 12px; margin-top:6px; float: left"></div>
		<div style="padding-left: 15px; font-size: 30px; float: left; color:#fff;">Log in</div>
		<form name="form1" id="form1" method="post">
			<div style="width: 400px; ">
				<!-- Please provide your details -->
				<div class="sign-in-field" style="padding-top: 80px; margin-left: 3px;">
					<span class="fas fa-user icon-silver" style="font-size: 30px; text-align: center; padding: 11px; background-color: white; border:0; float: left;"></span>
					<input type="text" name="loginid" id="loginid" placeholder="Id" style="background-color: white; font-size: 25px; width: 320px; height: 50px; border:0; float:left;" value=""/>
				</div>
				<div class="sign-in-field" style="margin-left: 3px;">
					<span class="fas fa-lock icon-silver" style="background-color: white; font-size: 30px; text-align: center; padding: 11px 13px; border:0; float: left; margin-top: 15px;"></span>
					<input name="loginpw" id="loginpw" type="password" placeholder="Password" style="font-size: 25px; background-color: white; width: 320px; height: 50px; border:0; margin-top: 15px; float: left;" onkeydown="onEnter(event)" value=""/>
				</div>
				<div>
					<a href="javascript:loginCall()" class="content-body-btn-save" style="width: 370px;height:50px;line-height:50px;border-radius:0; margin-top: 40px;font-size: 20px; background-color: #75D0B3;">Login</a>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="js/login.js"></script>
</body>
</html>