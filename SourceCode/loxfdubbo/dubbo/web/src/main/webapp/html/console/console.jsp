<!DOCTYPE>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<html>
<head>
<%@include file="../common/common.jsp" %>
<link rel="stylesheet" type="text/css"  href="<%=contextPath %>/html/console/css/panel.css"></link>
</head>
<body>
	<div>
		<div id="top" style="width:100%;height:50px"><h1>DUBBO管理界面</h1></div>
		<div id="content" style="width:100%;height:500px">
			<div style="display:inline;float:left;width:20%;">
				<ul id="menu">
					<li id="serviceMgr">服务管理</li>
					<li id="serverMgr">服务器管理</li>
					<li id="example">示例</li>
				</ul>
			</div>
			<div style="display:inline;float:left;width:75%;">
				<div id="serviceMgrDiv" style="width:100%;margin-left:5px;margin-right:20px">
					<table style="width:100%;margin-left:5px;margin-right:20px">
						<tr>
							<th>服务接口</th>
							<th>服务分组</th>
							<th>服务实现</th>
							<th>状态</th>
							<th>方法列表</th>
							<th>生产者</th>
							<th>消费者</th>
							<th>超时时间</th>
							<th>负载类型</th>
							<th>异步调用</th>
						</tr>
						<tbody id="serviceInfo">
						</tbody>
					</table>
				</div>
				<div id="serverMgrDiv" style="width:100%;margin-left:5px;margin-right:20px;display:none">
					<table style="width:100%;margin-left:5px;margin-right:20px">
						<tr>
							<th>服务器类型</th>
							<th>服务器地址</th>
							<th>服务器名称</th>
							<th>超时时间</th>
						</tr>
						<tbody id="serverInfo">
						</tbody>
					</table>
				</div>
				<div id="exampleDiv" style="width:100%;margin-left:5px;margin-right:20px;display:none">
					<h1>示例1</h1>
				</div>
			</div>
		</div>
		<div id="bottom" style="text-align:center;width:100%;height:30px">
			Product <a href="https://github.com/loxf/LoxfRepository">@loxf</a>
		</div>
	</div>
	<div class="poptip" style="display:none">
	    <span class="poptip-arrow poptip-arrow-top"><em>◆</em><i>◆</i></span>
<!-- 	    <span class="poptip-arrow poptip-arrow-right"><em>◆</em><i>◆</i></span> -->
<!-- 	    <span class="poptip-arrow poptip-arrow-bottom"><em>◆</em><i>◆</i></span> -->
<!-- 	    <span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span> -->
	    <div id="panelContent"></div>
	</div>
	<script type="text/javascript" src="<%=contextPath %>/html/console/js/console.js"></script>
</body>
</html>