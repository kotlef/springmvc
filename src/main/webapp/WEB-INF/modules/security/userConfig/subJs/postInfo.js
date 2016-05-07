/**
 * 远程验证roleCode
 */
var checkRoleCodeUrl = Swet.server.cloud+'/role/checkRoleCode';
var checkRoleCode = 'roleCode';//roleCode验证

/**
 * 添加角色面板
 */
function addRoleTab(rowData,rowIndex){
	if ($('#tabs_post').tabs('exists', rowData.text)){
		$('#tabs_post').tabs('select', rowData.text);
	} else {
		var orgid = rowData.id;
		$('#tabs_post').tabs('add',{
			title:rowData.text+
			"&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;-"+orgid,
			content:"<table id='datagrid_role"+orgid+"' border='0' class='easyui-datagrid'></table>",
			closable:false
		});
		
		$('#datagrid_role'+orgid).datagrid({
			url:Swet.server.cloud+"/orgrole/searchByOrgIdAll/"+orgid,
			method:"get",
			fit:true,
			fitColumns:true,
			singleSelect:true,
			checkOnSelect:false,
			selectOnCheck:false,
			idField:"roleId",
			columns:[[
				{field:"roleName",title:"角色名称",align:"center",width:75},
				{field:"roleCode",title:"角色编码",align:"center",width:75},
				{field:"roleType",title:"角色类型",align:"center",width:75,formatter:translateRoletype},
				{field:"homePageName",title:"主页名称",align:"center",width:75},
				{field:"updataRow",title:"修改",align:"center",width:30,formatter:updateR},
			    {field:"deleteRow",title:"删除",align:"center",width:30,formatter:deleteR},
				{field:"roleId",title:"roleId",align:"center",width:30,checkbox:true},
			]],
			onLoadSuccess:function(data){
				$(".datagrid-header-check").attr("disabled","disabled");
				if(addOrUp == 0 ||addOrUp == 2){
					//回显岗位
					Swet.request.get(
						Swet.server.cloud+"/post/findRoleidByUidOrgid/"+impoUID+"/"+orgid, 
						"",
						function(result){
							if(result.success){
								var allrows = $('#datagrid_role'+orgid).datagrid("getRows");
								for(var a=0;a<result.rows.length;a++){
									for(var b=0;b<allrows.length;b++){
										if(allrows[b].roleId == result.rows[a]){
											var index = $('#datagrid_role'+orgid).datagrid("getRowIndex",allrows[b]);
											$('#datagrid_role'+orgid).datagrid("checkRow",index);
										}
									}
								}
							}
						}
					);
				}
			}
		});
	}
}

/**
 * 删除角色面板
 */
function deleteRoleTab(rowData,rowIndex){
	var title = rowData.text+"&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;-"+rowData.id;
	if ($('#tabs_post').tabs('exists', title)){
		$('#tabs_post').tabs('close', title);
	}
}

/**
 * 选择角色面板
 */
function selectRoleTab(rowData,rowIndex){
	var title = rowData.text+"&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;-"+rowData.id;
	if ($('#tabs_post').tabs('exists', title)){
		$('#tabs_post').tabs('select', title);
	}
}

/**
 * 翻译
 */
function translateRoletype(v){
	return Swet.dic.translate("ROLE_TYPE_"+v);
}

/**
 * 修改删除快捷
 */
function updateR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='updateRoleDilog("+index+")' plain='true'></a>";
}

function deleteR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-del' onclick='deleteRoleDilog("+index+")' plain='true'></a>";
}

/**
 * 回显组织
 */
function getPost(){
	$.messager.progress({
		title:'请稍后',
		msg:'正在查询数据...'
	});
	Swet.request.get(
		Swet.server.cloud+"/post/findOrgidByUid/"+impoUID, 
		"",
		function(result){
			if(result.success){
				$.messager.progress('close');
				var index = getTabsIndex();
				$('#indexTabs').tabs('select',index+1);
				$("#indexTabs").tabs("disableTab",index);
				$('#indexTabs').tabs('enableTab',index+1);
				//展开全部节点
				var roots = $("#treegrid_org_post").treegrid("getRoots");
				if (roots.length > 0) {
					$.each(roots, function (i, item) {
						$("#treegrid_org_post").treegrid("expandAll", item.id);
					});
				}
				for(var a=0;a<result.rows.length;a++){
					$("#treegrid_org_post").treegrid("checkRow",result.rows[a]);
				}
			}else{
				$.messager.progress('close');
				$.messager.alert("提示", result.msg, "info"); 
			}
		}
	);
	
}

/**
 * 保存岗位 
 */
function saveOrUpdatapost(){
	var temporgrole = "";
	var tabs = $('#tabs_post').tabs('tabs');
	$.messager.progress({
		title:'请稍后',
		msg:'正在整理和保存数据...'
	});
	for(var a=0;a<tabs.length;a++){
		var divStr = tabs[a].panel("options").content;
		var divid = divStr.split("id='")[1].split("'")[0];
		var orgid = divid.split("datagrid_role")[1].split("'")[0];
		var cked = $("#"+divid).datagrid("getChecked");
		for(var b=0;b<cked.length;b++){
			var roleid = cked[b].roleId;
			if(temporgrole == ""){
				temporgrole = orgid+"-"+roleid;
			}else{
				temporgrole = temporgrole +","+ orgid+"-"+roleid;
			}
		}
	}
	var obj = new Object();
	obj.uid = impoUID;
	//obj.uid = 12345712;
	obj.orgRoles = temporgrole;
	Swet.request.save(
		Swet.server.cloud+"/post/saveAll", 
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
				$('#indexSaveButton').hide();
				$('#indexNextButton').hide();
				$('#indexUpButton').hide();
				$('#indexFinalButton').show();
				var index = getTabsIndex();
				$('#indexTabs').tabs('select',index+1);
				$("#indexTabs").tabs("disableTab",index);
				$('#indexTabs').tabs('enableTab',index+1);
			}else{
				$.messager.alert("提示", result.msg, "info"); 
			}
		}
	);
}

/**
 * 取消角色弹窗
 */
function cancelRoleDilog(){
	$('#roleEditForm').form('clear');
	$("#aditRole").dialog("close");
}

/**
 * 添加角色弹窗
 */
function addRoleDilog(){
	$('#roleEditForm').form('clear');
	$('#aditRole').dialog('setTitle','添加角色');
	$("#aditRole").dialog("open");
}

/**
 * 修改角色弹窗
 */
function updateRoleDilog(index){
	$.messager.progress({
		title:'请稍后',
		msg:'正在查询角色信息...'
	});
	var tab  = $('#tabs_post').tabs('getSelected');
	var tabtitle = tab.panel("options").title;
	var orgid = tabtitle.split("-")[1];
	$('#datagrid_role'+orgid).datagrid('selectRow',index);
    var row = $('#datagrid_role'+orgid).datagrid('getSelected');
    var roleId = row.roleId;
    if(row){
    	Swet.request.get(
    		Swet.server.cloud+"/orgrole/findByRoleIdToOrg/"+roleId, 
    		"",
    		function(orgresult){
    			if(orgresult.success){
    				Swet.request.get(
    					Swet.server.cloud+"/roleMenu/searchMenuIdByRoleId",
    					"roleId="+roleId,
    					function(menuresult){
    						$.messager.progress('close');
        					$('#roleEditForm').form('clear');
        					$("#oId").combotree('setValues',orgresult.rows);
        					$("#meId").combotree('setValues',menuresult);
        					$('#roleEditForm').form('load',row);
        					$('#aditRole').dialog('setTitle','修改角色');
        	    			$("#aditRole").dialog("open");
    					}
    				);
    			}else{
    				$.messager.progress('close');
    				$('#roleEditForm').form('clear');
    				$('#roleEditForm').form('load',row);
    				$('#aditRole').dialog('setTitle','修改角色');
    				$("#aditRole").dialog("open");
    			}
    		}
    	);
    }
}

/**
 * 删除角色
 */
function deleteRoleDilog(index){
	var tab  = $('#tabs_post').tabs('getSelected');
	var tabtitle = tab.panel("options").title;
	var orgid = tabtitle.split("-")[1];
	$('#datagrid_role'+orgid).datagrid('selectRow',index);
    var row = $('#datagrid_role'+orgid).datagrid('getSelected');
    var roleId = row.roleId;
	$.messager.confirm('提示','将删除所有组织下的该角色。若仅删除该组织下的该角色，请使用修改！',function(r){
    	if (r){
    		$.messager.progress({
    			title:'请稍后',
    			msg:'正在删除数据...'
    		});
    		Swet.request.delete(Swet.server.cloud+"/role/deleteAll/"+roleId," ",function(result){ 
    			$.messager.progress('close');
    			$('#datagrid_role'+orgid).datagrid('reload');
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
 * 保存或更新角色
 */
function saveOrUpdateRole(){
	$.messager.progress({
		title:'请稍后',
		msg:'正在整理和保存数据...'
	});
	var obj = $('#roleEditForm').form('getData');
	if($("#roleId").val() == ""){
		url = Swet.server.cloud+"/role/saveAll";
	}else{
		url = Swet.server.cloud+"/role/updateAll";
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
				var tab  = $('#tabs_post').tabs('getSelected');
				var tabtitle = tab.panel("options").title;
				var orgid = tabtitle.split("-")[1];
				$('#datagrid_role'+orgid).datagrid('reload');
				$("#aditRole").dialog("close");
			}else{
				$("#aditRole").dialog("close");
				$.messager.alert("提示", result.msg, "info");
			}
		}
	);
}