if(!window.XMLHttpRequest) {
	document.execCommand("BackgroundImageCache", false, true);
}

function changeThemeFun(themeName) {
    var $tplTwoTheme = $('#tpltwoColor');
    var url = $tplTwoTheme.attr('href');
    var href = url.substring(0, url.indexOf('colors')) + 'colors/' + themeName + '.css';
    $tplTwoTheme.attr('href', href);

    var $iframe = $('iframe');
    if ($iframe.length > 0) {
        for ( var i = 0; i < $iframe.length; i++) {
            var ifr = $iframe[i];
            $(ifr).contents().find('#tpltwoColor').attr('href', href);
        }
    }

    $.cookie('tpltwoColor', themeName, {
        expires : 7,path: '/'
    });
}

function autoChangeTheme(){
	var themeSrc = $('#tpltwoColor').attr('href');
	themeSrc = themeSrc.substring(themeSrc.indexOf('colors')+7);
	themeSrc = themeSrc.substring(0,themeSrc.indexOf("."));		
	var $CookieThemeName = $.cookie('tpltwoColor',{path: '/'});
	if ($CookieThemeName) {
		if(themeSrc!=$CookieThemeName){
			changeThemeFun($CookieThemeName);
		}
	}else{
		$.cookie('tpltwoColor', themeSrc, {
	        expires : 7,path: '/'
	    });
	}		
}

$().ready( function() {
	window.dodo_global_infos = window.dodo_global_infos||{};
	/* ---------- List ---------- */
	var $listForm = $("#listForm");// 列表表单
	if ($listForm.size() > 0) {
		var $searchButton = $("#searchButton");// 查找按钮
		var $allCheck = $("#listTable input.allCheck");// 全选复选框
		var $listTableTr = $("#listTable tr:gt(0)");
		var $idsCheck = $("#listTable input[name='ids']");// ID复选框
		var $deleteButton = $("#deleteButton");// 删除按钮
		var $pageNumber = $("#pageNumber");// 当前页码
		var $pageSize = $("#pageSize");// 每页显示数
		var $sort = $("#listTable .sort");// 排序
		var $orderBy = $("#orderBy");// 排序字段
		var $order = $("#order");// 排序方式
		
		// 全选
		$allCheck.click( function() {
			var $this = $(this);
			if ($this.attr("checked")) {
				$idsCheck.attr("checked", true);
				$deleteButton.attr("disabled", false);
				$listTableTr.addClass("checked");
			} else {
				$idsCheck.attr("checked", false);
				$deleteButton.attr("disabled", true);
				$listTableTr.removeClass("checked");
			}
		});
		
		// 无复选框被选中时,删除按钮不可用
		$idsCheck.click( function() {
			var $this = $(this);
			if ($this.attr("checked")) {
				$this.parent().parent().addClass("checked");
				$deleteButton.attr("disabled", false);
			} else {
				$this.parent().parent().removeClass("checked");
				var $idsChecked = $("#listTable input[name='ids']:checked");
				if ($idsChecked.size() > 0) {
					$deleteButton.attr("disabled", false);
				} else {
					$deleteButton.attr("disabled", true)
				}
			}
		});
	    
		// 批量删除
		$deleteButton.click( function() {
			var url = $(this).attr("url");
			var $idsCheckedCheck = $("#listTable input[name='ids']:checked");
			$.dialog({content:window.dodo_global_infos.deleteSureInfo,icon:"question",cancel:function(){},ok:batchDelete,lock:true,background: '#757575',opacity: 0.9});
			function batchDelete() {
				$.ajax({
					url: url,
					data: $idsCheckedCheck.serialize(),
					type: "POST",
					dataType: "json",
					cache: false,
					success: function(data) {
						if (data.status == "success") {
							$idsCheckedCheck.parent().parent().remove();
						}
						$deleteButton.attr("disabled", true);
						$allCheck.attr("checked", false);
						$idsCheckedCheck.attr("checked", false);
						$.dialog({content:data.message,icon:data.status!="success"?"error":"succeed",ok:function(){},time:15});
					}
				});
			}
		});
	
		// 查找
		$searchButton.click( function() {
			$pageNumber.val("1");
			$("#inputIsVague").val("1");
			$listForm.submit();
		});
	
		// 每页显示数
		$pageSize.change( function() {
			$pageNumber.val("1");
			$listForm.submit();
		});
	
		// 排序
		$sort.click( function() {
			var $currentOrderBy = $(this).attr("name");
			if ($orderBy.val() == $currentOrderBy) {
				if ($order.val() == "") {
					$order.val("asc")
				} else if ($order.val() == "desc") {
					$order.val("asc");
				} else if ($order.val() == "asc") {
					$order.val("desc");
				}
			} else {
				$orderBy.val($currentOrderBy);
				$order.val("asc");
			}
			$pageNumber.val("1");
			$listForm.submit();
		});
	
		// 排序图标效果
		if ($orderBy.val() != "") {
			$sort = $("#listTable .sort[name='" + $orderBy.val() + "']");
			if ($order.val() == "asc") {
				$sort.removeClass("desc").addClass("asc");
			} else {
				$sort.removeClass("asc").addClass("desc");
			}
		}
		
		// 页码跳转
		$.gotoPage = function(id) {
			$pageNumber.val(id);
			$listForm.submit();
		}
	}
	
    $("#quick ul ").css({ display: "none" });
    $("#quick li").hover(function () {
	$(this).find('ul:first').css({visibility: "visible",display: "none"}).show(400);
    }, function () {
        $(this).find('ul:first').css({ visibility: "hidden" });
    });
    
    $("#colors-switcher > a").click(function () {
    	changeThemeFun($(this).attr("title").toLowerCase());
        return false;
    });

    $("#menu h6 a").click(function () {
        var link = $(this);
        var value = link.attr("href");
        var id = value.substring(value.indexOf('#') + 1);

        var heading = $("#h-menu-" + id);
        var list = $("#menu-" + id);

        if (list.attr("class") == "closed") {
            heading.attr("class", "selected");
            list.attr("class", "opened");
        } else {
            heading.attr("class", "");
            list.attr("class", "closed");
        }
    });

    $("#menu li[class~=collapsible]").click(function () {
        var element = $(this);

        element.children("a:first-child").each(function () {
            var child = $(this);

            if (child.attr("class") == "plus") {
                child.attr("class", "minus");
            } else {
                child.attr("class", "plus");
            }
        });

        element.children("ul").each(function () {
            var child = $(this);

            if (child.attr("class") == "collapsed") {
                child.attr("class", "expanded");
            } else {
                child.attr("class", "collapsed");
            }
        });
    });
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
			picObj.parent("#dodo_show_thumbnail_div").width(width).height(height);
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
	$(".dodo_show_thumbnail").each(function(){
		$(this).hover(
				function(){
					var dodo_show_thumbnail_img = $("<img src='"+ $(this).attr("href") +"' style='border:1px solid #ccc;'/>");
					var imgDiv=$("#dodo_show_thumbnail_div").append(dodo_show_thumbnail_img).show().css({width:"auto",height:"auto"});
					$.dodo.resize_dodo_file_pic_judge(imgDiv.children("img").eq(0),380);
					var offset = $(this).offset();
					imgDiv.css("left",offset.left+$(this).outerWidth()+3);	
					var buttom = $(window).height()+$(document).scrollTop()-380-offset.top;
					if(buttom<0) {
						imgDiv.css("top",offset.top+buttom-3);
					} else {
						imgDiv.css("top",offset.top-3);
					}
				},
				function(){$("#dodo_show_thumbnail_div").empty().hide();}
		);
	});
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