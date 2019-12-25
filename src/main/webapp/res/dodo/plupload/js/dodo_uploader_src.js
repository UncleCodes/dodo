;(function ($,w) {
	window.dodo_global_infos = window.dodo_global_infos||{};
	if($.dodo==undefined){
		$.extend({dodo:{}});
	}
	if(dodo_global_infos==undefined){
		dodo_global_infos={}
	}
	/**
	 * public method
	 */
    $.extend($.dodo, {
        isArray:function (arr) {
            return Object.prototype.toString.call(arr) == '[object Array]';
        },
        isString:function (str) {
            return typeof str == 'string';
        },
        isObject:function (obj) {
            return obj && typeof obj == 'object' && !$.dodo.isArray(obj);
        },
        isNumber:function (num) {
            return (num instanceof Number || typeof num == "number");
        },
        io:{}
    });
    /**
     * pluploader config
     */
    if (w.plupload) {
        plupload.guidPrefix = 'dododoc';
    }
    var uploaderTpl = [
        '<div id="${containerId}" class="uploader-box">',
        '<input type="hidden" name="uploader-${propertyName}" value="${baseId}"/>',
        '<div class="clearfix upload-btn">',
        '<a id="${buttonId}" href="#"><b class="icon-add"></b>${chooseFileLabel}</a>',
        '</div>',
        '<ul id="${renderToId}Filelist">',
        '</ul>',
        '</div>'
    ].join('');
    var fileItemTpl = [
        '<li id="${id}" class="fileitem clearfix">',
        '<b class="icon-att"></b>',
        '<span class="filename">${name}</span>',
        '<span class="filesize">(${size})</span>',
        '<span class="pro-bar">',
        '<span class="pro-inner" style="width:0%"></span>',
        '</span>',
        '${fileHiddenPostHtml}',
        '<a href="#" class="removeFile">${removeItemLabel}</a>',
        '${fileAttrLocation}',
        '</li>'
    ].join('');
    
    var extAttrTpl='<b>${attrKeyName}:</b><input type="text" class="attrVal" value="" name="${fileId}-key-${attrKey}" style="width:50%;"/>&nbsp;&nbsp;';
    var hiddenItemTpl = [
        '<input type="hidden" name="${baseId}" value="${fileId}"/>',
        '<input type="hidden" name="${fileId}-name" value="${fileName}"/>',
        '<input type="hidden" name="${fileId}-puresize" value="${pureFileSize}"/>',
        '<input type="hidden" name="${fileId}-formatsize" value="${formatFileSize}"/>',
        '<input type="hidden" id="${fileId}-width" name="${fileId}-width" value="-1"/>',
        '<input type="hidden" id="${fileId}-height" name="${fileId}-height" value="-1"/>'
    ].join('');
    
    var videoItemTpl = [
                         '<br/>',
                         '<span class="sub-title">'+window.dodo_global_infos.adTypeLabel+':</span>',
                         '<input type="checkbox" name="${fileId}-ad" checked="checked" value="preOtherAd"/>'+window.dodo_global_infos.adPreotherLabel,
                         '<input type="checkbox" name="${fileId}-ad" checked="checked" value="preVideoAd"/>'+window.dodo_global_infos.adPrevideoLabel,
                         '<input type="checkbox" name="${fileId}-ad" checked="checked" value="pauseAd"/>'+window.dodo_global_infos.adPauseLabel,
                         '<input type="checkbox" name="${fileId}-ad" checked="checked" value="cornerAd"/>'+window.dodo_global_infos.adCornerLabel,
                         '<input type="checkbox" name="${fileId}-ad" checked="checked" value="afterAd"/>'+window.dodo_global_infos.adAfterLabel,
                         '<br/>',
                         '<span class="sub-title">'+window.dodo_global_infos.playerColorLabel+':</span>',
                         '<input type="radio" name="${fileId}-color" value="black"/>'+window.dodo_global_infos.playerColorBlackLabel,
                         '<input type="radio" name="${fileId}-color" checked="checked" value="red"/>'+window.dodo_global_infos.playerColorRedLabel,
                         '<input type="radio" name="${fileId}-color" value="dark-blue"/>'+window.dodo_global_infos.playerColorDarkblueLabel,
                         '<input type="radio" name="${fileId}-color" value="green"/>'+window.dodo_global_infos.playerColorGreenLabel,
                         '<input type="radio" name="${fileId}-color" value="purple"/>'+window.dodo_global_infos.playerColorPurpleLabel,
                         '<input type="radio" name="${fileId}-color" value="peak-green"/>'+window.dodo_global_infos.playerColorPeakgreenLabel,
                         '<br/>',
                         '<span class="sub-title">'+window.dodo_global_infos.videoHandfeeLabel+':</span>',
                         '<input type="text" name="${fileId}-handfee" value="-1"/>'
                     ].join('');
    
    $.dodo.io.uploader = function (cfg) {
        var params = cfg.params,
            chunkSize = cfg.chunkSize || 2048,
            chooseFileLabel = cfg.chooseFileLabel || "Choose File",
            removeItemLabel = cfg.removeItemLabel || "Remove",
            picWidth = cfg.picWidth || -1,
            picHeight = cfg.picHeight || -1,
            picWidthErrorMsg = cfg.picWidthErrorMsg || "The width of the image is error",
            picHeightErrorMsg = cfg.picHeightErrorMsg || "The height of the image is error",
            picSizeErrorMsg = cfg.picSizeErrorMsg || "The size of the image is error",
            propertyName = cfg.propertyName||"",
            bpCustomerDef = cfg.bpCustomerDef,
            bpCustomerUrl = cfg.bpCustomerUrl,
            singleModel = !!cfg.singleModel,
            isVideo = !!cfg.isVideo,
            isSuppBp = !!cfg.isSuppBp,
        	baseId = cfg.uploaderId || plupload.guid(),
        	callBack = cfg.callBack,
        	extAttr = cfg.extAttr || {},
        	fileTempServer = cfg.fileTempServer,
            boxId = 'uploader-' + baseId,
            complete = true,
            buttonId = 'uploader-browse-button-' + baseId,
            uploaderEl = $("#"+cfg.renderTo).append(
                uploaderTpl.replace('${containerId}', boxId)
                    .replace('${buttonId}', buttonId)
                    .replace('${baseId}', baseId)
                    .replace('${propertyName}', propertyName)
                    .replace('${chooseFileLabel}', chooseFileLabel)
                    .replace('${renderToId}', cfg.renderTo)
            ),
            progressBars = {},
            getProgressBar = function (id) {
                return progressBars[id] || (progressBars[id] = uploaderEl.find('#fileItem-' + id + ' .pro-inner'));
            },
            getProgressBarWrap = function (id) {
                var bar = getProgressBar(id);
                var bp = bar && bar.parent && bar.parent();
                return bp;
            },
            uploader = new plupload.Uploader({
                runtimes:'html5,flash,html4',
                url:cfg.uploadPath,
                max_file_size:cfg.maxSize,
                chunk_size:(chunkSize / 1024) + 'mb',
                unique_names:true,
                multiple_queues:true,
                multipart:true,
                flash_swf_url:cfg.contextPath + '/res/dodo/plupload/js/Moxie.swf',
                silverlight_xap_url:cfg.contextPath + '/res/dodo/plupload/js/Moxie.xap',
                browse_button:buttonId,
                container:boxId,
                drop_element:boxId,
                multipart_params:$.extend(params, {uploaderId:baseId}),
                file_data_name:'upload',
                headers:{
                    chunkSize:(chunkSize/1024)*1024*1024,
                    propertyName:propertyName
                },
                filters:cfg.filters||{},
                propertyName : propertyName,
                bpCustomerDef : bpCustomerDef,
                bpCustomerUrl : bpCustomerUrl
            }),
            fileList = uploaderEl.find('ul');
        fileList.delegate('a.removeFile', 'click', function () {
        	var fileId = $(this).parent().attr("id").replace('fileItem-', '');
            var file = uploader.getFile(fileId);
            if (file) {
                uploader.removeFile(file);
            } else {
                uploader.trigger('FilesRemoved', [
                    {id:fileId}
                ]);
            }
            return false;
        });
        fileList.delegate('input.attrVal', 'blur', function () {
        	var $this = $(this);
        	$this.val($.trim($this.val()));
        	if($this.val()==""){
        		if(!$this.is(".errorAttr")){
        			$this.addClass("errorAttr");
        		}
        	}else{
        		$this.removeClass("errorAttr");
        	}
        	return false;
        });
        uploader.bind('FilesAdded', function (up, files) {
            if (files.length > 0) {
                if (this.isComplete()) {
                    complete = false;
                }
                uploader.addDisplayFile(files);
                doStart(1400);
            }
        });

        var doStart = function (dely) {
            if (uploader.state === plupload.STOPPED) {
                setTimeout(function () {
                    if (uploader.state === plupload.STOPPED) {
                        uploader.start();
                    }
                }, dely);
            }
        };

        var onRemoved = function () {
            if (uploader.total.queued > 0) {
                doStart(140);
            } else {
                uploader.trigger('UploadComplete', uploader, uploader.files);
            }
        };
        var doStop = function () {
            if (uploader.state === plupload.STARTED || uploader.state === plupload.UPLOADING) {
                uploader.stop();
            }
        };

        var getHiddenItemTpl = function (file) {
            return hiddenItemTpl.replace('${baseId}', baseId)
            					.replace(/\$\{fileId\}/g, file.id)
            					.replace('${fileName}', file.name)
            					.replace('${pureFileSize}', file.size)
            					.replace('${formatFileSize}', plupload.formatSize(file.size));
        };
        var getVideoItemTpl = function (file) {
            return videoItemTpl.replace(/\$\{fileId\}/g, file.id);
        };
        uploader.bind('FilesRemoved', function (up, files) {
            if (files.length === uploader.total.queued) {
                doStop();
            }
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                if (progressBars[file.id]) {
                    doStop();
                }
                var fileItem = fileList.find('#fileItem-' + file.id);
                fileItem.animate({
                    height:0,
                    opacity:0
                }, 500, function () {
                    fileItem.remove();
                    delete progressBars[file.id];
                    if (i === files.length) {
                        onRemoved();
                    }
                    if(singleModel){
                    	uploader.disableAddFile(false);
                    }
                });
            }
        });
        
        uploader.bind('UploadProgress', function (up, file) {
            var bar = getProgressBar(file.id);
            bar && bar.css('width', file.percent + '%')&& bar.text(file.percent + '%');
        });
        var err = function (bp, e) {
            bp.html(e.code === plupload.FILE_SIZE_ERROR ? e.message + plupload.formatSize(uploader.settings.max_file_size) : e.message).removeClass('pro-bar').addClass('failure');
        };
        uploader.bind('FileUploaded', function (up, file, res) {
            var bp = getProgressBarWrap(file.id);
            try {
                var fileJson = $.parseJSON(res.response);
                bp.animate({
                    width:0,
                    opacity:0
                }, 500, function () {
                	if(!isSuppBp){
                		$(getHiddenItemTpl(file)).replaceAll(bp);
                	}
                    try{
                    	if(callBack){
                        	callBack(fileJson.success,
                        			'fileItem-' + file.id,
                        			baseId+"/"+file.target_name,
                        			plupload.mimeTypesDodo[file.name.replace(/^.+\.([^.]+)/,"$1").toLowerCase()],
                        			fileTempServer,
                        			picWidth,
                        			picHeight,
                        			picWidthErrorMsg,
                        			picHeightErrorMsg,
                        			picSizeErrorMsg);
                        }
                    }catch(ex){
                    }
                });
                if(singleModel){
                	uploader.disableAddFile(true);
                }
            } catch (e) {
                err(bp);
            }
        });

        uploader.bind('BeforeUpload', function () {
            complete = false;
        });

        uploader.bind('UploadComplete', function () {
            complete = true;
        });

        uploader.bind('Error', function (up, e) {
            var bp = getProgressBarWrap(e.file.id);
            if(bp.size()>0){
            	err(bp, e);
            }else{
            	$._exception_alert_info_(e.file.name+"["+e.message+"]");
            }
        });

        uploader.init();

        uploader.isStop = function () {
            return uploader.state === plupload.STOPPED;
        };

        uploader.isComplete = function () {
            return complete;
        };

        uploader.clear = function () {
            uploader.splice();
            fileList.empty();
        };

        uploader.addDisplayFile = function (files) {
            if ($.dodo.isObject(files)) {
                files = [files];
            }
            if ($.dodo.isArray(files)) {
                var html = [];
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    var tpl = fileItemTpl.replace('${id}', 'fileItem-' + file.id)
                        .replace('${name}', file.name)
                        .replace('${ShowFileid}', file.id)
                        .replace('${removeItemLabel}', removeItemLabel)
                        .replace('${size}', plupload.formatSize(file.size));
                    
                    if(isVideo){
                    	tpl = tpl.replace('<a href="#" class="removeFile">'+removeItemLabel+'</a>', '<a href="#" class="removeFile">'+removeItemLabel+'</a>'+getVideoItemTpl(file));
                    }
                    if (file.canDelete === false) {
                        tpl = tpl.replace('<a href="#" class="removeFile">'+removeItemLabel+'</a>', '');
                    }
                    var extAttrArr = new Array();
                    for(var key in extAttr){
                    	extAttrArr.push('<div>文件属性:<ul style="list-style-type: none;padding-left: 50px;">');    
                    	for(var attrKey in extAttr){
                    		extAttrArr.push("<li>");	
                        	extAttrArr.push(extAttrTpl.replace("${fileId}", file.id).replace("${attrKey}",attrKey).replace("${attrKeyName}",extAttr[attrKey]));
                    		extAttrArr.push("</li>");
                    	}
                    	extAttrArr.push('</ul></div>');
                    	break;
                    }
                    tpl = tpl.replace("${fileAttrLocation}",extAttrArr.join(''));
                    
                    if (!$.dodo.isNumber(file.loaded) || file.loaded === file.size) {
                        tpl = tpl.replace(/<span class="pro-bar">[\s\S]*?<\/span>/, getHiddenItemTpl(file));
                    }else if(isSuppBp){
                    	tpl = tpl.replace("${fileHiddenPostHtml}",getHiddenItemTpl(file));
                    }else{
                    	tpl = tpl.replace("${fileHiddenPostHtml}","");
                    }
                    html[html.length] = tpl;
                }
                html = $(html.join(''));
                html.css('display', 'none');
                fileList.append(html);
                html.fadeIn(500);
            }
        };

        var addFileButton = document.getElementById(buttonId),
            addFileButtonParent = addFileButton.parentNode;

        uploader.disableAddFile = function (bool) {
            var parentNode = addFileButton.parentNode;
            if (bool) {
                if (parentNode) {
                    parentNode.removeChild(addFileButton);
                }
            } else {
                if (parentNode !== addFileButtonParent) {
                    addFileButtonParent.appendChild(addFileButton);
                }
            }
        };

        return uploader;
    };
})(jQuery,window);