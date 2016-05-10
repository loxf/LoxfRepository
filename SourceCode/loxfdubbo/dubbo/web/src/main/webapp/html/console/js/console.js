$(document).ready(function() {
	getServicesInfo();
	$("#menu li").bind('click', getInfo);
	$("body").bind('click', hidePanel);
});

function getInfo(){
	var id = $(this).prop("id");
	if(id=="serviceMgr"){
		getServicesInfo();
		$("#serviceMgrDiv").show().siblings().hide();
	} else if(id=="serverMgr"){
		getServersInfo();
		$("#serverMgrDiv").show().siblings().hide();
	} else if (id=="example"){
		$("#exampleDiv").show().siblings().hide();
	}
}
/**
 * 获取服务信息
 */
var services ;
function getServicesInfo() {
	var url = contextPath + "/console?method=service";
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		success : function(data) {
			var html = "";
			if(data!=null&&data.length>0){
				for(var i=0; i<data.length; i++){
					var row = data[i];
					html += "<tr>";
					html += "<td>" + row.interfaces + "</td>";
					html += "<td>" + (row.serviceName==null||row.serviceName==""?"无":row.serviceName) + "</td>";
					html += "<td>" + row.implClazz + "</td>";
					html += "<td>" + row.status + "</td>";
					html += "<td><a href='javascript:void(0)' onclick=\"viewMethod(event, " + i + ")\">查看</a></td>";
					html += "<td><a href='javascript:void(0)' onclick=\"viewProduct(event, " + i + ")\">查看</a></td>";
					html += "<td><a href='javascript:void(0)' onclick=\"viewCustomer(event, " + i + ")\">查看</a></td>";
					html += "<td>" + (row.timeout==0?"未设置":row.timeout) + "</td>";
					html += "<td>" + (row.pollingType==null||row.pollingType==""?"默认：RANDOM":row.pollingType) + "</td>";
					html += "<td>" + row.asyn + "</td>";
					html += "</tr>";
				}
				services = data;
			}
			$("#serviceInfo").html(html);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest);
		}
	});
}

function viewMethod(e, i){
	showPanelPos(e.clientX, e.clientY);
	var row = services[i];
	var html = "<table><tr><th>方法</th><th>是否异步</th><th>超时</th><th>轮询</th></tr>";
	var methods = row.method;
	for(var i in methods){
		var m = methods[i];
		var methodName = m.methodName;
		var params = m.params;
		if(params!=null&&params.length>0){
			methodName += "(";
			for(var j in params){
				if(j!=0){
					methodName += ", ";
				}
				methodName += params[j];
			}
			methodName += ")";
		} else {
			methodName += "()";
		}
		html += "<tr>"
			+ "<td>" + methodName + "</td>"
			+ "<td>" + m.asyn + "</td>"
			+ "<td>" + m.timeout + "</td>"
			+ "<td>" + m.pollingType + "</td>"
			+ "</tr>";
	}
	html += "</table>";
	$("#panelContent").html(html);
}

function viewProduct(e, i){
	showPanelPos(e.clientX, e.clientY);
	var row = services[i];
	var html = "<table><tr><th>地址</th><th>状态</th><th>超时</th></tr>";
	var servers = row.servers;
	for(var i in servers){
		var s = servers[i];
		var addr = s.serverAddr + ":" + s.serverPort + (s.serverName==null||s.serverName==""?"":"[" + s.serverName + "]");
		html += "<tr>"
			+ "<td>" + addr + "</td>"
			+ "<td>" + s.status + "</td>"
			+ "<td>" + s.timeout + "</td>"
			+ "</tr>";
	}
	$("#panelContent").html(html);
}

function viewCustomer(e, i){
	showPanelPos(e.clientX, e.clientY);
	var row = services[i];
	var html = "<table><tr><th>地址</th><th>异步</th><th>超时</th><th>轮询</th></tr>";
	var clients = row.clients;
	for(var i in clients){
		var c = clients[i];
		var addr = c.clientAddr + ":" + c.clientPort + (c.clientName==null||c.clientName==""?"":"[" + c.clientName + "]");
		html += "<tr>"
			+ "<td>" + addr + "</td>"
			+ "<td>" + c.asyn + "</td>"
			+ "<td>" + c.timeout + "</td>"
			+ "<td>" + c.pollingType + "</td>"
			+ "</tr>";
	}
	$("#panelContent").html(html);
}

function showPanelPos(x, y){
	$("body").unbind("click");
	$(".poptip").css("top", y+15);
	$(".poptip").css("left", x-10);
	setTimeout(function (){
		$(".poptip").show();
	}, 100);
	$("body").bind('click', hidePanel);
}

function hidePanel(event){
	$(".poptip").hide();
}
/**
 * 获取服务器信息
 */
var servers ;
function getServersInfo() {
	var url = contextPath + "/console?method=server";
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		success : function(data) {

			var html = "";
			if(data!=null&&data.length>0){
				for(var i=0; i<data.length; i++){
					var row = data[i];
					html += "<tr>";
					html += "<td>" + (row.type=="SERV"?"生产者":"消费者") + "</td>";
					html += "<td>" + row.ip + ":" + row.port + "</td>";
					html += "<td>" + row.appName + "</td>";
					html += "<td>" + (row.timeout==0?"未设置":row.timeout) + "</td>";
					html += "</tr>";
				}
				servers = data;
			}
			$("#serverInfo").html(html);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest);
		}
	});
}