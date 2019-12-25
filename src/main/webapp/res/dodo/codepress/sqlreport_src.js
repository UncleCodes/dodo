//init Language.databaseMetadata.data and sort
;(function ($,w) {
	if($.dodo==undefined){
		$.extend({dodo:{}});
	}
    $.extend($.dodo, {
        sqlreport:{
           databaseMetadata:
           {
               output:'<em>$1</em>',
               syntax:[]
           },
           execSQLURL:'',
           execSQLPageSize:20,
           loddingText:'Loadding...',
           dbMetadataURL:'',
           queryTypesUrl:'',
           queryTypes:{},
           fieldTypesUrl:'',
           fieldTypes:{},
           currentExecSQL:'',
           currentExecSQLFmt:'',
           queryOptions:{
        	   'String':[],
        	   'BigDecimal':[],
        	   'Boolean':[],
        	   'Byte':[],
        	   'Short':[],
        	   'Integer':[],
        	   'Long':[],
        	   'Float':[],
        	   'Double':[],
        	   'Date':[],
        	   'Time':[],
        	   'Timestamp':[],
        	   'ByteArray':[],
        	   'Object':[]
           },
           conditionExecUrl:'',
           isSqlReportValid:false,
           isSqlChanged:false,
           init:function(){
        	   var $this = this;
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.dbMetadataURL,
    				type: "GET",
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					for(var i=0;i<data.length;i++){
		        		   $this.databaseMetadata.syntax.push(new RegExp("\\b("+data[i]+")\\b","gi"));
						}
    				}
        	   });
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.queryTypesUrl,
    				type: "GET",
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					$this.queryTypes=data;
    					for(var key in data){
    						var optionVal = "<option value='"+key+"'>"+data[key]+"</option>";
    						if(key=="eq" || key=="ne" || key=="isNull" || key=="isNotNull"){
    							$this.queryOptions['String'].push(optionVal);
    							$this.queryOptions['BigDecimal'].push(optionVal);
    							$this.queryOptions['Boolean'].push(optionVal);
    							$this.queryOptions['Byte'].push(optionVal);
    							$this.queryOptions['Short'].push(optionVal);
    							$this.queryOptions['Integer'].push(optionVal);
    							$this.queryOptions['Long'].push(optionVal);
    							$this.queryOptions['Float'].push(optionVal);
    							$this.queryOptions['Double'].push(optionVal);
    							$this.queryOptions['Date'].push(optionVal);
    							$this.queryOptions['Time'].push(optionVal);
    							$this.queryOptions['Timestamp'].push(optionVal);
    						}else if(key=="gt" || key=="lt" || key=="ge" || key=="le" || key=="between" || key=="notBetween"){
    							$this.queryOptions['BigDecimal'].push(optionVal);
    							$this.queryOptions['Byte'].push(optionVal);
    							$this.queryOptions['Short'].push(optionVal);
    							$this.queryOptions['Integer'].push(optionVal);
    							$this.queryOptions['Long'].push(optionVal);
    							$this.queryOptions['Float'].push(optionVal);
    							$this.queryOptions['Double'].push(optionVal);
    							$this.queryOptions['Date'].push(optionVal);
    							$this.queryOptions['Time'].push(optionVal);
    							$this.queryOptions['Timestamp'].push(optionVal);
    						}else if(key=="in" || key=="notIn"){
    							$this.queryOptions['String'].push(optionVal);
    							$this.queryOptions['BigDecimal'].push(optionVal);
    							$this.queryOptions['Byte'].push(optionVal);
    							$this.queryOptions['Short'].push(optionVal);
    							$this.queryOptions['Integer'].push(optionVal);
    							$this.queryOptions['Long'].push(optionVal);
    							$this.queryOptions['Float'].push(optionVal);
    							$this.queryOptions['Double'].push(optionVal);
    						}else if(key=="like" || key=="notLike"){
    							$this.queryOptions['String'].push(optionVal);
    						}
    					}
    				}
        	   });     
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.fieldTypesUrl,
    				type: "GET",
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					$this.fieldTypes=data;
    				}
        	   });         	   
           },
           makeHighLightSQL:function(sqlStr){
        	   for(i=0;i<this.databaseMetadata.syntax.length;i++){
        		   sqlStr = sqlStr.replace(this.databaseMetadata.syntax[i],this.databaseMetadata.output);
        	   }
        	   return sqlStr;
           },
           execSQL:function(sql,pn,fmtSql){
        	   var $this = this;
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.execSQLURL,
    				type: "POST",
    				data:{execSQL:sql,pageNumber:pn,pageSize:$this.execSQLPageSize},
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					if($this.currentExecSQL!='' && $this.currentExecSQL!=sql){
    						$this.isSqlChanged = true;
    					}
    					$this.isSqlReportValid=data.isSuccess;
    					$this.currentExecSQL=sql;
    					$this.currentExecSQLFmt=fmtSql;
    					$this.execSQLCallBack(data);
    				}
        	   });
           },
           nextPage:function(pn){
        	   var $this = this;
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.execSQLURL,
    				type: "POST",
    				data:{execSQL:$this.currentExecSQL,pageNumber:pn,pageSize:$this.execSQLPageSize},
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					$this.execSQLCallBack(data);
    				}
        	   });
           },
           execSQLCallBack:function(data){
        	   alert(data)
           },
           getFieldTypeLabel:function(type){
        	   return this.fieldTypes[type];  
           },
           execCondition:function(conditionId,callback){
        	   var $this = this;
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.conditionExecUrl,
    				type: "POST",
    				data:{conditionId:conditionId},
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					if(callback){
    						callback(data);
    					}else{
    						$.dialog({content:data.message,icon:data.isSuccess?"succeed":"error",ok:function(){},time:15});
    					}
    				}
        	   });
           },
           config:function(conf){
        		this.execSQLURL = conf.execSQLURL||this.execSQLURL;
        		this.dbMetadataURL = conf.dbMetadataURL||this.dbMetadataURL;
        		this.queryTypesUrl = conf.queryTypesUrl||this.queryTypesUrl;
        		this.fieldTypesUrl = conf.fieldTypesUrl||this.fieldTypesUrl;
        		this.conditionExecUrl = conf.conditionExecUrl||this.conditionExecUrl;
        		this.saveOrUpdateUrl = conf.saveOrUpdateUrl||this.saveOrUpdateUrl;
        		this.execSQLPageSize = conf.execSQLPageSize||this.execSQLPageSize;
        		this.loddingText = conf.loddingText||this.loddingText;
        		this.execSQLCallBack = conf.execSQLCallBack||this.execSQLCallBack;
        		this.init();
           },
           saveOrUpdate:function(data,callback){
        	   var $this = this;
        	   core.ajax.loading($this.loddingText);
        	   $.ajax({
        		   	url: $this.saveOrUpdateUrl,
    				type: "POST",
    				data:data,
    				dataType: "json",
    				cache: false,
    				success: function(data) {
    					core.ajax.unLoading();
    					if(callback){
    						callback(data);
    					}
    				}
        	   });
           }
        }
    });
    
})(jQuery,window);