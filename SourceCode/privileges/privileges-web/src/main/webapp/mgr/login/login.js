var positionData=null;
var loginObj={
	/** 页面元素的展示控制 */
	hideExpand:function(){
		$(".tag").height(275);
	},
	/** 登录 */
	login:function(){
		var loginName=$("#userid").val();
		var loginPwd=$("#password").val();
		var sms=$('#sms').val();
		
	    if(loginName!=''&&loginPwd!=''&&loginArea==null&&positionSelect==null){//获取岗位
			if(accTypeMark=='1')
			{
				if(sms=='')
				{
					Showbo.Msg.alert("请输入短信码！",'warn');
				}
				else
				{
				   this.getPosition(loginName,loginPwd);
				}
			}
			else
			{
			   this.goToMain(loginName,loginPwd);
			}
		}
		else if(loginName!=''&&loginPwd!=''&&loginArea!=null&&positionSelect!=null){
			this.goToMain(loginName,loginPwd);
		}else{
			Showbo.Msg.alert("输入的信息不完整！",'error');
		}
	},
	/** 登录，转主页 */
	goToMain:function(loginName,loginPwd){
		$('#loginForm').attr('action', contextPath + 'sysmgr/login.action');

		// 跳转页面
		$('#loginForm').attr('target', '_self');
		$('#loginForm').submit();
		// 设置参数
		$('#userid, #password').val('');
		$('#loginBnt').attr('disabled', 'disabled');
		$('#cancelBnt').hide();
	},
	
	/** 取消 */
	cancel:function(){
		$("#userid").val("");
		$("#password").val("");
		$("#userid").focus();
		$('#userid').prop('disabled',false);
		$('#password').prop('disabled',false);
	}
};

$(document).ready(function(e) {
	/** 获取验证码 */
	getsmsinfo();
	
	/** 页面元素的展示控制 */
	loginObj.hideExpand();
	
	/** 绑定取消登录事件 */
	$("#cancelBnt").click(function(){
		loginObj.cancel();
	});
	
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
 * */
function getsmsinfo()
{
	$("#checkCode").attr(
			"src",
			contextPath + "system/login/getIdentifyCode?nocache="
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

