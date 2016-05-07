var currentOrg;
var checkCellUrl = Swet.server.cloud+'/user/checkCell';checkCell = 'cell';//手机号验证
var checkEmpCodeUrl =Swet.server.cloud+'/emp/checkEmpCode';checkempCode='empCode';
$(function(){
	var arrayAllOrgids = new Array();
	var info=JSON.parse(Swet.dic.translate("currentUserInfo"));
	currentOrg=info.org;
	var orgids=new Array();
	for(var i=0;i<info.org.length;i++){
		var org = info.org[i];
		orgids.push(org.id)
	}
	var parentIds;
	parentIds = orgids.join(",");
	Swet.request.get(
		Swet.server.cloud +'/org/findNoteId',  //提交的路径
		{parentIds:parentIds},
		function(result){
			var allOrgids = result.concat(orgids);
			var ids = allOrgids.join(",");
			$('#datagrid_list_operate').datagrid({
				onBeforeLoad:function(param){
					var value = $.trim($("#searchbox_emp").searchbox("getValue"));
					if(!value == ""){
						$('#datagrid_list_operate').datagrid('options').url = Swet.server.cloud+'/user/searchByOrgidAndValue?orgids='+ids
						param.value=value;
					}else{
						$('#datagrid_list_operate').datagrid('options').url = Swet.server.cloud+'/user/findByorgids?orgids='+ids
					}
				} 
			});
		},
		function(result){
			errorCallback(result);
		}
	);
	//添加界面
	$('#combotree_add_org').combotree({
		url:Swet.server.cloud+'/org/findOrgByParentIds?parentIds='+parentIds,
		onSelect:loadRoleAdd
	});
	//修改界面
	$('#combotree_update_org').combotree({
		url:Swet.server.cloud+'/org/findOrgByParentIds?parentIds='+parentIds,
		onSelect:loadRoleUp
	});
	
	//状态添加
	$('#cmb_add_operate_status').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/STATUS'
	});
	//状态修改
	$('#cmb_update_operate_status').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/STATUS'
	});
});

//翻译状态
function translateStatus(status){
	return Swet.dic.translate("STATUS_"+status);
}
function formatterActionColumns(val,row,index){
	//index:当前行的索引
   var ctrs = [
      '<span  title="修改" class="kit-img-btn kit-btn-edit" onclick="openUpdate('+index+');" ></span>',
      '<span  title="删除" class="kit-img-btn kit-btn-del" onclick="deleteRow('+index+');" ></span>'
      ];
   return ctrs.join('');
}
function loadRoleAdd(org){
	var roleIds= org.id;
	$('#combobox_add_role').combobox({
		method:'get',
		url:Swet.server.cloud+'/orgrole/findByOrgidToRole/'+roleIds,
		editable:false,
		valueField:'roleId',
		textField:'roleName',
		prompt:'请选择'
	});
}
function loadRoleUp(org){
	var roleIds= org.id;
	$('#combobox_update_role').combobox({
		method:'get',
		url:Swet.server.cloud+'/orgrole/findByOrgidToRole/'+roleIds,
		editable:false,
		valueField:'roleId',
		textField:'roleName',
		prompt:'请选择',
	});
}
function add(){
//	checkEmpCode();
	$("#dlg_add_operate").dialog("open");
	$("#dlg_add_operate").dialog("setTitle","添加人员");
}
function openUpdate (index){
	$('#datagrid_list_operate').datagrid('selectRow',index);
	var row =$("#datagrid_list_operate").datagrid("getSelected");
	$("#dlg_update_operate").dialog("open");
	$("#dlg_update_operate").dialog("setTitle","修改人员");
	$('#from_update_operate').form('load',row);
}
function deleteRow(index){
	$('#datagrid_list_operate').datagrid('selectRow',index);
	var rows =$("#datagrid_list_operate").datagrid("getSelections");
	deleteOperate(rows);
}
function deleteRows(){
	var rows =$("#datagrid_list_operate").datagrid("getChecked");
	deleteOperate(rows);
}
function saveAdd(){
	if(!$('#from_add_operate').form('validate')){
		return;
	}
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	var obj = $("#from_add_operate").form("getData");
	Swet.request.save(
		Swet.server.cloud +'/emp/saveOpterae',  //提交的路径
		obj,
		function(result){
			$.messager.progress('close');
			successCallback(result);
		},
		function(result){
			$.messager.progress('close');
			errorCallback(result);
		}
	);
}
function saveUpdate(){
	if(!$('#from_update_operate').form('validate')){
		return;
	}
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	var rows =$("#datagrid_list_operate").datagrid("getSelections");
	var obj = $("#from_update_operate").form("getData");
	obj.uid = rows[0].uid;   
	Swet.request.update(
			Swet.server.cloud +'/emp/updateOpterae/'+rows[0].uid,  //提交的路径
			obj,
			function(result){
				$.messager.progress('close');
				successCallback(result);
			},
			function(result){
				$.messager.progress('close');
				errorCallback(result);
			}
	);
}
function deleteOperate(rows){
	if(rows.length < 1){
		toptip("请选择需要删除的用户!");
		return ;
	}else{
    	var  uids="";
        for (var i = 0; i < rows.length; i++) {
        	if(uids==""){
        		uids=rows[i].uid;
        		
        	}else{
        		uids=rows[i].uid+","+uids;
			}
        }
        
		$.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		$.messager.progress({
        			text:'数据删除中,请稍候.....'
        		});
        		Swet.request.delete(
        				Swet.server.cloud+"/emp/"+uids,uids,
        				function(result){
		        			$.messager.progress('close');
			        	    toptip('删除成功！');
			        	    $("#datagrid_list_operate").datagrid("reload");
        				},
		        		function(result){
		        			$.messager.progress('close');
			        	    toptip('删除失败！');
		        		}
        		);
        	}
        });
	}
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
function centerTip(msg){
	$.messager.alert({  
        title: 'Error',  
        msg: msg
   });
}
/**
 * 失败时提示方法
 */
function errorCallback(result){
	if(result.error){
		centerTip("操作失败！");
	}
}
/**
 * 返回成功后执行方法
 */
function successCallback(result){
	if(result.success){
		$("#dlg_update_operate").dialog("close");          // 关闭dialog  
		$("#dlg_add_operate").dialog("close"); 
		toptip("操作成功");
        $("#datagrid_list_operate").datagrid("reload");    // 加载数据 
	}
}
/**
 * 取消时关闭dailog函数
 */
function closeAddForm(){
	$('#dlg_add_operate').dialog('close');
}
function closeUpdateForm(){
	$('#dlg_update_operate').dialog('close');
}
/**
 * 清除表单中的数据
 */
function clearDate(){
	$('#dlg_add_operate').form("clear");
	$('#dlg_update_operate').form("clear");
}
/**
 * 绑定搜索按钮的的点击事件
 */
function doSearch(){
	$("#datagrid_list_operate").datagrid("load");
}



