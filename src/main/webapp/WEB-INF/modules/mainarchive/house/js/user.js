//用户信息界面:使用组件的validType属性对手机号、邮箱、证件号进行唯一性的异步验证
var checkEmailUrl,checkCellUrl,checkIdNoUrl;
checkEmailUrl = Swet.server.cloud+'/user/checkEmail';checkEmail = 'email';//邮箱验证
checkCellUrl = Swet.server.cloud+'/user/checkCell';checkCell = 'cell';//手机号验证
checkIdNoUrl = Swet.server.cloud+'/user/checkIdNo';checkIdNo = 'idNo';//证件号验证
$(function(){ 
	$("#datagrid_list_user").datagrid({
		method:"GET",
		nowrap:"false",
		striped:"true",
		sortName:"uid",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"uid",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		singleSelect:true,
		fit:true,
		columns:[[
				  {field:"userName",title:"用户名称",align:"left",width:50},
				  {field:"userType",title:"用户类型",align:"left",width:50,formatter:translateUtype},
				  {field:"idType",title:"证件类型",align:"left",width:50,formatter:translateItype},
				  {field:"idNo",title:"证件号码",align:"left",width:50 },
				  {field:"cell",title:"手机号",align:"left",width:50 },
				  {field:"email",title:"邮箱",align:"left",width:50 }
				]],
		toolbar:"#btn_operate_user"
	});
})

function translateUtype(v){
	return Swet.dic.translate("USER_TYPE_"+v);
}
function translateItype(v){
	return Swet.dic.translate("ID_TYPE_"+v);
}

/**
 * 查找房屋下的住户
 * @param houseId
 */
function searchUser(houseId) {
	var url=Swet.server.mainArchive+'/houseresident/searchHouseresidentByHouseId/'+houseId;
	Swet.request.get(url,"",function(data){
		 var  UID="";
		for(var i = 0;i < data.length; i++) {
			if(UID==""){
        		UID=data[i];
        	}else{
        		UID=data[i]+","+UID;
			}
		}
		if(UID!=null){
			Swet.request.get(Swet.server.cloud+"/user/searchUserByUid",{UID:UID},function(data){
				$("#datagrid_list_user").datagrid("loadData",data);  
			});
		}
	});
}


/**
* 删除房屋住户
* @param {} 
 */
function deleteUser(uid){
	var selRows = $('#datagrid_list_user').datagrid("getSelected");
	var row=$('#datagrid_list_user').datagrid("getSelected");
	if (selRows==null) { 
		 $.messager.alert("提示", "请选择你要删除的住户！", "info"); 
		 return; 
	}
	var houseId=$('#houseId').val();
	var uid=row.uid;
	var obj=houseId+ ':' + uid;
    $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        if (r){
        	Swet.request.delete(
        			Swet.server.mainArchive+"/houseresident/deleteUserById/"+obj,"",function(result){ 
        				if(result.success){
        					$.messager.show({
        						title:'提示',
        						msg:result.msg,
        						showType:'slide',
        						style:{
        							right:'',
        							top:document.body.scrollTop+document.documentElement.scrollTop,
        							bottom:''
        						}
        					});
        					searchUser(houseId);
        				}
        			}
        	);
        }
       });
}
/**
 * 打开编辑用户界面
 */
function addOrUpdateUser(val){
	$('#form_edit_user').form("clear");
	var selRows=$('#datagrid_list_house').datagrid("getSelected");
	var row=$('#datagrid_list_user').datagrid("getSelected");
	if(val=="1"){
		if (selRows==null) { 
			 $.messager.alert("提示", "编辑住户前，请选择一个房屋 ！", "info"); 
			 return; 
		}
	}else{
		if (row==null) { 
			 $.messager.alert("提示", "请选择要编辑的住户！", "info"); 
			 return; 
		}
	
		$('#form_edit_user').form('load',row);
	}
	$("#dialog_edit_user").dialog("open");
}
/**
* 保存房屋住户
* @param {} 
 */
function saveUser(){
	var oldUid=$("#uid").val();
	var houseId=$("#houseId").val();
	var url=Swet.server.cloud+"/user/addUser";
	if(oldUid!=null&&oldUid>0){//uid>0--修改用户
		 url=Swet.server.cloud+"/user/update/"+oldUid;
	 }
	$("#form_edit_user").form("submit",{
		url:url,
		method:"POST",
		onSubmit:function(){
			return $(this).form('validate');
		},success:function(result){
			result=$.parseJSON(result);
			if(oldUid!=null&&oldUid>0){
				searchUser(houseId);
			}else{
				var obj=houseId+ ',' + result.rows;
				Swet.request.save(Swet.server.mainArchive+"/houseresident/saveUser/"+obj,"",function(result){
					if(result.success){
						searchUser(houseId);
					}
				});
			}
			$.messager.show({
				title:'提示',
				msg:"编辑成功！",
				showType:'slide',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#form_edit_user').form('clear');
			$('#dialog_edit_user').dialog('close');
			
		}
	});
}
/**************************第二种方法暂时解决问题*******************/
/**
 * 打开修改用户界面
 */
function doUpdateUser(){
	$('#form_update_user').form("clear");
	var selRows=$('#datagrid_list_house').datagrid("getSelected");
	var row=$('#datagrid_list_user').datagrid("getSelected");
	if (selRows==null) { 
		$.messager.alert("提示", "编辑成员前，请选择一个客户 ！", "info"); 
		return; 
	}
	if (row==null) { 
		$.messager.alert("提示", "请选择要编辑的成员！", "info"); 
		return; 
	}
	$('#form_update_user').form('load',row);
	$("#dialog_update_user").dialog("open");
}

function updateUser(){
	var oldUid=$("#update_user_uid").val();
	var row=$('#datagrid_list_house').datagrid("getSelected");
	var url=Swet.server.cloud+"/user/update/"+oldUid;
	$("#form_update_user").form("submit",{
		url:url,
		method:"POST",
		onSubmit:function(){
			return $(this).form('validate');
		},success:function(result){
			searchUser(row.houseId);
			$.messager.show({
				title:'提示',
				msg:"编辑成功！",
				showType:'slide',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#form_update_user').form('clear');
			$('#dialog_update_user').dialog('close');
			
		}
	});
}
function CancelUpdate(){
	$('#form_update_user').form('clear');
	$('#dialog_update_user').dialog('close');
}
/**********************************************************************************/
/**
* 关闭添加房屋住户页面
* @param {} 
 */
function CancelEditUser() {
	$("#dialog_edit_user").dialog("close");
	$('#form_edit_user').form('clear');
}