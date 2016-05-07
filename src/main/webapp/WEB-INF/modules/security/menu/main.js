/***
 * 菜单管理
 * 孟话然
 */

$(function(){
	
	$("#treegrid_list_menu").treegrid({
		url:Swet.server.cloud+'/menu/serchByParentId',
		method:"get",
		sortName:"id",
		collapsible:"true",
		animate: true,
		idField:"id",
		lines: true,
		treeField:"text",
		fitColumns:"true",
		rownumbers:"true", 
		fit:true,
		columns:[[
			{field:"text",title:"菜单名称",align:"left",width:121},
			{field:"menuUrl",title:"导航地址",align:"left",width:150},
			{field:"note",title:"备注",align:"center",width:166 },
			{field:"createTime",title:"创建时间",align:"center",width:71 },
		    //{field:"addRow",title:"增子菜单",align:"center",width:25,formatter:addR},
			{field:"updataRow",title:"修改",align:"center",width:25,formatter:updateR},
		    {field:"deleteRow",title:"删除",align:"center",width:25,formatter:deleteR},
		]],
		toolbar:"#btn_operate_menu",
		onBeforeLoad:function(row,param){
			var value = $('#searchBox_menu').searchbox('getValue');
			if(!value==""){
				$('#treegrid_list_menu').treegrid('options').url = Swet.server.cloud+'/menu/searchBymenuName/params';
				param.value = value;
			}else{
				$('#treegrid_list_menu').treegrid('options').url = Swet.server.cloud+'/menu/serchByParentId';
			}	
		}
	});	
	
	$("#parentId").combotree({
		url:Swet.server.cloud+'/menu/serchByParentId',
		method:'get',
		onExpand:function(node){
			$('#treegrid_list_menu').treegrid('expand',node.id);
		}
	});
	
})

var temp=1;

/*
 * 快捷操作
 */
/*function addR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-add' onclick='saveMenu("+row.id+")' plain='true'></a>";
}*/

function updateR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='updateMenu("+row.id+")' plain='true'></a>";
}

function deleteR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-del' onclick='deleteMenu("+row.id+")' plain='true'></a>";
}

/*
 * 搜索
 */
function getMenuByParams(){
	$("#treegrid_list_menu").treegrid("load");
}

/*
 * 添加根菜单
 */
function saveRootMenu(){
	$("#menuEdit").dialog("open");
	$('#menuEditForm').form('clear');
	$("#menuUrl").textbox("enable");
	$("#parentId").combotree('reload');
	$("#parentId").combotree("setValue",0);
	$('#parentId').combo('readonly', true);
	$('#treegrid_list_menu').treegrid('clearSelections');
}

/*
 * 打开添加页面
 */
function saveMenu(id){
	/*$('#treegrid_list_menu').treegrid('select',id);
    var row = $('#treegrid_list_menu').treegrid('getSelected');*/ 
	$("#menuEdit").dialog("open");
	$('#menuEditForm').form('clear');
	$("#menuUrl").textbox("enable");
	$("#parentId").combotree('reload');
	/*$("#parentId").combotree("setValue",row.id);*/
	$('#parentId').combo('readonly', false);
	$('#treegrid_list_menu').treegrid('clearSelections');
}	

/*
 * 打开修改页面
 */
function updateMenu(id){
	$('#treegrid_list_menu').treegrid('select',id);
    var row = $('#treegrid_list_menu').treegrid('getSelected'); 
    if (row){
    	if(row.menuUrl == undefined || row.menuUrl == null || row.menuUrl == ""){
    		$("#menuUrl").textbox("disable");
    	}else{
    		$("#menuUrl").textbox("enable");
    	}
		$('#menuEditForm').form('load',row);
		$("#parentId").combotree('reload');
		$('#parentId').combo('readonly', false);
	}
	$('#menuEdit').dialog('setTitle','修改菜单资料');
	$("#menuEdit").dialog("open");
	//清空选择selections
	$('#treegrid_list_menu').treegrid('clearSelections');
}

/*
 * 单条删除菜单
 */
function deleteMenu(id) {
	$('#treegrid_list_menu').treegrid('select',id);
    var row = $('#treegrid_list_menu').treegrid('getSelected'); 
    menuId = row.id;
    $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        if (r){
        	Swet.request.delete(Swet.server.cloud+"/menu/"+menuId," ",function(result){ 
        		$.messager.show({
        			title: "删除结果",
		        	msg: result.msg,
		        	showType: 'slide',
	        	})
	        	$('#treegrid_list_menu').treegrid("reload");
	        	$('#menuEditForm').form("clear");
        	});
        }    	
    }); 
    $('#treegrid_list_menu').treegrid('clearSelections');   
} 

//取消添加或修改
function CancelEdit(){
	$('#menuEdit').dialog('close');
	$('#menuEditForm').form('clear');
}

//确认添加或修改
function saveAndUpdateMenu(){
	//验证
	if(!$('#menuEditForm').form('validate')){
		return;
	}
	//设置menuid和menuname
	var obj = $("#menuEditForm").form("getData");
	obj.menuId=obj.id;
	obj.menuName=obj.text;
	//自动赋值menulevel和判断菜单是否为顶级
	var tempPid = $("#parentId").combotree("getValue");
	if(tempPid == 0){
		obj.parentId=0;
		obj.menuLevel=0;
		saveAndUpdate(obj);
	}else{
		$('#treegrid_list_menu').treegrid('select',tempPid);
		var prow = $('#treegrid_list_menu').treegrid('getSelected');
		if(prow.menuUrl ==  undefined || prow.menuUrl == null || prow.menuUrl == ""){
			obj.parentId=tempPid;
	    	var numberLevel = parseInt(prow.menuLevel)+1;
	    	var stringLevel=numberLevel.toString();
	    	obj.menuLevel=stringLevel;
	        saveAndUpdate(obj);
		}else{
			$.messager.confirm('提示','将清空父菜单的URL,是否继续？',function(r){
				if (r){
					obj.parentId=tempPid;
			    	var numberLevel = parseInt(prow.menuLevel)+1;
			    	var stringLevel=numberLevel.toString();
			    	obj.menuLevel=stringLevel;
			        saveAndUpdate(obj);
			    }else{
			        $("#menuEditForm").form("clear");
			        $('#menuEdit').dialog('close');
			        return;
			    } 	
			});
		}
	}
}

//添加和修改
function saveAndUpdate(obj){
	//获取id,创建基础url
	var url=Swet.server.cloud+"/menu";
	if(obj.id!=null&&obj.id>0){
	//如果存在ID，则改变URL,改变为修改菜单
		url=Swet.server.cloud+"/menu/"+obj.id;
	}else{
	//新增，给与初始state状态为"open"
		obj.state="open";
	}
	//提交返回方法
	Swet.request.save(
		url,  
		obj,
		function(result){
			if(result.success){
				$("#menuEditForm").form("clear");
				$('#menuEdit').dialog('close');
				$("#treegrid_list_menu").treegrid("reload");
				$.messager.show({
					title: "操作结果",
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
				$.messager.show({
					title:'提示',
					msg:'操作失败.',
					showType:'slide',
					timeout:3000,
					style:{
						top:document.body.scrollTop+document.documentElement.scrollTop,
					}
			 }); 
			}
		}
	);
	
}
