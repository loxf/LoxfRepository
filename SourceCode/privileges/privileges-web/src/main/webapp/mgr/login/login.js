var loginObj={
	/** 页面元素的展示控制 */
	hideExpand:function(){
		// $(".tag").height(275);
	},
	/** 登录 */
	login:function(){
		var param = $("#loginForm").serialize();
		var url = contextPath + "system/login/login";
		$.ajax({
			type : 'POST',
			url : url,
			data : param,
			dataType : "json",
			success: function(data)
			{
				if(data.resCode=="1000"){
					var obj = dealResultData(data);
				} else {
					alert(data.resMsg);
					getsmsinfo();
				}
			},
	        error: function(){
	        	alert('内部错误！');
				getsmsinfo();
	        }
		});

	}
	
};

$(document).ready(function(e) {
	/** 获取验证码 */
	getsmsinfo();
	
	loginObj.hideExpand();
	
	/** 绑定登录按钮回车登录事件 */
	$("#loginBnt").click(function(){
		loginObj.login();
	});
	
	/** 绑定用户名框回车事件 */
	$('#userid').bind('keypress',function(event){
         if(event.keyCode == "13")    
         {
             $('#password').focus();
         }
     });

	/** 绑定密码框、验证框、登录按钮回车登录事件 */
	$('#password,#sms,#loginBnt').bind('keypress',function(event){
         if(event.keyCode == "13")    
         {
             loginObj.login();
         }
     });
	
	$('.title li').click(function()
	{
		$(this).addClass('li_on').siblings().removeClass();
		$('.tab').hide();
		$('.tab').eq($(this).index()).show();
	});
});

/**
 * 获取校验码
 */
function getsmsinfo()
{
	$("#checkCode").attr("src",
			contextPath + "/system/login/getIdentifyCode?nocache="
					+ new Date().getTime());  
}

/** 检测IE8以下提示弹窗 */
function popwin(){
	$(".mask").css("display","block");
	$(".popwin").css("display","block");
	$(".future,.btn-close").click(function(){
		$(".mask").css("display","none");
		$(".popwin").css("display","none");
	});
}

/**
 * @param data
 * @returns
 */
function dealResultData(data){
	if(data.view!=null){
		$.ajax({
			type : 'POST',
			url : contextPath + data.view.url,
			data : data.obj,
			success: function(data)
			{
				//console.log("登录成功，页面跳转成功！");
			}
		});
		return 1;
	} else {
		if(data.obj!=null){
			return data.obj;
		}else{
			return null;
		}
	}
}
