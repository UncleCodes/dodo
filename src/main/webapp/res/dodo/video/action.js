function close_light(d,status){
	if($("#close_light_bg").size()==0){
		$("body").append($("<DIV class='close_light_bg' id='close_light_bg'></DIV>"));
	}
	if(status == "open"&&$("#close_light_bg").css("display")=="none"){ 
		return;
	}
	
	if(status == "close"&&$("#close_light_bg").css("display")!="none"){ 
		return;
	}
	if($("#close_light_bg").css("display")=="none")
	{
		$(".video").css({zIndex:"1000"});
		document.getElementById("close_light_bg").style.position="absolute";
		document.getElementById("close_light_bg").style.zIndex="999";
		var b=document.documentElement.scrollWidth;
		var c=$(document).height();
		$("#close_light_bg").css({width:b,height:c});
		$("#close_light_bg").show();
		_attachEvent(window,"resize",setposition);
	}
	else{
		$(".video").css({zIndex:"0"});
		$("#close_light_bg").hide();
	}
}
function close_light_play(d){
	if($("#close_light_bg").css("display")=="none")
	{
		$(".video").css({zIndex:"1000"});
		document.getElementById("close_light_bg").style.position="absolute";
		document.getElementById("close_light_bg").style.zIndex="999";
		var b=document.documentElement.scrollWidth;
		var c=$(document).height();
		$("#close_light_bg").css({width:b,height:c});
		$("#close_light_bg").show();
		_attachEvent(window,"resize",setposition);
	}else{
		$(".video").css({zIndex:"10"});
		$("#close_light_bg").hide();
	}
}
function _detachEvent(obj,evt,func){
	if(obj.removeEventListener){
		obj.removeEventListener(evt,func,true);
	}else{
		if(obj.detachEvent){
			obj.detachEvent("on"+evt,func);
		}else{
			eval(obj+".on"+evt+"=old"+func+";");
		}
	}
}
function _attachEvent(obj,evt,func){
	if(obj.addEventListener){
		obj.addEventListener(evt,func,true);
	}else{
		if(obj.attachEvent){
			obj.attachEvent("on"+evt,func);
		}else{
			eval("var old"+func+"="+obj+".on"+evt+";");
			eval(obj+".on"+evt+"="+func+";");
		}
	}
}

function setposition(){
	var a=document.documentElement.scrollWidth;
	var b=$(document).height();
	$("#close_light_bg").css({width:a,height:b});
}
function light_bgch(b,a){
	if(b==0){
		if(a.currentStyle){
			$(a).css({backgroundPositionX:"0",backgroundPositionY:"-176px"});
		}else{
			a.style.backgroundPosition="0 -176px";
		}
		$(a).css({color:"#ff6523"});$(a).css({textDecoration:"underline "});
	}else{
		if(a.currentStyle){
			$(a).css({backgroundPositionX:"-57px",backgroundPositionY:"-176px"});
		}else{
			a.style.backgroundPosition="-57px -176px";
		}
		$(a).css({color:"#656565"});
		$(a).css({textDecoration:"none "});
	}
}
function light_bgch_db(b,a){
	if(b==0){
		if(a.currentStyle){
			$(a).css({backgroundPositionX:"-1px",backgroundPositionY:"-240px"});
		}else{
			a.style.backgroundPosition="-1px -240px";
		}
		$(a).css({color:"#54990b"});
		$(a).css({textDecoration:"underline "});
	}else{
		if(a.currentStyle){
			$(a).css({backgroundPositionX:"-58px",backgroundPositionY:"-240px"});
		}else{
			a.style.backgroundPosition="-58px -240px";
		}
		$(a).css({color:"#656565"});
		$(a).css({textDecoration:"none"});
	}
}
function getLight(pars)
{	
	close_light(this,pars);
}
function globalGetMovie(movieName) {
    if (navigator.appName.indexOf("Microsoft") != -1){
        return window[movieName];
    }
    else { 
    	return document[movieName]; 
    }
}
function getTimes(pars)
{
	try{
		doHandFeeCallback(pars);
	}catch(e){}
}