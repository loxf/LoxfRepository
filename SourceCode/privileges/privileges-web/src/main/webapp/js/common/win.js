// JavaScript Document

function openBox(_sbox,_cbtn){
	//添加半透明遮罩背景层
	if(0 == $('#mask').length){$('body').append('<div id="mask" style="position:fixed;top:0;left:0;z-index:999;background:#333;width:100%;filter:alpha(opacity=80);opacity:0.8;"></div>');}
	//显示层
	$('#mask,#'+_sbox).show();
	//定位弹出层
	var domHtml = $('html')[0];// domHtml = document.documentElement
	var domBody = $('body')[0];// domBody = document.body
	$('#'+_sbox).css({position : 'absolute',zIndex : '9999'});
	$('#'+_sbox).css('top',Math.floor(domHtml.clientHeight / 2)-Math.floor($('#'+_sbox)[0].offsetHeight / 2)+Math.max(domHtml.scrollTop,domBody.scrollTop)+'px');
	$('#'+_sbox).css('left',Math.floor(domHtml.clientWidth / 2)-Math.floor($('#'+_sbox)[0].offsetWidth / 2)+Math.max(domHtml.scrollLeft,domBody.scrollLeft)+'px');
	//设置半透明遮罩背景层高度
	$('#mask').css('height',Math.max(Math.ceil(domHtml.clientHeight),Math.max(domHtml.scrollHeight,domBody.scrollHeight))+'px');
	//隐藏body滚动条
	$('body').css('overflow','hidden');
	//点击关闭
	$('.'+_cbtn+',#'+_cbtn).click(function(){
		$('#mask,#'+_sbox).hide();
		$('body').css('overflow','visible');
	});
}