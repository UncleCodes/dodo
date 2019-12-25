$().ready( function() {
	if($.dodo==undefined){
		$.extend({dodo:{}});
	}
	$.extend($.dodo, {
		fileUploadCallBack:function(success,id,path,mimeType,fileTempServer,picWidthCheck,
    			picHeightCheck,
    			picWidthErrorMsg,
    			picHeightErrorMsg,
    			picSizeErrorMsg){
			if(success=='true' && mimeType.indexOf('image')!=-1 && fileTempServer!=undefined){
				$("#"+id).append($('<div><img id="'+id+'ShowFile" src="'+fileTempServer+"/"+path+'" /></div>'));
				$.dodo.resize_dodo_file_pic_judge($("#"+id+"ShowFile").eq(0),240,id,picWidthCheck,
		    			picHeightCheck,
		    			picWidthErrorMsg,
		    			picHeightErrorMsg,
		    			picSizeErrorMsg);
        	}
		},
		resize_dodo_file_pic_judge:function(picObj,picWidth,fileId,picWidthCheck,
    			picHeightCheck,
    			picWidthErrorMsg,
    			picHeightErrorMsg,
    			picSizeErrorMsg){
			var img = new Image();
			img.src = picObj.attr("src");
			if(img.complete){ 
				picObj.width(img.width);
				picObj.height(img.height);
				$.dodo.resize_dodo_file_pic(picObj,picWidth,fileId,picWidthCheck,
		    			picHeightCheck,
		    			picWidthErrorMsg,
		    			picHeightErrorMsg,
		    			picSizeErrorMsg);
			}else{
				picObj.load(function(){
					$.dodo.resize_dodo_file_pic(picObj,picWidth,fileId,picWidthCheck,
			    			picHeightCheck,
			    			picWidthErrorMsg,
			    			picHeightErrorMsg,
			    			picSizeErrorMsg);
				});
			}
		},
		resize_dodo_file_pic:function(picObj,picWidth,fileId,picWidthCheck,
    			picHeightCheck,
    			picWidthErrorMsg,
    			picHeightErrorMsg,
    			picSizeErrorMsg){
			var	width = picObj.width();
			var	height = picObj.height();
			var	realWidth = width;
			var	realHeight = height;			
			if(fileId){
				fileId=fileId.replace('fileItem-', '');
				$("#"+fileId+"-width").val(width);
				$("#"+fileId+"-height").val(height);
				$("#"+fileId+"-height").after("("+width+"px * "+height+"px)");
			}
			if(width>=height&&width>picWidth) {
				height = height*picWidth/width;
				width = picWidth;
			} else if(height>=width&&height>picWidth) {
				width = width*picWidth/height;
				height = picWidth;
			}
			picObj.width(width);
			picObj.height(height);
			picWidthCheck = picWidthCheck || -1;
			picHeightCheck = picHeightCheck || -1;	
			if(picWidthCheck>-1&&picHeightCheck>-1&&(realWidth!=picWidthCheck||realHeight!=picHeightCheck)){
				$._exception_alert_info_(picSizeErrorMsg,function(){$("#fileItem-"+fileId+" a.removeFile").click();});
				return;
			}
			if(picWidthCheck>-1&&realWidth!=picWidthCheck){
				$._exception_alert_info_(picWidthErrorMsg,function(){$("#fileItem-"+fileId+" a.removeFile").click();});
				return;
			}
			if(picHeightCheck>-1&&realHeight!=picHeightCheck){
				$._exception_alert_info_(picHeightErrorMsg,function(){$("#fileItem-"+fileId+" a.removeFile").click();});
				return;
			}
		}
	});
//	$(".dodo_file_pic").each(function(){
//		$.dodo.resize_dodo_file_pic_judge($(this),780);
//	});	
	$._exception_alert_info_=function(msg,callback){
		callback = callback ||function(){};
		try{
			$.dialog({content:msg,icon:"error",lock:true,close:callback,ok:callback,time:15});
		}catch(ex){
			try{
				$.messager.alert('', msg, 'error',callback);
			}catch(exx){
				alert(msg);
				callback();
			}
		}
	};
	
	$._exception_continue_function=function(func,data){
		var isExec = false;
		return function(){
			if(isExec){
				return;
			}
			isExec = true;
			try{
				func(data);
			}catch(exx){
				$._exception_alert_info_("Callback error:"+exx.message);
			}
		}
	};
	
	$.ajaxPrefilter(function(options) {
		if (options.success && typeof options.success === "function") {
			options.oldSuccess = options.success;
			options.success = function (data, textStatus, xhr) {
				if(typeof data=='string'&&data!=null&&data.indexOf("_exception_status_")!=-1){
					$._exception_alert_info_($.parseJSON(data)._exception_tip_,$._exception_continue_function(options.oldSuccess,data));
					return;
				}
				
				if(typeof data=='object'&&data!=null&&data._exception_tip_!=undefined){
					$._exception_alert_info_(data._exception_tip_,$._exception_continue_function(options.oldSuccess,data));
					return;
				}
				$._exception_continue_function(options.oldSuccess,data)();
			}
	     }
	 });		
});
