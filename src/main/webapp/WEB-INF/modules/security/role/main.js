/*
 * role模块的功能
 * 角色信息的增删改查
 *
 */
$(function(){
	$("#homepageId").combobox({
		method:"GET",
		editable:false,
		prompt:'请选择',
		url:Swet.server.cloud+'/homepage',
		valueField:'homepageId',
		textField:'homepageName'
	});
	$("#datagrid_list_role").datagrid({
		method:"GET",
		striped:"true",
		sortName:"roleId",
		sortOrder:"desc",
		loadMsg:"数据加载中...",
		idField:"roleId",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		pageSize:25,
		pageList:[15,25,35],
		fit:true,
		columns:[[
		    {field:"roleCode",title:"角色编码",align:"center",width:50},
		    {field:"roleName",title:"角色名称",align:"center",width:87 },
		    {field:"roleType",title:"角色类型",align:"center",width:50,formatter:translateRtype },
		    {field:"homePageName",title:"主页",align:"center",width:50},
		    {field:"status",title:"状态",align:"center",width:50,formatter:translateStype },
		    {field:"note",title:"备注",align:"center",width:110 },
		    {field:"modifyTime",title:"最后修改时间",align:"center",width:81 },
		    {field:"updataRow",title:"修改",align:"center",width:30,formatter:updateR},
		    {field:"deleteRow",title:"删除",align:"center",width:30,formatter:deleteR},
		    {field:"roleId",title:"角色ID",align:"center",width:81,checkbox:true },
		]],
		toolbar:"#btn_operate_role",	
		onBeforeLoad:function(param){
			var value = $('#searchBox_role').searchbox('getValue');
			if(!value==""){
				$('#datagrid_list_role').datagrid('options').url = Swet.server.cloud+'/role/searchByKeyValueLike/params';
				param.value = value;				
			}else{
				$('#datagrid_list_role').datagrid('options').url = Swet.server.cloud+'/role/findAllAndHpage/ToPage';
			}
		}
	});
})	

var checkRoleCodeUrl = Swet.server.cloud+'/role/checkRoleCode';
var checkRoleCode = 'roleCode';//roleCode验证

/*
 * 翻译字段
 */
function translateRtype(v){
	return Swet.dic.translate("ROLE_TYPE_"+v);
}
function translateStype(v){
	return Swet.dic.translate("STATUS_"+v);
}

/*
 * 修改删除快捷键
 */
function updateR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='updateRole("+index+")' plain='true'></a>";
}

function deleteR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-del' onclick='deleteRole("+index+")' plain='true'></a>";
}

/*
 * 搜索role
 */
function getRoleByParams(){
	$("#datagrid_list_role").datagrid("load");
}

/*
 * 打开添加页面
 */
function saveRole(){
	$("#roleEdit").dialog("open");
	$('#roleEditForm').form('clear');
	$('#roleCode').textbox("enableValidation");
	$('#roleCode').textbox('readonly',false);
}

/*
*打开修改页面
*/
function updateRole(index){
	$('#datagrid_list_role').datagrid('selectRow',index);
    var row = $('#datagrid_list_role').datagrid('getSelected'); 
	if (row){
		$('#roleEditForm').form('load',row);
		$('#roleCode').textbox("disableValidation");
		$('#roleCode').textbox('readonly',true);
	}
	$('#roleEdit').dialog('setTitle','修改菜单资料');
	$("#roleEdit").dialog("open");
}

/*
 * 取消按钮
 */
function cancelEdit(){
	$('#roleEdit').dialog('close');
	$('#roleEditForm').form('clear');
	$("#datagrid_list_role").datagrid("clearSelections");
}

/**
 * 批量删除角色
 */
function deleteRoles(){
	var rows = $('#datagrid_list_role').datagrid('getChecked');
	if(rows.length==0){
		$.messager.show({
			title:'提示',
			msg:'请选择要删除的数据.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
		}); 
        return;
	}else{
		var ROLEID="";
	    for (var i = 0; i < rows.length; i++) {
	    	if(ROLEID==""){
	        	ROLEID=rows[i].roleId;
	        }else{
	        	ROLEID=rows[i].roleId+","+ROLEID;
			}
	    }
	    deleteRo(ROLEID);
	}

}

/*
*删除role
*/
function deleteRole(index){
	$('#datagrid_list_role').datagrid('selectRow',index);
	var row = $('#datagrid_list_role').datagrid('getSelected');
	deleteRo(row.roleId);
}

/*
 * 删除
 */
function deleteRo(roleIds){
	$.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        if (r){
        	Swet.request.delete(Swet.server.cloud+"/role/"+roleIds," ",function(result){ 
        	    $.messager.show({
    				title:'提示',
    				msg:result.msg,
    				showType:'slide',
    				timeout:3000,
    				style:{
    					top:document.body.scrollTop+document.documentElement.scrollTop,
    				}
    		 });
        	    $("#datagrid_list_role").datagrid("reload");
        		});
        	}
        });
	$("#datagrid_list_role").datagrid("clearSelections");
}

/*
*添加或修改role
*/
function saveAndUpdateRole(){
	//验证
	if(!$('#roleEditForm').form('validate')){
		return;
	}
	//得到表单数据
	var obj = $("#roleEditForm").form("getData");
	var roleId=$("#roleId").val();
	var url=Swet.server.cloud+"/role";
	if(roleId!=null&&roleId>0){//存在roleid改为修改role功能
		 url=Swet.server.cloud+"/role/"+roleId;
	 }
	Swet.request.save(
			url,  
			obj,
			function(result){
				if(result.success){
					$("#roleEditForm").form("clear");
					$('#roleEdit').dialog('close');
					$("#datagrid_list_role").datagrid("reload");
					$.messager.show({
		        	    title: "操作成功",
		        	    msg: result.msg,
		        	    showType: 'slide',
		        	    timeout:3000,
		        	    style:{
		    				top:document.body.scrollTop+document.documentElement.scrollTop,
		    			}
	        	    }) 
				}
			},
			function(result){
				if(result.error){
					$.messager.alert("提示", "操作失败！", "info"); 
				}
			}
	);
	
}