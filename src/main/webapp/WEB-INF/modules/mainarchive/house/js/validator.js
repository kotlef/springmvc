//扩展easyui表单的验证
$.extend($.fn.validatebox.defaults.rules, {
    //验证汉子
    CHS: {
        validator: function (value) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '只能输入汉字'
    },
    NOCHS: {
        validator: function (value) {
        	var patrn=/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi; 
        	if(!patrn.exec(value)){ 
        		return true; 
        	}else{ 
        		return false; 
        	} 
        },
        message: '输入的内容不能包含中文!'
    },
    //移动手机号码验证
    mobile: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message: '输入手机号码格式不准确.'
    },
    //邮箱验证
    semail:{
    	validator:function(value){
    		value=value.trim();
    		var xx= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    		return xx.test(value);
    	},
    	message:'请输入正确的邮箱!'
    },
    //国内邮编验证
    zipcode: {
        validator: function (value) {
            var reg = /^[1-9]\d{5}$/;
            return reg.test(value);
        },
        message: '邮编必须是非0开始的6位数字.'
    },
    //电话号码和手机号码同时验证
    phoneNumber: {
        validator: function (value) {
            var reg =/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
            return reg.test(value);
        },
        message: '请输入正确电话或手机号！'
    },
    //用户账号验证(只能包括 _ 数字 字母) 
    account: {//param的值为[]中值
        validator: function (value, param) {
            if (value.length < param[0] || value.length > param[1]) {
                $.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';
                return false;
            } else {
                if (!/^[\w]+$/.test(value)) {
                    $.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
                    return false;
                } else {
                    return true;
                }
            }
        }, message: ''
    }
});