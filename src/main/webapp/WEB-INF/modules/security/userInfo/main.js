/***
 * 修改密码模块
 * 李玉鹏
 */
var checkPasswordUrl,checkPasswordKey,userInfo,currentUser,currentOrg,currentRes,currentRoles;
checkPasswordKey = 'oldPassword';//原密码验证
checkPasswordUrl = Swet.server.cloud+'/userlogin/checkPassword';

//当前登录用户信息
var rolesIds = [];
var resCommunitys = [];
var resStations = [];
userInfo=JSON.parse(Swet.dic.translate("currentUserInfo"));
currentUser=userInfo.user;
currentOrg=userInfo.org;

if(null != userInfo || userInfo != ""){
	currentRoles = userInfo.role;
	if(null != currentRoles || currentRoles != ""){
		for(var i=0;i<currentRoles.length;i++){
			rolesIds.push(currentRoles[i].roleId);
		}
	}
	currentRes=userInfo.res;
	if(null != currentRes || currentRes != ""){
		for(var i=0;i<currentRes.length;i++){
			if(currentRes[i].resType=="C"){
				resCommunitys.push(currentRes[i].resId);
			}
			if(currentRes[i].resType=="S"){
				resStations.push(currentRes[i].resId);
			}
		}
	}
}

$(function(){
	$.fn.validatebox.defaults.rules.remote.message = '原密码输入错误！';
	$('#tt').tabs({
		tabPosition:'left',
		onSelect: function(title,index){
			switch (title) {
				case "组织资源":
					$('#datagrid_login_user_org').datagrid('loadData',currentOrg);
					//$('#datagrid_login_user_res').datagrid('loadData',currentRes);
					$('#datagrid_login_user_res_tb_resType').combobox({
						method:'GET',
						url:Swet.server.mainArchive+'/wordbook/list/RES_TYPE',
						valueField:'wordValue',
						textField:'wordDisplay',
						editable:false,
						onSelect: function(record){
							searchLoginResByResType(record);
						}
					});
					break;
				case "角色菜单":
					$.messager.progress({
						title:'请稍后',
						msg:'系统正加载数据...'
					});
					Swet.request.get(Swet.server.cloud+"/homepage/findByRoles",
						{roles:rolesIds}, function (homepages) {
							$.messager.progress('close');
								$("#datagrid_login_user_role").datagrid({
									//动态加载选择的菜单
									data:homepages,
									onClickRow:function(index,row){
										lookRoleMenu(row);
									}
								});
								loginRoleMenuReload();
							}
					);
					break;
				default:
					break;
			}
		}
	});
	$('#div_create_table').append(createImgDIV(currentUser));
})
function searchLoginResByResType(record){
	var url,queryResIds;
	switch (record.wordValue){
		case "C":
			url = Swet.server.mainArchive+"/community/findOrgResByResType";
			queryResIds = resCommunitys;
			if(resCommunitys.length==0){
				$('#datagrid_login_user_res').datagrid('loadData',[]);
			}else{
				$('#datagrid_login_user_res').datagrid({
					method:'get',
					queryParams:{resIds:queryResIds},
					url:url
				});
			}
			break;
		case "S":
			url = Swet.server.mainArchive+"/station/findOrgResByResType";
			queryResIds = resStations;
			if(resStations.length==0){
				$('#datagrid_login_user_res').datagrid('loadData',[]);
			}else{
				$('#datagrid_login_user_res').datagrid({
					method:'get',
					queryParams:{resIds:queryResIds},
					url:url
				});
			}
			break;
		default:
			break;
	}


}
function loginRoleMenuReload(){
	$("#login_tree_list_menu").tree({
		url:Swet.server.cloud+'/menu/searchByRoleIds/toTree',
		method:"get",
		id:"id",
		text:"text",
		queryParams:{roleIds:rolesIds},
		onLoadSuccess: function (node,data) {
			$("#login_tree_list_menu").tree("expandAll");//默认展开树的所有菜单
		}
	});
}
/*
 *  查询已有权限
 */
function lookRoleMenu(row){
	var roles = new Array;
	roles.push(row.roleId);
	$("#login_tree_list_menu").tree({
		url:Swet.server.cloud+'/menu/searchByRoleIds/toTree',
		method:"get",
		id:"id",
		text:"text",
		queryParams:{roleIds:roles},
		onLoadSuccess: function (node,data) {
			$("#login_tree_list_menu").tree("expandAll");//默认展开树的所有菜单
		}
	});
}
function createImgDIV(currentUser){
	var idType = Swet.dic.translate("ID_TYPE_"+currentUser.idType);
	var userType = Swet.dic.translate("USER_TYPE_"+currentUser.userType);
	var status = Swet.dic.translate("STATUS_"+currentUser.status);
	var div=
	'<div style="margin:2px;width:100%;float:center;">'+
		'<table style="width:100%" cellpadding="5" border="0">'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "用户名称:" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ currentUser.userName + '</td>'+
				'<td style="width:70px;text-align:right">'+ "用户类型：" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ userType + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "证件类型:" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ idType + '</td>'+
				'<td style="width:70px;text-align:right">'+ "证件号：" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ currentUser.idNo + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "联系手机:" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ currentUser.cell + '</td>'+
				'<td style="width:70px;text-align:right">'+ "电子邮箱：" + '</td>'+
				'<td style="width:160px;text-align:left;">'+ currentUser.email + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "创建时间:" + '</td>'+
				'<td style="width:50px;text-align:left;">'+currentUser.createTime+  '</td>'+
				'<td style="width:70px;text-align:right">'+ "最后修改时间:" + '</td>'+
				'<td style="width:50px;text-align:left;">'+currentUser.modifyTime + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "状态:" + '</td>'+
				'<td style="width:50px;text-align:left;">'+ status+  '</td>'+
				'<td style="width:70px;text-align:right">'+ "备注:" + '</td>'+
				'<td style="width:50px;text-align:left;">'+currentUser.note + '</td>'+
			'</tr>'+
		'</table>'+ 
	'</div>';
	return div;
}
function gridTranslateResType(v){
	return Swet.dic.translate("RES_TYPE_"+v);
}
/*
 * 修改密码
 */
function updatePassword(){
	if(!$('#form_changePwd').form('validate')){
		return;
	}
	Swet.request.save(Swet.server.cloud + "/changePassword",
			{
				oldPassword:$('#txt_old_pwd').textbox("getValue"),
				newPassword:$('#txt_new_rpwd').textbox("getValue")
			},
			function(result){
				var editStatus = JSON.parse(result);
				if(editStatus){
					$.messager.alert('提示','密码修改成功，请重新登录!','info',function(){
						window.top.location.href = "/ynlxcloud/logout";
					});
				}else{
					$.messager.alert('提示','密码修改失败,请核对原密码！','error');
				}
			}
	);
}
Date.prototype.format = function(format){
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(),    //day
		"h+" : this.getHours(),   //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
		"S" : this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
		(this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)if(new RegExp("("+ k +")").test(format))
		format = format.replace(RegExp.$1,
			RegExp.$1.length==1 ? o[k] :
				("00"+ o[k]).substr((""+ o[k]).length));
	return format;
}
setInterval(function(){
	var time = new Date().format("hh:mm:ss");
	var date = new Date().format("yyyy-MM-dd");
	document.getElementById("Time").innerHTML=time;
	document.getElementById("Date").innerHTML=date;
},1000);