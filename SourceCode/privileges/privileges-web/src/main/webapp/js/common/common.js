/**
 * 公共方法的JS
 */

/**
* 把form里面的输入控件（如，input, select, textarea等）序列化为以下格式：<br>
* name=value&name=value&name=value&...
* @param formId 要序列化的表单的id
* @return 序列化的结果，主要用于把表单的信息提交到后台程序里
* @author huang weijian 
*/
function serializeForm(formId) {
	var result = '';
	var $fields = $('#' + formId).serializeArray();
	var len = $fields.size();
	$.each($fields, function(i, field){
  		result += field.name + "=" + field.value;
  		if (i != len - 1) {
  			result += '&';
  		}
	});
	return result;
}

/**
*把日期性按yyyy-MM-dd hh:mm:ss.zzz格式转换为字符串
*/
function formateDate(dt){
 	return dt.getFullYear() + "-" + (dt.getMonth()+1) + "-" + dt.getDate() + 
 		   " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds() + "."+ dt.getMilliseconds();
}

/**
*显示详细信息对话框
*/
function showDetailWindow (url, caption){
		var height =window.screen.height*0.7;
		var width =window.screen.width*0.7;
		var top = (window.screen.height-height)/2;
		var left = (window.screen.width-width)/2;
		var detailWin = window.open(url, caption,
			"toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes,resizable=yes,width="+width+
		 	",height="+height +",top=" + top + ",left="+left);	   
		
	 	detailWin.focus();
	 	
	   return;
	}

	
/**
* 打开小窗口
* url http连接
* 宽度，h高度，n小窗口名称
*/
function openWindow(url,w,h,n) {
    var top = (screen.availHeight-h)/2;
	var left = (screen.availWidth-w)/2;
	var options = "width=" + w + ",height=" + h + ",";
    options += "status=yes,scrollbars=yes,resizable=yes,location=no,alwaysRaised=yes,menubar=no,toolbar=no,directories=no,top="+top+",left="+left;
    if(n == "") {
    	var date=new Date();
    	n=date.getSeconds();
    }
    n=n+"ab";
    var newWin=window.open(url,n, options);
    newWin.focus();
    return newWin;
}

function openFullWindow(url,n) {
	var top = 0;
	var left = 0;
	var w = (window.screen.availWidth-10);
	var h = (window.screen.availHeight-50);
	var options = "width=" + w + ",height=" + h + ",";
    options += "status=yes,scrollbars=yes,resizable=yes,location=yes,menubar=no,toolbar=no,directories=no,top="+top+",left="+left;
    if(n == "") {
    	var date=new Date();
    	n=date.getSeconds();
    }
    n=n+"ab";
    var newWin = window.open(url,n, options);
    try {
    	newWin.focus();
    	newWin.moveTo(0, 0);
    } catch (e) {}
    return newWin;
}

/**
 * 打开模式窗口，窗口打开后居中。
 * url: 要打开的窗口链接
 * width: 窗口宽度
 * height: 窗口高度
 */
function openShowModal(url, width, height) {
	var ops = calcShowModalDialogLocation(width,height);
	return window.showModalDialog(url, window, ops);
}

//屏幕居中显示对话框
function calcShowModalDialogLocation(dialogWidth, dialogHeight) {
    var iWidth = dialogWidth;
    var iHeight = dialogHeight;
    var iTop = (window.screen.availHeight - 20 - iHeight) / 2;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    return 'dialogWidth:' + iWidth + 'px;dialogHeight:' + iHeight + 'px;dialogTop: ' + iTop + 'px; dialogLeft: ' + iLeft 
      + 'px;center:yes;scroll:no;status:no;resizable:0;location:no';
} 
/**
 * 根据一个字符串，返回该字符串所代表时间的毫秒
 */
function parseDate(dateStr) {
 	var result = dateStr.replace(/-/g,"/");
 	return Date.parse(result);
}


 /**
 * 根据日子字符串的格式，比较两个日期
 * 字符串的格式例如  "2005-10-10"   "2005-10-10 22:10"    "2003-4-10 10:34:34"
 * 如果第一个字符串小于第二个字符串，返回true
 * 如果相等，返回0
 * 大于返回false
 */
function compareDate(dateStr1, dateStr2) {
 	var minus = parseDate(dateStr1) - parseDate(dateStr2);
    if(minus >0) {
  		return false;
 	} else if(minus <0) {
  		return true;
   	}
   	return true;
}

/**
*解决ajax乱码
*/
function encodeURIComponent(name) {
  return encodeURI(encodeURI(name));  
}

/**
* 若不传separator则默认为@@poson@@
* @return array, null if value is empty
*/
function splitReturnValue(value, separator) {
	separator = separator || '@@poson@@';
	if (value) {
		return value.split(separator);
	}
	return null;
}

/**
* 报表选择对话框的全选操作
*/
function selectAlls(selectCheck,ob)
{
	if(ob)
	{
		if(selectCheck.checked)
		{
			if(ob.length)
			{
				for(var i=0;i<ob.length;i=i+1)
				{
					ob[i].checked=true;
				}
			}
			else
			{
				ob.checked=true;
			}
		}
		else
		{
			if(ob.length)
			{
				for(var i=0;i<ob.length;i++)
				{
					ob[i].checked=false;
				}
			}
			else
			{
				ob.checked=false;
			}
		}
	}
}

/*设置选择框readonly 
 * 参数说明：obj:选择框对象,可以通过document.getElementById('xxxId')获取
		   $('#xxxId')[0]也可以用获取*/
function SetSelectReadOnly(obj) {
	if (obj) {
		obj.onbeforeactivate = function() {
			return false;
		};
		obj.onfocus = function() {
			obj.blur();
		};
		obj.onmouseover = function() {
			obj.setCapture();
		};
		obj.onmouseout = function() {
			obj.releaseCapture();
		};
	}
} 

/*校验是否有非法字符：使用方法
 * <textarea id="orgDesc" name="organBean.orgDesc" cols="71"
	class="edit_textarea" rows="5" onblur="checks(this)">*/
function checkInvalidCharacter(obj) {
	var t = obj.value;
	szMsg = "['\"]";
	alertStr = "";
	for (var i = 1; i < szMsg.length + 1; i++) {
		if (t.indexOf(szMsg.substring(i - 1, i)) > -1) {
			alertStr = "请勿包含非法字符如: ['\"]";
			obj.focus();
			break;
		}
	}
	if (alertStr != "") {
		alert(alertStr);
		return false;
	}
	return true;
}


$(function() {
	$('#closeImg').click(function() {
		$('#catDiv').css('display', 'none');
		$('#closeImg').css('display', 'none');
		$('#openImg').css('display', '');
	});
	$('#openImg').click(function() {
		$('#catDiv').css('display', '');
		$('#closeImg').css('display', '');
		$('#openImg').css('display', 'none');
	});
	$('#button_di span').click(function(e) {
		$('#button_di li').removeClass('selected hover');
		$(this).parent('li').addClass('selected');
	});
	$('#button_di span').mouseover(function(e) {
		$('#button_di li').removeClass('hover ');
		$(this).parent('li').addClass('hover');
	});
	$('#button_di span').mouseout(function(e) {
		$('#button_di li').removeClass('hover ');
		$(this).parent('li').addClass('');
	});
	$('#projectTreeTd span').click(function(e) {
		$('#projectTreeTd li').removeClass('selected');
		$(this).parent('li').addClass('selected');
	});
	$('#projectTreeTd a').click(function(e) {
		$('#projectTreeTd li').removeClass('selected');
		$(this).parent('li').addClass('selected');
	});
});
function getCurrentLinkName() {
	return $('#projectTreeTd li.selected').text();
}

/**当前页面中的frame装载src
* divId      包裹frame的div的id    必填
* frameId    iframe的id			  必填
* url        iframe的src			  必填
* height	 类似: **px
* width		 类似: **%
* scrolling
*  例子：showDiv("editDiv","editFrame",url,"500px","50%","auto");
*/
function showDiv(divId,frameId,url,height,width,scrolling){
	var _height = height||"200px";
	var _width = width||"100%";
	var _scrolling = scrolling||"auto";
	$("#"+frameId).remove();
	$("#"+divId).append("<iframe frameborder='0' hspace='0' src=\""+url+"\" id='"+frameId+"'  width=\""+_width+"\" height=\""+_height+"\ scrolling=\""+_scrolling+"\"</iframe>");
}

/**父页面中的frame装载src
* divId      包裹frame的div的id    必填
* frameId    iframe的id			  必填
* url        iframe的src			  必填
* height	 类似: **px
* width		 类似: **%
* scrolling
*  例子：showParentDiv("editDiv","editFrame",url,"500px","50%","auto");
*/
function showParentDiv(divId,frameId,url,height,width,scrolling){
	var _height = height||"200px";
	var _width = width||"100%";
	var _scrolling = scrolling||"auto";
	parent.$("#"+frameId).remove();
	parent.$("#"+divId).append("<iframe frameborder='0' hspace='0' src=\""+url+"\" id='"+frameId+"'  width=\""+_width+"\" height=\""+_height+"\ scrolling=\""+_scrolling+"\"</iframe>");
}

/**
 * 判断参数是否为空
 * @param {Object} str
 */
function isEmpty(str){
	if(str==null||str==''||str=='undefined'){
		return true;
	}else{
		return false;
	}
}

/**
 * 跳转页面的统一函数
 * @param {Object} url 页面的url
 * @param {Object} type 窗口的类型 
 * 	1:在执行的iframe区域显示新页面;2:打开一个全屏的窗口;3:打开一个指定大小的的窗口;4:打开模式窗口;5:在本窗口打开一个新的链接
 * @param {Object} width 窗口的宽度
 * @param {Object} height 窗口的高度
 */
function goToPage(url,type,width,height){
	if(type==1){//在执行的iframe区域显示新页面
		$(top.document).find("#mainBody").find(">iframe").attr("src",url);
	}else if(type==2){//打开一个全屏的窗口
		openFullWindow(url,"");
	}else if(type==3){//打开一个指定大小的的窗口
		openWindow(url,width,height,"");
	}else if(type==4){//打开模式窗口
		openShowModal(url,width,height);
	}else if(type==5){//在本窗口打开一个新的链接
		self.location.href=url;
	}else{
		alert('未知的类型值:'+type);
	}
}

	/**
	 * 构建提交form表单对象
	 * @param formId		自定义的ID
	 * @param actionUrl		提交到的URL
	 * @param target		是新开窗口还是本页刷新: 空值是本页刷新, '_blank' 是新开窗口
	 */
	 function createForm(formId, actionUrl, target){
	     var frmIdstr = formId;
	     if(formId){
	     	$("body").append("<form id=\""+frmIdstr+"\"></form>");
	     } else {
	     	var suffix = (new Date()).getMilliseconds();
	     	frmIdstr = "_form_"+suffix;
	     	$("body").append("<form id=\""+frmIdstr+"\"></form>");
	     }
	    $("#"+frmIdstr).attr("action",actionUrl);
	    $("#"+frmIdstr).attr("method","post"); 
	    $("#"+frmIdstr).attr("display","none");
	    if(target) {
	    	$("#"+frmIdstr).attr("target", target);
	    }
	 	
	 	return $("#"+frmIdstr);
	 }
	 /**
	 * 复制源form的参数到目标form,目标form没有时自动创建相应的input控件
	 * @param srcForm			源表单
	 * @param targetForm		目标表单
	 * @param paramNames		需要复制的参数名清单 
	 */
	 function copyFormParamValue(srcForm, targetForm, paramNames, excludeIds){
	 	if(!paramNames) 
	 		return ;
	 	if(!paramNames.length)
	 		return ;
	 	if(typeof paramNames === "string"){
	 
	 		try{
	 	 		var paramName = paramNames;
 	 			var objarr = [];
		 		$(srcForm).find("[name='"+paramName+"']").each(function(idx, objx){
		 			var objid = $(objx).attr("id");
		 			
		 			if(!isIncluded(objid, excludeIds)){
			 			objarr.push($(objx).clone());	 
			 		}
		 			
		 		});
		 		
		 		$(targetForm).append(objarr);
	 	 	
	 	 	} catch(e){
	 			//todo nothing
	 		}
	 		
	 	} else if (Array == paramNames.constructor || typeof paramNames =='array'){
			var isArray = true;
			for(idx in paramNames){
				var paramName = paramNames[idx];

				try{
			 		//清除已存在的项重新复制
			 		if($(targetForm).find("[name='"+paramName+"']").get() != "" ){
			 			$(targetForm).find("[name='"+paramName+"']").remove();
			 		}
			 		
			 		var objarr = [];
			 		$(srcForm).find("[name='"+paramName+"']").each(function(idx, objx){
			 			var objid = $(objx).attr("id"); 
			 			 
			 			if(!isIncluded(objid, excludeIds)){
				 			objarr.push($(objx).clone());	 
				 		}
			 			
			 		});
			 		 
			 		$(targetForm).append(objarr);
		 		} catch(e){
		 			//todo nothing
		 			alert("script error: "+ e);
		 		}
			}
		}
	 	
	 	 
	 }

	/**
	* 判断目标是否包含源
	* @param src  源
	* @param tar  目标
	*/
	function isIncluded(src, tar) {
		if(src && src != null && tar){
			if(typeof src === "string" && typeof tar==="string"){
	 			if(tar.indexOf(src) > -1){
	 				return true;
	 			}
	 		} else if(Array == tar.constructor || typeof tar =='array'){
 				if($.inArray(src, tar)>-1){
 					return true ;
 				}
 			} else if (typeof tar == 'object'){
 				for(var k in tar) {
 					if (tar[k] == src) {
 						return true;
 					}
 				}
 			}
 			
 			return false;
 		} else {
 			return false;
 		}
	}

//获取范围内的随机数
function random(min,max){

    return Math.floor(min+Math.random()*(max-min));

}

	/**
	* 设置值串是JSON格式的input控件的json属性
	* @param objId   input控件的ID
	* @attrNames     属性名列表
	* @values        属性值列表
	*/
	function setJsonAttr(objId, attrNames, values){
		var jsonObj = $.utils.stringToJson($("#"+objId).attr("value"));
		var isArray = false;
		if (Array == attrNames.constructor || typeof attrNames =='array'){
			isArray = true;
		}
		if(!isArray){
			//单值  
			$(jsonObj).attr(attrNames, values);
		}else {
			for(var i=0; i<attrNames.length; i++){
				var attrName = attrNames[i];
				var value = values[i];
				$(jsonObj).attr(attrName, value);
			}
		}
		
		$("#"+objId).attr("value", $.utils.jsonToString(jsonObj));
	}


	/**
	* 设置值串是JSON格式的input控件的json属性
	* @param htmlObj   input控件对象
	* @attrNames     属性名列表
	* @values        属性值列表
	*/
	function setJsonObjectAttr(htmlObj, attrNames, values){
		var jsonObj = $.utils.stringToJson($(htmlObj).attr("value"));
		var isArray = false;
		//alert(typeof attrNames);
		if (Array == attrNames.constructor || typeof attrNames =='array'){
			isArray = true;
		}
		
		if(!isArray){
			//单值 
			//alert("1~"+values);
			var attrName = attrNames;
			var value = values;
			
			$(jsonObj).attr(attrName, value);
		}else {
			for(i in attrNames ){
				var attrName = attrNames[i];
				var value = values[i]; 
				$(jsonObj).attr(attrName, value);
			}
		}
		
		$(htmlObj).attr("value", $.utils.jsonToString(jsonObj));
	}

	/**
	*
	* 将界面 上要提交的中文或特殊字符进行编码
	*/
	function encodeToUTF8(val){
		if(!val || val == undefined || val == "" || val == null) {
			return "";
		}
		//js文件中使用encodeURI()方法(必须套两层) , why?
	 	try{
	 		return encodeURI(val);
	 	} catch(e){
	 		return "";
	 	}
	}

	/**
	*
	* 将form表单的值进行编码
	*/
	function encodeFormFields(frm, names){
	 
		if(frm  != undefined){
			if(names && typeof names == 'string' ) {
				var name = names;
				var inputer = $(frm).find("[name='"+name+"']");
				$.each(inputer, function(idx, objx){
				
					$(objx).val(encodeToUTF8($(objx).val()));
					 
				});
			
			} else if(names && typeof names !== 'string' && names.length > 0){
				for(var kx in names){
					var name = names[kx];
					var inputer = $(frm).find("[name='"+name+"']");
					$.each(inputer, function(idx, objx){
					
						$(objx).val(encodeToUTF8($(objx).val()));
						 
					});
				}
			} else {
				 
				if($(frm).get() != "" ){
					var vfrm = $(frm).get(0);
					if(vfrm != null && vfrm != undefined){
						for (var idx in vfrm.elements){
							var objx = vfrm.elements[idx];
							$(objx).val(encodeToUTF8($(objx).val())); 
						}
					}
				}
			}
		}
	}
	/**
	*
	* 去空格加强版，可以去除字符串中间的空格
	* str:被操作的源字符串
	* isGlobal:是否全局
	* replaceStr:用来替换的字符串,默认为""
	*/
	function Trim(str,isGlobal,replaceStr) 
	{ 
  		var result; 
  		result = str.replace(/(^\s+)|(\s+$)/g,""); 
  		if(isEmpty(replaceStr)) replaceStr = "";
  		if(isGlobal.toLowerCase()=="g") {
  			result = result.replace(/\s/g,replaceStr); 
  		}
  		return result; 
	} 
	
	/**
	*
	* 获取字符串的长度,支持数字，字母，中文 
	* 如："1sS#符"的长为6
	* s:被操作的源字符串
	*/
	function len(s) 
	{ 
		var l = 0; 
		var a = s.split(""); 
		for (var i=0;i<a.length;i++) 
		{ 
			if (a[i].charCodeAt(0)<299) 
			{ 
				l++; 
			} 
			else{ l+=2; } 
		} 
		return l; 
	}
	
	//计算字符串长度
   String.prototype.strLen = function() {
     var len = 0;
     for (var i = 0; i < this.length; i++) {
         if (this.charCodeAt(i) > 255 || this.charCodeAt(i) < 0) len += 2; else len ++;
    }
    return len;
  }
  //将字符串拆成字符，并存到数组中
  String.prototype.strToChars = function(){
    var chars = new Array();
    for (var i = 0; i < this.length; i++){
        chars[i] = [this.substr(i, 1), this.isCHS(i)];
    }
    String.prototype.charsArray = chars;
    return chars;
 }
 //判断某个字符是否是汉字
 String.prototype.isCHS = function(i){
    if (this.charCodeAt(i) > 255 || this.charCodeAt(i) < 0) 
        return true;
    else
        return false;
 }
  //截取字符串（从start字节到end字节）
 String.prototype.subCHString = function(start, end){
    var len = 0;
    var str = "";
    this.strToChars();
    for (var i = 0; i < this.length; i++) {
        if(this.charsArray[i][1])
            len += 2;
        else
            len++;
        if (end < len)
            return str;
        else if (start < len)
            str += this.charsArray[i][0];
    }
    return str;
 }
 //截取字符串（从start字节截取length个字节）
 String.prototype.subCHStr = function(start, length){
    return this.subCHString(start, start + length);
 }
	
 /**
  * 从一个给定的数组arr中,随机返回num个不重复项
  */
 function getArrayRandomItems(arr, num) {
     //新建一个数组,将传入的数组复制过来,用于运算,而不要直接操作传入的数组;
     var temp_array = new Array();
     for (var index in arr) {
         temp_array.push(arr[index]);
     }
     //取出的数值项,保存在此数组
     var return_array = new Array();
     for (var i = 0; i<num; i++) {
         //防下标越界
         if (temp_array.length>0) {
             //在数组中产生一个随机索引
             var arrIndex = Math.floor(Math.random()*temp_array.length);
             //将此随机索引的对应的数组元素值复制出来
             return_array[i] = temp_array[arrIndex];
             //然后删掉此索引的数组元素,这时候temp_array变为新的数组
             temp_array.splice(arrIndex, 1);
         } else {
             //数组中数据项取完后,退出循环,比如数组本来只有10项,但要求取出20项.
             break;
         }
     }
     return return_array;
 }
	
 /**
  * 从26个字母中取出N位不重复的随机字母
  */
   function generateRandomLeter(num, letcase) {
	   var letters = new Array('a','b','c','d','e','f','g','h','i','g','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
	   var strArr = getArrayRandomItems(letters,num);
	   var str = strArr.join("");
	   if(letcase) {
		   switch(letcase){
		   	case "upper": return str.toUpperCase();
		   	case "lower": return str.toLowerCase();
		   	default: return str;  
		   }
	   } else {
		   return str;
	   }
   }
   
   /**
    * 获取浏览器信息
    * 获取浏览器名字+版本字符串
    */
   function getBrowserInfo1(){
   		var agent = navigator.userAgent.toLowerCase();
   		var regStr_ie = /msie [\d.]+;/gi ;
		var regStr_ff = /firefox\/[\d.]+/gi
		var regStr_chrome = /chrome\/[\d.]+/gi ;
		var regStr_saf = /safari\/[\d.]+/gi ;
		
		
		//IE
		if(agent.indexOf("msie") > 0)
		{
			return agent.match(regStr_ie) ;
		}

		//firefox
		if(agent.indexOf("firefox") > 0)
		{
			return agent.match(regStr_ff) ;
		}

		//Chrome
		if(agent.indexOf("chrome") > 0)
		{
			return agent.match(regStr_chrome) ;
		}

		//Safari
		if(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0)
		{
			return agent.match(regStr_saf) ;
		}
   } 
   /**
    * 获取浏览器信息,上面方法无法处理IE11的情况
    * 获取浏览器名字+版本字符串以JSON串的格式输出
    */
   function getBrowserInfo(){
   		var u = window.navigator.userAgent.toLocaleLowerCase();
        var msie = /(msie) ([\d.]+)/;
        var chrome = /(chrome)\/([\d.]+)/;
        var firefox = /(firefox)\/([\d.]+)/;
        var safari = /(safari)\/([\d.]+)/;
        var opera = /(opera)\/([\d.]+)/;

		var ie11 = /(trident)\/([\d.]+)/;
        b = u.match(msie)||u.match(chrome)||u.match(firefox)||u.match(safari)||u.match(opera)||u.match(ie11);
        return {NAME: b[1], VERSION: parseInt(b[2])};
   } 
   
   /**
    * 从字符串中捡出数字串 
    * @param numStr
    */
   function parseToInt(numStr) {
	   if(typeof numStr != 'string' && typeof numStr != 'String') 
		   return numStr;
	   var result = $.trim(numStr);
	   if (result == 0 || result =='0') {
		   return 0;
	   }
	   if (result ) {
		   result =  result.replace(/[^0-9]/ig,"");
		   return parseInt(result);
	   } else {
		   return 0;
	   }
	   
   }
   