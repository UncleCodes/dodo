var core = {};
core.copyright = "(c) www.bydodo.com 2009-2013\nAll code protected by international copyright law.\nSee license.txt for terms and conditions.";
core.version = "1.02";
core.ajax={};

core.ajax.loading = function(statusText) {
	var divHtml = "<div id='lodingDiv'>" + statusText + "</div>";
	$("body:first").append(divHtml);
	$.blockUI($("#lodingDiv"));
};

core.ajax.unLoading = function() {
	$("#lodingDiv").remove();
	$.unblockUI($("#lodingDiv"));
};

core.ajax.send = function(packet, process, aysncflag,method) {
		var sendUrl = packet.sendUrl;
		var sendData = packet.data.data;
		var statusText = packet.statusText;

		if (process == null){
			process = doProcess;
		}
		if (aysncflag == null){
			aysncflag = false;
		}
		if (aysncflag == false) {
			core.ajax.loading(statusText);
		}
		if (method==null){
			method = "POST";
		}
		setTimeout(function() {
			$.ajax({
				url : sendUrl,
				data : sendData,
				type : method,
				async : aysncflag,
				dataType : "html",
				success : function(data) {
					process(data);
				},
				error : function(data) {
					if (data.status == "404") {
						try{
							$.dodo.dialog.alert("System Error 404!","false");
						}catch(ex){
							alert("System Error 404!");
						}
					} else if (data.status == "500") {
						try{
							$.dodo.dialog.alert("System Error 500!","false");
						}catch(ex){
							alert("System Error 500!");
						}
					} else {
						try{
							$.dodo.dialog.alert("System Error other!","false");
						}catch(ex){
							alert("System Error other!");
						}
					}
				}
			});
			if (aysncflag == false) {
				core.ajax.unLoading();
			}
		}, 1);
};

core.ajax.JsMap = function() {
	this.clear = function() {
		this.data = {};
	};
	this.data = {};
	this.add = function(name, value) {
		this.data[name] = value;
	};
	this.find = function(name) {
		return this.data[name];
	};
	this.clear();
};

core.ajax.sendPacket = function(sendUrl, statusText) {
	this.sendUrl = sendUrl;
	this.data = new core.ajax.JsMap();
	this.statusText = statusText;
};