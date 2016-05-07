
$(function(){
	$("#treegrid_list_org").treegrid({
		method:'get',
		iconCls: 'icon-ok',
		rownumbers: true, 
		animate: true,
		collapsible: true,
		fit:true,
		fitColumns: true,
		idField: 'id',
		treeField: 'text',
        pagination: true,
		pageSize:25,
		pageList:[15,25,35],
		fitColumns:true,
		onContextMenu:showMenu,//右键菜单事件
		columns:[[
		          {title:'组织内码',field:'id',width:100,hidden:'true'},
		          {title:'组织名称',field:'text',width:200},
		          {title:'组织类型',field:'orgType',width:80,formatter:translateOrgType},
		          {title:'组织级别',field:'treeLevel',width:100,hidden:'true'},
		          {title:'所属机构',field:'orggroupId',width:80,hidden:'true'},
		          {title:'记录建立时间',field:'createTime',width:100,hidden:'true'},
		          {title:'最后修改时间',field:'modifyTime',width:100,hidden:'true'}
		      ]],
		toolbar:"#btn_operate_org",
		onBeforeLoad:function(param){
			var key = $("#searchbox_org").searchbox("getName");
			var value = $("#searchbox_org").searchbox("getValue");
			if(!value == ""){
				$('#treegrid_list_org').treegrid('options').url = Swet.server.cloud+ '/org/searchaByKeyValue/toPage';
				param.key=key;
				param.value=value;
			}else{
				$('#treegrid_list_org').treegrid('options').url = Swet.server.cloud+ '/org/serchByParentId';
			}
		},
		onClickRow:function(row){
			$('#datagrid_list_orgres').datagrid('unselectAll');
			$('#datagrid_list_orgres').datagrid('options').url = Swet.server.cloud+'/orgres/searchResByOrgId/'+row.id;
			$('#datagrid_list_orgres').datagrid('reload');
		}
	});
	
	$("#datagrid_list_orgres").datagrid({
		method:"GET",
		nowrap:"false",
		striped:"true",
		sortName:"resId",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"resId",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		fit:true,
		columns:[[
		          {field:"resId",title:"",align:"left",width:50,checkbox:true},
		          {field:"itemId",title:"资源ID",align:"left",width:40},
				  {field:"resType",title:"资源类型",align:"left",width:50,formatter:translateResType},
				  {field:"note",title:"备注",align:"left",width:80 },
				  {field:"createTime",title:"建立时间",align:"left",width:50 }
				]],
		toolbar:"#btn_operate_res",
	});
	
})
/**
 * 字典表翻译
 */
function translateResType(v){
	return Swet.dic.translate("RES_TYPE_"+v);
}
//翻译是服务的类型
function translateOrgType(orgType){
	return Swet.dic.translate("ORG_TYPE_"+orgType);
}
/**
 * 右键时触发事件
 */
var showMenu=function(e, row){
	e.preventDefault();
	//清除所有选中项
	$('#treegrid_list_org').treegrid('unselectAll');
	// 查找节点
	$('#treegrid_list_org').treegrid('select', row.id);
	// 显示快捷菜单
	$('#menu_list_org').menu('show', {
		left: e.pageX,
		top: e.pageY
	});

	}
/*
 * 搜索res
 */
function getresByParams(){
	$("#datagrid_list_orgres").datagrid("load");
}
/*
 *查询组织数据 
 */
function doSearch(){
	 $("#treegrid_list_org").treegrid("reload"); 
}
/*
 * 添加资源
 */
function addRes(){
	var selRows = $('#treegrid_list_org').treegrid("getSelections");
	var row=$('#treegrid_list_org').treegrid("getSelected");
	if (selRows.length==0) { 
		 $.messager.alert("提示", "添加资源前，请选择一个组织 ！", "info"); 
		 return; 
	}
	document.getElementById("orgid").value=row.id;
    $("#dialog_edit_res").dialog("open");
	$("#datagrid_list_res").datagrid({
		url:Swet.server.cloud+'/res/toPage',
		method:"GET",
		nowrap:"false",
		striped:"true",
		sortName:"resId",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"resId",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		fit:true,
		columns:[[
		          {field:"resId",title:"",align:"left",width:50,checkbox:true},
		          {field:"itemId",title:"资源ID",align:"left",width:30},
				  {field:"resType",title:"资源类型",align:"left",width:20,formatter:translateRtype},
				  {field:"note",title:"备注",align:"left",width:50 },
				  {field:"createTime",title:"建立时间",align:"left",width:50 }
				]]
	});
}

function translateRtype(v){
	return Swet.dic.translate("RES_TYPE_"+v);
}
//移除组织资源
function removeRes(){
    var rows = $('#datagrid_list_orgres').datagrid('getSelections');
    var orgRow=$('#treegrid_list_org').treegrid("getSelected");
    if (rows.length<1){ 
    	$.messager.alert("提示", "请选择要删除的数据！", "info");  
        return; 
    }else{
    	var orgResIds="";
        for (var i = 0; i < rows.length; i++) {
        	if(orgResIds==""){
        		orgResIds=orgRow.id+","+rows[i].resId;
        	}else{
        		orgResIds=orgResIds + ","+rows[i].resId;
        	}
        	
        }
    	$.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
    		if (r){
    			$.messager.progress({
    				text:'数据删除中,请稍候.....'
    			});
	        	Swet.request.delete(
	        			Swet.server.cloud+"/orgres/deleteResById/"+orgResIds,
	        			"",
	        			function(result){
	        				$.messager.progress('close');
	        				if(result.success){
	        					$("#datagrid_list_orgres").datagrid("reload");
	        				}
	        			},
	        			function(result){
	        				$.messager.progress('close');
	        				if(result.success){
	        					$("#datagrid_list_orgres").datagrid("reload");
	        				}
	        			}
	        	);
    		}
       });
    }
}

/*
 * 保存组织资源
 */
function saveRes(){
	var rows = $("#datagrid_list_res").datagrid("getSelections");
	var selRows = $('#datagrid_list_res').datagrid("getRows");
	if (rows.length==0) { 
		 $.messager.alert("提示", "请选择要添加的资源 ！", "info"); 
		 return; 
	}
	var orgid=$("#orgid").val();
	var  resids="";
    for (var i = 0; i < rows.length; i++) {
    	if(resids==""){
    		resids=rows[i].resId;
    	}else{
    		resids=rows[i].resId+","+resids;
    	}
    }
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.save(
			url=Swet.server.cloud+'/orgres/saveMore',{
				orgid:orgid,
				resids:resids
    		},
			function(result){
    			$.messager.progress('close');
				if(result.success){
					toptip("保存成功！");
	            	$('#dialog_edit_res').dialog("close");
	            	$('#datagrid_list_res').datagrid('clearSelections');
	            	$('#datagrid_list_orgres').datagrid('reload');
	            }
			},
			function(result){
				$.messager.progress('close');
				$('#datagrid_list_orgres').datagrid('reload');
			}
		);	
}
//提示(页面顶部居中位置)
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
function clearDate(){
	$("#datagrid_list_res").datagrid('clearSelections');
}