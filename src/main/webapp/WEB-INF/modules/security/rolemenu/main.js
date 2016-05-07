/**
 * 模块说明:角色菜单管理功能实现
 * @module rolemenu
 */


$(function(){
	$("#datagrid_list_role").datagrid({
		method:"GET",
		striped:"true",
		sortName:"roleId",
		sortOrder:"desc",
		collapsible:"true",
		idField:"roleId",
		remoteSort:"false",
		singleSelect:"true",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		pageSize:25,
		pageList:[15,25,35],
		fit:true,
		columns:[[
		    {field:"roleCode",title:"角色编码",align:"center",width:50},
			{field:"roleName",title:"角色名称",align:"center",width:50 },
		]],				
		toolbar:"#btn_operate_role",
		//搜索功能
		onBeforeLoad:function(param){
			var value = $('#searchBox_role').searchbox('getValue');
			if(!value==""){
				$('#datagrid_list_role').datagrid('options').url = Swet.server.cloud+'/role/searchByKeyValueLike/params';
				param.value = value;				
			}else{
				$('#datagrid_list_role').datagrid('options').url = Swet.server.cloud+'/role/toPage';
			}
		},
		//动态加载选择的菜单
		onClickRow:function(index,row){
			lookRoleMenu();
		}
	});
	$('#tree_list_menu').tree({
	    url:Swet.server.cloud+'/menu/serchByParentId',
	    checkbox:true,
	    lines:true,
	    method:"get",
	    cascadeCheck:true,
	    onLoadSuccess:function(node,data){
			$('#tree_list_menu').tree('expandAll');	
	    },
	   /* onCheck: function (node, checked) {
            if(checked){
                var parentNode = $("#tree_list_menu").tree('getParent', node.target);
                if(parentNode != null){
                    $("#tree_list_menu").tree('check', parentNode.target);
                }
            }else{
                var childNode = $("#tree_list_menu").tree('getChildren', node.target);
                if(childNode.length > 0){
                    for(var i = 0; i < childNode.length; i++){
                        $("#tree_list_menu").tree('uncheck', childNode[i].target);
                    }
                }
            }
        }*/
	});	
})

/*
 * 修改权限
 */
function updataRoleMenu(){
	var row = $("#datagrid_list_role").datagrid("getSelected");
	if(row==null){
		$.messager.show({
			title:'提示',
			msg:'请选择一个角色.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
		 });
		return;
	}
	$.messager.progress({
		title:'请稍等',
        msg:'系统正在处理。。。'
    }); 
	var menuIds = ""; 	
	var ce = $("#tree_list_menu").tree("getChecked",['checked', 'indeterminate']);
	for(var a=0;a<ce.length;a++){
		if(menuIds==""){
			menuIds=ce[a].id;
		}else{
			menuIds=ce[a].id+","+menuIds;
		}
	}
	var obj=new Object();
	obj.roleId=row.roleId;
	obj.menuIds=menuIds;
	Swet.request.save(
		url=Swet.server.cloud+'/roleMenu/updataRoleMenu',  
		obj,
		function(result){
			$.messager.progress('close');
			if(result.success){
				$.messager.show({
					title:'提示',
					msg:'操作成功.',
					showType:'slide',
					timeout:3000,
					style:{
						top:document.body.scrollTop+document.documentElement.scrollTop,
					}
				 });
        	    lookRoleMenu();
            }
		},
		function(result){
			$.messager.progress('close');
			if(result.error){
				$.messager.alert("提示", "操作失败！", "info"); 
			}
		}
	);
}

/*
 * 搜索role
 */
function getRoleByParams(){
	$("#datagrid_list_role").datagrid("load");
}

/*
 * 取消按钮
 */
function cancelEdit(a){
	lookRoleMenu();
}

/*
 *  查询已有权限
 */
function lookRoleMenu(){
	var row = $("#datagrid_list_role").datagrid("getSelected");
	nodes=$('#tree_list_menu').tree('getChecked',['checked', 'indeterminate']);
	if(nodes.length!=0){
		for(var i=0;i<nodes.length;i++){
			$('#tree_list_menu').tree('uncheck', nodes[i].target);
		}
	}
	if(row!=null){
		var obj=new Object();
		obj.roleId=row.roleId;
		Swet.request.get(
			url=Swet.server.cloud+'/roleMenu/searchMenuIdByRoleId',  
			obj,
			function(result){
		       var node;
		       for(var a=0;a<result.length;a++){
		            node=$('#tree_list_menu').tree('find', result[a]);
		            if(node != null && $('#tree_list_menu').tree('isLeaf',node.target)){
		            	$('#tree_list_menu').tree('check', node.target);
		            }
		       }
			},
			function(result){
			}
		);
	}
}
