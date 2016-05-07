/**
 * 翻译
 */
function translateRestype(v){
	return Swet.dic.translate("RES_TYPE_"+v);
}

/**
 * 修改删除快捷
 */
function updateE(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='updateResDilog("+index+")' plain='true'></a>";
}

function deleteE(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-del' onclick='deleteResDilog("+index+")' plain='true'></a>";
}

/**
 * 回显资源
 */
function getRes(rowData,rowIndex){
	$('#datagrid_res').datagrid({
		url:Swet.server.cloud+"/orgres/searchByOrgIdAll/"+rowData.id,
		method:"get",
		fit:true,
		fitColumns:true,
		idField:"resId",
		columns:[[
			{field:"resName",title:"资源名称",align:"center",width:75},
			{field:"resType",title:"资源类型",align:"center",width:75,formatter:translateRestype},
			{field:"updataRow",title:"修改",align:"center",width:30,formatter:updateE},
		    {field:"deleteRow",title:"删除",align:"center",width:30,formatter:deleteE},
			
		]],
	})
}

/**
 * 取消资源弹窗
 */
function cancelResDilog(){
	$('#resEditForm').form('clear');
	$("#aditRes").dialog("close");
}

/**
 * 添加资源弹窗
 */
function addResDilog(index){
	$('#resEditForm').form('clear');
	$('#aditRes').dialog('setTitle','添加资源');
	$("#aditRes").dialog("open");
}

/**
 * 修改资源弹窗
 */
function updateResDilog(index){
	$.messager.progress({
		title:'请稍后',
		msg:'正在查询资源信息...'
	});
	$('#datagrid_res').datagrid('selectRow',index);
    var row = $('#datagrid_res').datagrid('getSelected');
    var resId = row.resId;
    if(row){
    	Swet.request.get(
    		Swet.server.cloud+"/orgres/findByResIdToOrg/"+resId, 
    		"",
    		function(result){
    			if(result.success){
    				$.messager.progress('close');
        			$('#resEditForm').form('clear');
        			$("#oIdr").combotree('setValues',result.rows);
        			$('#resEditForm').form('load',row);
        			$('#aditRes').dialog('setTitle','修改资源');
        	    	$("#aditRes").dialog("open");
    			}else{
    				$.messager.progress('close');
    				$('#resEditForm').form('clear');
    				$('#resEditForm').form('load',row);
    				$('#aditRes').dialog('setTitle','修改资源');
    				$("#aditRes").dialog("open");
    			}
    		}
    	);
    }
}

/**
 * 删除资源
 */
function deleteResDilog(index){
	$('#datagrid_res').datagrid('selectRow',index);
    var row = $('#datagrid_res').datagrid('getSelected');
    var resId = row.resId;
    $.messager.confirm('提示','将删除所有组织下的该资源。若仅删除该组织下的该资源，请使用修改！',function(r){
    	if (r){
    		$.messager.progress({
    			title:'请稍后',
    			msg:'正在删除数据...'
    		});
    		Swet.request.delete(Swet.server.cloud+"/orgres/deleteAll/"+resId," ",function(result){ 
    			$.messager.progress('close');
    			$('#datagrid_res').datagrid('reload');
	    	    $.messager.show({
					title:'提示',
					msg:result.msg,
					showType:'slide',
					timeout:3000,
					style:{
						top:document.body.scrollTop+document.documentElement.scrollTop,
					}
	    	    });
    		});
    	}
    });
}

/**
 * 保存资源
 */
function saveOrUpdataRes(){
	$.messager.progress({
		title:'请稍后',
		msg:'正在整理和保存数据...'
	});
	var obj = $('#resEditForm').form('getData');
	if($("#resId").val() == ""){
		url = Swet.server.cloud+"/orgres/saveAll";
	}else{
		url = Swet.server.cloud+"/orgres/updateAll";
	}
	Swet.request.save(
			url,
			obj,
			function(result){
				$.messager.progress('close');
				if(result.success){
					$.messager.show({
						title: "操作成功",
				        msg: result.msg,
				        showType: 'slide',
				        timeout:3000,
				        style:{
				    		top:document.body.scrollTop+document.documentElement.scrollTop,
				    	}
			        });
					$('#datagrid_res').datagrid('reload');
					$("#aditRes").dialog("close");
				}else{
					$("#aditRes").dialog("close");
					$.messager.alert("提示", result.msg, "info");
				}
			}
		);
}
