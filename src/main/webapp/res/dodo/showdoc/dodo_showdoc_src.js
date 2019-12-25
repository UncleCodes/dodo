;(function ($,w) {
	if($.dodo==undefined){
		$.extend({dodo:{}});
	}
	if($.dodo.io==undefined){
		$.extend($.dodo,{io:{}});
	}
	 $.dodo.io.showDoc = function (cfg) {
		 cfg = cfg || {};
		 cfg.flash = cfg.flash || {};
		 cfg.flash.attributes = $.extend({id : this.swfId}, cfg.flash.attributes);
		 cfg.flash.params = $.extend({
               quality : 'high',
               bgcolor : '#ffffff',
               allowscriptaccess :'sameDomain',
               allowfullscreen : true,
               wmode : 'window'
         }, cfg.flash.params);
		 cfg.flash.vars = $.extend({
             SwfFile : cfg.docUrl,
             Scale : 0.8,
             ZoomTransition : 'easeOut',
             ZoomTime : 0.5,
             ZoomInterval : 0.1,
             FitPageOnLoad : false,
             FitWidthOnLoad : true,
             FullScreenAsMaxWindow : false,
             ProgressiveLoading : true,
             TextSelectEnabled:true,
             ReadOnly:true,
             ViewModeToolsVisible : true,
             ZoomToolsVisible : true,
             FullScreenVisible : true,
             NavToolsVisible : true,
             CursorToolsVisible : true,
             SearchToolsVisible : true,
             LocaleChain : ((w.navigator.userLanguage || w.navigator.language.replace("-", "_")) || '').replace("-", "_")
         }, cfg.flash.vars);
		 if(cfg.SecretKey){
			 cfg.flash.vars.SecretKey = cfg.SecretKey;
		 }
		 swfobject.embedSWF(cfg.contextPath + '/res/dodo/showdoc/dodo_showdoc.swf',
				 cfg.renderTo, '95%', '95%', '10.0.0', cfg.contextPath + '/res/dodo/showdoc/expressInstall.swf',
				 cfg.flash.vars, cfg.flash.params, cfg.flash.attributes, 
				 function(a) {
				 	if(a['success']==false){
			 			alert('加载Flash失败，请检查浏览器设置：Flash = 允许访问');
			 		}
                 }
         );
	 };
})(jQuery,window);