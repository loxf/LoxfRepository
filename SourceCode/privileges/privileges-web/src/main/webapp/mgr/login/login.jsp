<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<base href="<%=basePath%>">
	<title>LOXY Privilege Manager System</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<link href="<%=basePath%>skin/default/css/login/login.css"
		rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
		var contextPath='<%=basePath%>';
	</script>
	<script type="text/javascript"
		src="<%=basePath%>js/jquery/1.8.0/jquery-1.8.0.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/common/common.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>js/jquery/blockui/jquery.blockUI.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>js/jquery/json/jquery.json.js"></script>
		
</head>

<body style="overflow-y: hidden">
	<!--top-->
	<div class="top">
		<div class="topMain">
			<div class="logo">
				<a href="javascript:void(0)"><img
					src="<%=basePath%>/skin/default/images/login/logo.png"><span>P M S</span>
				</a>
			</div>
			<div class="help">
			    <a href="###"><img src="<%=basePath%>/skin/default/images/login/icon_help.png">帮助中心</a>
			    <a href="###"><img src="<%=basePath%>/skin/default/images/login/icon_online.png">在线客服</a>
			</div>
		</div>
	</div>

	<!--banner-->
	<div class="banner01">
		<div class="b_main">
			<!--登陆-->
			<div class="div_login">
				<ul class="title">
					<li class="li_on">会员登录</li>
					<li>扫二维码</li>
				</ul>
				<!--文本输入-->
				<div class="tab">
					<form action="" method="post" id="loginForm">
						<input type="hidden" id="accTypeMark" value="0" /> 
						<input type="hidden" id="theUuid" />
						<div class="input">
							&nbsp;账户 <input type="text" name="userName" id="userid" />
						</div>
						<div class="input">
							&nbsp;密码 <input type="password" name="pwd" id="password" />
						</div>
						<div class="test expand expand-sms">
							<input name="sms" type="text" id="sms" /> <a id="getsms"
								href="javascript:void(0)" onclick="getsmsinfo()">
								<img id="checkCode" src=""/>
								</a>
						</div>
						<p class="tip" >忘记密码</p>
						<a href="javascript:void(0)" class="btn-login" id="loginBnt">登 录</a>
						<br/>
						<a href="javascript:void(0)" class="btn-regist" id="registBnt">注册</a>
					</form>
				</div>
				<!--二维码-->
				<div class="tab none">
					<span><img
						src="<%=basePath%>/skin/default/images/login/er.jpg">
					</span>
					<p class="sao">手机扫描 下载客户端</p>
				</div>
			</div>
		</div>
	</div>
	
	<div>
	</div>
	<!--foot-->
	<div class="foot login_foot">
		<p>Copyright © Loxf Team</p>
	</div>

<script type="text/javascript"
	src="<%=basePath%>mgr/login/login.js"></script>
<script src="<%=basePath%>js/common/win.js"></script>
		
</body>
</html>
