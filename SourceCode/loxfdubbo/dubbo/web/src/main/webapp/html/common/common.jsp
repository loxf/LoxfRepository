<%@ page language="java" pageEncoding="GBK"%>
<%
    String sysTitle = "DUBBO服务管理";
	String contextPath = (String)request.getContextPath();
	request.setAttribute("contextPath",contextPath);
%>
<title><%=sysTitle %></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript">
	var contextPath = '<%=contextPath %>';
</script>
<script type="text/javascript" src="<%=contextPath %>/common/1.8.0/jquery-1.8.0.js"></script>
