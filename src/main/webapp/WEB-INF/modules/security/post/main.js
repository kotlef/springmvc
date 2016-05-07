var userId;
$(function(){

});
//翻译用户的类型
function translateUserType(userType){
	return Swet.dic.translate("USER_TYPE_"+userType);
}
//翻译证件的类型
function translateItype(idType){
	//ID_TYPE为需要翻译的keyword
	return Swet.dic.translate("ID_TYPE_"+idType);
}

var editIndex = undefined;
var obj = new Object();
/**
 * 判断结束编辑
 * @returns {Boolean}
 */
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#datagrid_list_post').datagrid('validateRow', editIndex)){
		var orgEd = $('#datagrid_list_post').datagrid('getEditor', {index:editIndex,field:'id'});
		var roleEd = $('#datagrid_list_post').datagrid('getEditor', {index:editIndex,field:'roleId'});
        var roleName = $(roleEd.target).combobox('getText');
        var text = $(orgEd.target).combotree('getText');
        var a = $('#datagrid_list_post').datagrid('getRows')[editIndex];
        $('#datagrid_list_post').datagrid('getRows')[editIndex]['roleName'] = roleName;
        $('#datagrid_list_post').datagrid('getRows')[editIndex]['text'] = text;
		$('#datagrid_list_post').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
/**
 * 添加按钮发生的方法
 */
function addPost(){
	$("#datagrid_list_post").datagrid('selectRow', editIndex);
	var user = $("#datagrid_list_user").datagrid("getSelected");
	if (endEditing() && !user == ""){
		 $('#datagrid_list_post').datagrid('appendRow',{id:'',roleId:''});
         editIndex = $('#datagrid_list_post').datagrid('getRows').length-1;
		$("#datagrid_list_post").datagrid('beginEdit',editIndex)
								.datagrid('selectRow', editIndex);
	}else{
		$.messager.show({
			title:'提示信息!',
			msg:'请选择一个用户!',
            showType:'slide',
            timeout:3000,
            style:{
                right:'',
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
		});
	}
}
/**
 * 联动加载角色
 */
function loadRole(org){
	var roleIds= org.id;
	var roleIdEd = $('#datagrid_list_post').datagrid('getEditor', {index:editIndex,field:'roleId'});
	$(roleIdEd.target).combobox({url:Swet.server.cloud+'/orgrole/findByOrgidToRole/'+roleIds});
	
}
/**
 * 保存按钮的方法
 */
function savePost(){
	endEditing();
	var user = $("#datagrid_list_user").datagrid("getSelected");
	if(! user==""){
		var roleIds=$("#datagrid_list_post").datagrid("getChanges","inserted");
		if(roleIds.length > 0){
			for(var i = 0; i < roleIds.length;i++){
				obj.uid=user.uid;
				obj.orgid=roleIds[i].id;
				obj.roleId=roleIds[i].roleId;
				saveObj(obj);
			}
		}
	}
}
/**
 * 添加的数据
 * @param obj
 */
function saveObj(obj){
	Swet.request.save(
		Swet.server.cloud +'/post', 
		obj,
		function(result){
			$('#datagrid_list_post').datagrid({
				method:'get',
				url:Swet.server.cloud +'/post/searchByUid/'+obj.uid
			});
			toptip('保存成功!');
		},
		function(result){
			errorCallback(result)
		}
	);
}

/**
 * 双击以后进行修改
 * @param index
 * @param field
 */
function onClickCell(index, field){
    if (editIndex != index){
        if (endEditing()){
            $('#datagrid_list_post').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
            var ed = $('#datagrid_list_post').datagrid('getEditor', {index:index,field:field});
            ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            editIndex = index;
        } else {
            $('#datagrid_list_post').datagrid('selectRow', editIndex);
        }
    }
}



/**
 * 选中用户时加载已分配的部门角色
 */
function loadOrgRole(rowIndex,rowData){
	editIndex = undefined;
	endEditing();
	var uid= rowData.uid;
	$('#datagrid_list_post').datagrid({
		method:'get',
		url:Swet.server.cloud +'/post/searchByUid/'+uid
	});
}
function deletPost(){
	var user = $("#datagrid_list_user").datagrid("getSelected");
	var uid = user.uid;
	var orgRole=$("#datagrid_list_post").datagrid("getSelections");
	if(user!= null&& !orgRole != null){
		for(var i = 0; i < orgRole.length;i++){
			var postids = orgRole[i].id.uid +","+ orgRole[i].id.orgid +","+orgRole[i].id.roleId;
			deleteObj(uid,postids);
		}
	}
}
function deleteObj(uid,postids){
	Swet.request.delete(
		Swet.server.cloud +'/post/deletePost/'+postids,
		'',
		function(result){
			$('#datagrid_list_post').datagrid({
				method:'get',
				url:Swet.server.cloud +'/post/searchByUid/'+uid
			});
			toptip('删除成功!');
		},
		function(result){
			errorCallback(result);
		}
	)
}
/**
 * 提示(页面顶部居中位置)
 */
function toptip(msg){
	$.messager.show({
		title:"提示信息",
		msg:msg,
        showType:'slide',
        timeout:3000,
        style:{
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:''
        }
	});
}
/**
 * 失败时提示方法
 */
function errorCallback(result){
	if(!result.success){
			$.messager.alert({  
                title: 'Error',  
                msg: '操作失败' 
           });
	}
}