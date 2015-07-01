/**
 * swetKit 1.1
 *
 * create 2015-05-20,update 2015-05-22
 * Copyright (c) 2009-2015 www.chinaswet.com. All rights reserved.
 * To use it on other terms please contact zmk
 */

var Swet={};

/**
 * 定义服务器
 */
Swet.server={
	url:"http://localhost:8080"
};
Swet.server.mainArchive=Swet.server.url+"/ynlxmainarchive";
Swet.server.alarmCenter=Swet.server.url+"/ynlxalarmcenter";
Swet.server.business=Swet.server.url+"/ynlxbusiness";
Swet.server.productRun=Swet.server.url+"/ynlxproductrun";
Swet.server.cloud=Swet.server.url+"/ynlxcloud";

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
 * easyui 表单验证
 */
$.extend($.fn.validatebox.defaults.rules, {
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
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message : '身份证号码格式不正确'
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