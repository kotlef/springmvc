/**
 * swetKit 1.1
 *
 * create 2015-05-20,update 2015-05-22
 * Copyright (c) 2009-2015 www.chinaswet.com. All rights reserved.
 * To use it on other terms please contact zmk
 */

Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
}

Date.prototype.format = function(format){
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter
		"S" : this.getMilliseconds() //millisecond
	} 
	if(/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) {
		if(new RegExp("("+ k +")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 

var Swet={};

//调用拨号方法
Swet.phone={
  call:function(num){
	return window.parent.callTell(num);
}};


/**
 * 定义服务器
 */
Swet.server={
	url:"http://localhost:8080",
	pushUrl:"ws://www.ynswet.com:8080",
	premesUrl:"http://localhost:8080",
	jobtaskUrl:"http://localhost:8080",
	ossUrl:"http://localhost:8080",
	runUrl:"http://localhost:8080"
};
Swet.server.mainArchive=Swet.server.url+"/ynlxmainarchive";
Swet.server.alarmCenter=Swet.server.url+"/ynlxalarmcenter";
Swet.server.business=Swet.server.url+"/ynlxbusiness";
Swet.server.productRun=Swet.server.runUrl+"/ynlxproductrun";
Swet.server.cloud=Swet.server.url+"/ynlxcloud";
Swet.server.oss=Swet.server.ossUrl+"/ynlxjobtask";
Swet.server.jobtask=Swet.server.jobtaskUrl+"/ynlxjobtask";
Swet.server.msgpush=Swet.server.pushUrl+"/ynlxmsgpush/websocket";
Swet.server.premes=Swet.server.premesUrl+"/ynlxpremes";

/**
 * 定义字典
 */
Swet.dic={translate:function(key){
	return window.localStorage.getItem(key);
}}

/**
 * 封装ajax请求
 */
Swet.request={
	ajax:function(url,data,type,successCallback,errorCallback){
		data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
		$.ajax({
			type: type,
		    url: url,
		    data:data,
		    dataType:"json",
		    success: function (result) {
		    	if(jQuery.isFunction(successCallback))
		    		successCallback(result);
		    	else if("GET"!=type)
		    	    $.messager.alert("提示", result.msg, "info");
		    },
		    error: function (XMLHttpRequest,msg,exception) {
		    	if(jQuery.isFunction(errorCallback))
		    		errorCallback(XMLHttpRequest,msg,exception);
		    	else
		    	    $.messager.alert(msg, exception, "error");
		    }
		});
	},
	save:function(url,data,successCallback,errorCallback){
		this.ajax(url,data,"POST",successCallback,errorCallback);
	},
	get:function(url,data,successCallback,errorCallback){
		this.ajax(url,data,"GET",successCallback,errorCallback);
	},
	update:function(url,data,successCallback,errorCallback){
		this.ajax(url,data,"POST",successCallback,errorCallback);
	},
	delete:function(url,data,successCallback,errorCallback){
		this.ajax(url,data,"DELETE",successCallback,errorCallback);
	}
};

/**
 * 基础工具类
 * @type 
 */
Swet.util={
	getCurrentDateTime:function(){
		var d = new Date();
		return d.format("yyyy-MM-dd hh:mm:ss");
	},
	getCurrentDate:function(){
		var d = new Date();
		return d.format("yyyy-MM-dd");
	},
	/**
     * 当前时间戳
     * @return <int>        unix时间戳(秒)  
     */
    CurTime: function(){
         return Date.parse(new Date())/1000;
     },
     /**              
      * 日期 转换为 Unix时间戳
      * @param <string> 2014-01-01 20:20:20  日期格式              
      * @return <int>        unix时间戳(秒)              
      */
     DateToUnix: function(string) {
         var f = string.split(' ', 2);
         var d = (f[0] ? f[0] : '').split('-', 3);
         var t = (f[1] ? f[1] : '').split(':', 3);
         return (new Date(
              parseInt(d[0], 10) || null,
              (parseInt(d[1], 10) || 1) - 1,
              parseInt(d[2], 10) || null,
              parseInt(t[0], 10) || null,
              parseInt(t[1], 10) || null,
              parseInt(t[2], 10) || null
          )).getTime() / 1000;
     },
     /**              
      * 时间戳转换日期              
      * @param <int> unixTime    待时间戳(秒)              
      * @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)              
      * @param <int>  timeZone   时区              
      */
     UnixToDate: function(unixTime,timeZone) {
          if (typeof (timeZone) == 'number')
          {
              unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
          }
          var time = new Date(unixTime * 1000);
          return time.format("yyyy-MM-dd hh:mm:ss");
     }
}

/**
 * easyui 表单验证
 */
$.extend($.fn.validatebox.defaults.rules, {
	email:{
		validator: function (value) {
			//var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
            return reg.test(value);
        },
        message: '请输入正确的邮箱！'
	},
    //移动手机号码验证
    mobile: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message: '请输入正确的手机号！'
    },
    //国内邮编验证
    zipCode: {
        validator: function (value) {
            var reg = /^[0-9]\d{5}$/;
            return reg.test(value);
        },
        message: '请填写正确的邮编！'
    },
    //数字验证
    number: {
        validator: function (value) {
            var reg =/^[0-9]*$/;
            return reg.test(value);
        },
        message: '请输入数字！'
    },
    minLength: {
        validator: function(value, param){   //value 为需要校验的输入框的值 , param为使用此规则时存入的参数
             return value.length >= param[0];
        },
        message: '请输入最小{0}位字符.'
    },
    maxLength: {
        validator: function(value, param){
             return param[0] >= value.length;
        },
        message: '请输入最大{0}位字符.'
    },
    date : {
        validator: function(value){
             return /^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$/i.test($.trim(value));
        },
        message: '曰期格式错误,如2012-09-11.'
    },
    idcard : {// 验证身份证
        validator : function(value) {
     		var szReg=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
     		return szReg.test(value);
        },
        message : '身份证号码格式不正确'
    },
    isQQ : {// 腾讯QQ验证
        validator : function(value) {
        	var szReg = /^[1-9][0-9]{4,11}$/;
     		return szReg.test(value);
        },
        message : 'QQ号格式有误,请重新输入!'
    },
    officerCertificate : {//军官证
	    validator : function(value) { 
	    	//var reg = /南字第(\d{8})号|北字第(\d{8})号|沈字第(\d{8})号|兰字第(\d{8})号|成字第(\d{8})号|济字第(\d{8})号|广字第(\d{8})号|海字第(\d{8})号|空字第(\d{8})号|参字第(\d{8})号|政字第(\d{8})号|后字第(\d{8})号|装字第(\d{8})号/; 
	    	var reg = /^\d{9}$/;
	    	value = value.replace(/(^\s*)|(\s*$)/g, ""); 
		    if(reg.test(value) === false) { 
		    	return false; 
		    }else{ 
		    	return true; 
		    } 
	    }, 
	    //message : '请输入正确的军官证号'
	    message : '请输入正确的军官证号,9位数字'
    },
    passport : {//护照
        validator : function(value) {
        	var szReg =/(P\d{7})|(G\d{8})/;
     		return szReg.test(value);
        },
        message : '护照号码格式不正确'
    },
    isFAX : {//传真
        validator : function(value) {
        	var szReg =/^(\d{3,4})?(\-)?\d{7,8}$/;
     		return szReg.test(value);
        },
        message : '传真号码格式不正确'
    },
    iswx : {//微信
        validator : function(value) {
        	var szReg = /^[a-zA-Z\d_]{5,}$/; 
     		return szReg.test(value);
        },
        message : '微信号码格式不正确'
    },
    isUrl : {// 验证导航地址
        validator : function(value) {	
        	var re =/^((https|http|ftp|rtsp|mms)?:\/\/)?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5]$)|([0-9a-z_!~*'()-]+\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\.[a-z]{2,6})(:[0-9]{1,4})?((\/?)|(\/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+\/?)$/;
            return re.test(value);
        },
        message : '导航地址不正确'
    },
    tel : {// 座机号码地址
        validator : function(value) {	
        	var reg = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
            return reg.test(value);
        },
        message : '请输入正确的电话号码'
    },
    pwdMinLength: {
        validator: function(value, param){   //value 为需要校验的输入框的值 , param为使用此规则时存入的参数
             return value.length >= param[0];
        },
        message: '最小6个字符'
    },
    pwdMaxLength: {
        validator: function(value, param){
             return param[0] >= value.length;
        },
        message: '最大16个字符'
    },
    // extend the 'equals' rule
    equals: {
		validator: function(value,param){
			return value == $(param[0]).val();
		},
		message: '两次输入的密码不一致'
    },
    isTels : {// 逗号隔开的手机字符串
        validator : function(value) {	
        	var reg = /^1[358]\d{9}(?:\,1[358]\d{9})*$/;
            return reg.test(value);
        },
        message : '请输入正确的格式(格式为:xxxxxxxxxxx)'
    },
    isTelephone : {// 逗号隔开的手机字符串
        validator : function(value) {	
        	var reg = /^(\d{3,4})?(\-)?\d{7,8}$/;
            return reg.test(value);
        },
        message : '请输入正确的客户电话(格式为:xxxx-xxxxxxx)'
    },
    equalsTo: {//比较两个值，并设置提示
		validator: function(value,param){
			var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value) && (value!=$(param[0]).val());
		},
		message: '不是电话号码或者与联系手机相同'
    },
    ajagVerCode:{//版本号校验
    	validator:function(value){
    		var reg = /^\d+\.\d+\.\d+\.\d+$/;
    		return reg.test(value);
    	},
    	message: '版本号格式错误'
    },
    invalideHanZi:{
        validator:function(value){
            var reg=/[a-zA-Z\u4E00-\u9FA5]+$/;
            return reg.test(value);
        },
        message: '请输入正确的格式'
    },
    isWordOrNumber:{//判断输入的字符是否为:a-z,A-Z,0-9    
        validator:function(value){
            var reg =/^[a-zA-Z0-9_]+$/;
            return reg.test(value);
        },
        message: '请输入正确的格式'
    }
   
});

/**
 * 下拉框默认选择第一项
 */
$.extend($.fn.combobox.defaults, {
	onLoadSuccess: function (data) { //数据加载完毕事件
		var obj=$(this);
		var defSelected=obj.attr("defSelected")||obj.combobox("options").defSelected;
		if(defSelected!=null&&typeof(defSelected)!="undefined"&&data.length > 0){
			var value=eval("data[0]."+defSelected);
            obj.combobox("select",value);
		}
    }
});

/**
 * From扩展
 * getData 获取数据接口
 *
 * @param {Object} jq
 * @param {Object} params 设置为true的话，会把string型"true"和"false"字符串值转化为boolean型。
 */
$.extend($.fn.form.methods, {
    getData: function(jq, params){
        var formArray = jq.serializeArray();
        var oRet = {};
        for (var i in formArray) {
            if (typeof(oRet[formArray[i].name]) == 'undefined') {
                if (params) {
                    oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true" : formArray[i].value;
                }
                else {
                    oRet[formArray[i].name] = formArray[i].value;
                }
            }
            else {
                if (params) {
                    oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true" : formArray[i].value;
                }
                else {
                    oRet[formArray[i].name] += "," + formArray[i].value;
                }
            }
        }
        return oRet;
    }
});

$.extend($.fn.progressbar.methods, {
	setText: function (jq, text){
		return jq.each(function () {
			var t = $(this), opts = t.progressbar("options");
			t.find(".progressbar-text").text(opts.text = text);
		}); 
	}
});