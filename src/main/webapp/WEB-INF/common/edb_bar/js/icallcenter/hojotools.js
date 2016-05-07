hojo.provide("icallcenter.hojotools");

icallcenter.hojotools.input = function(connectType) {
	   var clickBtn = "";
	   var keyEvent = "";
	   if(connectType == "transfer") {
		   clickBtn = "<input type=button value=\"转接\" class=fl style=margin-left:5px; onclick=softphoneBar.exTransfer(hojo.byId('softphone.input').value) />";
		   keyEvent = "onKeyDown=\"if(event.keyCode == 13){softphoneBar.exTransfer(hojo.byId('softphone.input').value)}\"";
	   } else if(connectType == "consult") {
		   clickBtn = "<input type=button value=\"咨询\"  class=fl onclick=softphoneBar.exConsult(hojo.byId('softphone.input').value) />";
		   keyEvent = "onKeyDown=\"if(event.keyCode == 13){softphoneBar.exConsult(hojo.byId('softphone.input').value)}\"";
	   }
	   var msgw,msgh,bordercolor;
	   msgw=247;
	   msgh=52;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById("softphonebar").offsetWidth;
	   sHeight=document.getElementById("softphonebar").offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','hollyc5.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.6";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById("softphonebar").appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","hollyc5.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
       msgObj.style.left = "50%";
       msgObj.style.top = "50%";
       msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
       msgObj.style.marginLeft = "-135px" ;
       msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px";
       msgObj.style.width = msgw + "px";
       msgObj.style.height =msgh + "px";
       msgObj.style.textAlign = "left";
       msgObj.style.lineHeight ="25px";
       msgObj.style.zIndex = "10001";
       msgObj.style.paddingTop = "2px";
       msgObj.style.paddingLeft = "18px";
       msgObj.style.paddingRight = "4px";
       msgObj.innerHTML = "<div style='text-align:right'>" +
       							"<a href=javascript:icallcenter.hojotools.close();>" +
       								"<img src='../imgs/tinybox/close_1.gif' onmouseover=\"this.src='../imgs/tinybox/close_2.gif'\" onmouseout=\"this.src='../imgs/tinybox/close_1.gif'\" />" +
       							"</a>" +
       						"</div>" +
       						"<div style='height:35px;overflow:hidden;text-align:center'>" +
       							"<div id='hollyc5.loading.message' style='float:left;color:#666666;padding-left:5px;'>" +
       								"<input id='softphone.input' value='请输入手机号码或工号'   onfocus='if(value==\"请输入手机号码或工号\") {value=\"\"}'  onblur='if(value==\"\"){value=\"请输入手机号码或工号\"}' type='text' size='20' class='fl' "+keyEvent+" />" + clickBtn + "</div>" +
       						"</div>";
       document.getElementById("softphonebar").appendChild(msgObj);
};

icallcenter.hojotools.close = function() {
	if(icallcenter.hojotools.notifyDialogInterval != "") {
		clearInterval(icallcenter.hojotools.notifyDialogInterval);
	}
	var bgObj = document.getElementById("hollyc5.bgDiv");
	var msgObj = document.getElementById("hollyc5.msgDiv");
	if(msgObj != null) {
		document.getElementById("softphonebar").removeChild(msgObj);	
	}
	if(bgObj != null) {
		document.getElementById("softphonebar").removeChild(bgObj);	
	}
};

icallcenter.hojotools.dateParse = function(date) {
    var year = date.getFullYear(); 
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    return year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day) + " " 
                + (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + (second > 9 ? second : "0" + second);
};

icallcenter.hojotools.notifyDialogStayRemain;
icallcenter.hojotools.notifyDialogInterval = "";
icallcenter.hojotools.index = 1;

icallcenter.hojotools.showLoading = function(destExten) {
	icallcenter.hojotools.notifyDialogStayRemain = 40000;
	icallcenter.hojotools.index = 1;
	icallcenter.hojotools.loading("", "softphonebar");
	icallcenter.hojotools.notifyDialogInterval = setInterval(function(){
		icallcenter.hojotools.checkLoadingHide(destExten);
	}, 1000);
};

icallcenter.hojotools.checkLoadingHide = function(destExten) {
	var index = icallcenter.hojotools.index ++;
	var html = ("正在等待<span style='color:#3fb23f;font-weight:bold'>"+destExten+ "</span>接听，" + "请稍后<span style='font-weight:bold'>(00:" + (index<10?("0"+index):index) +")</span>");
	document.getElementById("hollyc5.loading.message").innerHTML = html;
	if(icallcenter.hojotools.notifyDialogStayRemain <= 0){
		icallcenter.hojotools.hideNotify();
	}
	icallcenter.hojotools.notifyDialogStayRemain -= 1000;
};

icallcenter.hojotools.hideNotify = function() {
	icallcenter.hojotools.close();
	clearInterval(icallcenter.hojotools.notifyDialogInterval);
};

icallcenter.hojotools.loading = function(message, parentId) {
	   var msgw,msgh,bordercolor;
	   msgw=247;
	   msgh=52;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById(parentId).offsetWidth;
	   sHeight=document.getElementById(parentId).offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','hollyc5.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.6";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById(parentId).appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","hollyc5.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
	   msgObj.style.left = "50%";
	   msgObj.style.top = "50%";
	   msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	   msgObj.style.marginLeft = "-135px" ;
	   msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px";
	   msgObj.style.width = msgw + "px";
	   msgObj.style.height =msgh + "px";
	   msgObj.style.textAlign = "left";
	   msgObj.style.lineHeight ="25px";
	   msgObj.style.zIndex = "10001";
	   msgObj.style.paddingTop = "11px";
	   msgObj.style.paddingLeft = "12px";
	   msgObj.style.paddingRight = "10px";
	   msgObj.innerHTML = "<div style='height:20px;overflow:hidden;text-align:center'><img src='../imgs/loading.gif' style='float:left;margin-top:5px;' /><div id='hollyc5.loading.message' style='float:left;color:#666666;padding-left:5px;'>" + message + "</div><div style='clear:both;height:1px;overflow:hidden'>&nbsp;</div></div>";

	   document.getElementById(parentId).appendChild(msgObj);
};


icallcenter.hojotools.error = function(message) {
	//自改错误消息提示
	console.log(message);
	/*var msgw,msgh,bordercolor;
	msgw=249;
    msgh=152;
    bordercolor="#c6c6c6";
   
    var sWidth,sHeight;
    sWidth=document.getElementById("softphonebar").offsetWidth;
    sHeight=document.getElementById("softphonebar").offsetHeight;
    var bgObj=document.createElement("div");
    bgObj.setAttribute('id','hollyc5.bgDiv');
    bgObj.style.position="absolute";
    bgObj.style.top="0";
    bgObj.style.background="#f2f2f2";
    bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity="0.6";
    bgObj.style.left="0";
    bgObj.style.width=sWidth + "px";
    bgObj.style.height=sHeight + "px";
    bgObj.style.zIndex = "10000";
    document.getElementById("softphonebar").appendChild(bgObj);

    var msgObj=document.createElement("div");
    msgObj.setAttribute("id","hollyc5.msgDiv");
    msgObj.setAttribute("align","center");
    msgObj.style.background="white";
    msgObj.style.border="1px solid " + bordercolor;
    msgObj.style.position = "absolute";
	msgObj.style.left = "50%";
	msgObj.style.top = "50%";
    msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
    msgObj.style.marginLeft = "-135px" ;
    msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px";
    msgObj.style.width = msgw + "px";
    msgObj.style.height =msgh + "px";
    msgObj.style.textAlign = "left";
    msgObj.style.lineHeight ="25px";
    msgObj.style.zIndex = "10001";
    msgObj.innerHTML = "<div style='height:27px;background: #f3f3f3;overflow:hidden;padding-top:10px;padding-right:10px;'><div style='float:right'><a href='javascript:icallcenter.hojotools.close();'><img src='../imgs/tinybox/close_1.gif' style='cursor: pointer;border:0px;' /></a></div></div>"+
        					"<div style='height:30px;overflow:hidden;margin-top:20px;padding-left:20px;'><img src='../imgs/error.jpg' style='float:left;margin-top:4px;' /><div style='float:left;color:#666666;padding-left:5px;font-size:15px;padding-top:4px'>"+message+"</div></div>"+
        					"<div style='clear:both;height:1px;overflow:hidden'>&nbsp;</div>"+
        					"<div style='padding-left:180px;padding-top:20px;'><a href='javascript:icallcenter.hojotools.close();'><img src='../imgs/confirm_1.jpg' onmouseover=\"this.src='../imgs/confirm_2.jpg'\" onmouseout=\"this.src='../imgs/confirm_1.jpg'\" style='cursor: pointer;border:0px;' /></a></div>";

    document.getElementById("softphonebar").appendChild(msgObj);*/
}

icallcenter.hojotools.showSucc = function(message) {
	icallcenter.hojotools.notifyDialogStayRemain = 500;
	icallcenter.hojotools.success(message);
	icallcenter.hojotools.notifyDialogInterval = setInterval("icallcenter.hojotools.checkHide()", 500);
};

icallcenter.hojotools.checkHide = function() {
	if(icallcenter.hojotools.notifyDialogStayRemain <= 0){
		icallcenter.hojotools.hideNotify();
	}
	icallcenter.hojotools.notifyDialogStayRemain -= 500;
};

icallcenter.hojotools.success = function(message) {
	   var msgw,msgh,bordercolor;
	   msgw=172;
	   msgh=56;
	   bordercolor="#c6c6c6";
	   
	   var sWidth,sHeight;
	   sWidth=document.getElementById("softphonebar").offsetWidth;
	   sHeight=document.getElementById("softphonebar").offsetHeight;
	   var bgObj=document.createElement("div");
	   bgObj.setAttribute('id','hollyc5.bgDiv');
	   bgObj.style.position="absolute";
	   bgObj.style.top="0";
	   bgObj.style.background="#f2f2f2";
	   bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	   bgObj.style.opacity="0.6";
	   bgObj.style.left="0";
	   bgObj.style.width=sWidth + "px";
	   bgObj.style.height=sHeight + "px";
	   bgObj.style.zIndex = "10000";
	   document.getElementById("softphonebar").appendChild(bgObj);

	   var msgObj=document.createElement("div");
	   msgObj.setAttribute("id","hollyc5.msgDiv");
	   msgObj.setAttribute("align","center");
	   msgObj.style.background="white";
	   msgObj.style.border="1px solid " + bordercolor;
	   msgObj.style.position = "absolute";
	   msgObj.style.left = "50%";
	   msgObj.style.top = "50%";
	   msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	   msgObj.style.marginLeft = "-105px" ;
	   msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px";
	   msgObj.style.width = msgw + "px";
	   msgObj.style.height =msgh + "px";
	   msgObj.style.textAlign = "left";
	   msgObj.style.lineHeight ="25px";
	   msgObj.style.zIndex = "10001";
	   msgObj.style.paddingTop = "18px";
	   msgObj.style.paddingLeft = "35px";
	   msgObj.innerHTML = "<div style='height:30px;overflow:hidden'><img src='../imgs/success.jpg' style='float:left;margin-top:4px;' /><div style='float:left;color:#666666;padding-left:5px;font-size:15px;padding-top:4px'>" + message + "</div><div class='clear1'>&nbsp;</div></div>";

	   document.getElementById("softphonebar").appendChild(msgObj);
};


