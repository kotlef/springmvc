/**
 * org模块的功能
 * 组织信息的增删改查
 */
$(function(){
	loadOrgTree();
	$("#treegrid_list_org").treegrid({
		method:'get',
		iconCls: 'icon-ok',
		rownumbers: true, 
		animate: true,
		collapsible: true,
		fitColumns: true,
		idField: 'id',
		treeField: 'text',
        pagination: true,
		pageSize:25,
		pageList:[15,25,35],
		onContextMenu:showMenu,//右键菜单事件
		toolbar:'#tb_org',
		fitColumns:true,
        singleSelect:true,
		columns:[[
		          {title:'组织名称',field:'text',width:180},
		          {title:'组织编码',field:'orgCode',width:80},
		          {title:'组织类型',field:'orgType',width:80,formatter:translateOrgType},
		          {title:'所属组织ID',field:'parentId',width:100,hidden:'true'},
		          {title:'组织级别',field:'treeLevel',width:100,hidden:'true'},
		          {title:'是否服务站',field:'stationFlag',width:100,hidden:'true',formatter:translateStationFlag},
		          {title:'所属机构',field:'orggroupId',width:80,hidden:'true'},
		          {title:'启用日期',field:'openDate',width:100},
		          {title:'停用日期',field:'closeDate',width:100},
		          {title:'状态',field:'status',width:80,formatter: translateStatus},
		          {title:'备注',field:'note',width:100},
		          {title:'操作列',field:'action',formatter: formatterActionColumns},
		          {field:'cl',checkbox:true}
		      ]],
	      onBeforeLoad:function(row,param){
				var key = $("#searchbox_org").searchbox("getName");
				var value = $("#searchbox_org").searchbox("getValue");
				if(!value == ""){
					$('#treegrid_list_org').treegrid('options').url = Swet.server.cloud+ '/org/searchaByKeyValue/toPage';
					param.key=key;
					param.value=value;
				}else{
					$('#treegrid_list_org').treegrid('options').url = Swet.server.cloud+ '/org/serchByParentId';
				}
			}
	});
});
//翻译是否为服务站点
function translateStationFlag(stationFlag){
	return Swet.dic.translate("STATION_FLAG_"+stationFlag);
}
//翻译是服务的类型
function translateOrgType(orgType){
	return Swet.dic.translate("ORG_TYPE_"+orgType);
}
//翻译状态
function translateStatus(status){
	return Swet.dic.translate("STATUS_"+status);
}

function formatterActionColumns(val,row,index){
	//index:当前行的索引
	   var ctrs = [
	      '<span  title="修改" class="kit-img-btn kit-btn-edit" onclick="openUpdate('+row.id+');" ></span>',
	      '<span  title="删除" class="kit-img-btn kit-btn-del" onclick="deleteRow('+row.id+');" ></span>'
	      ];
	   return ctrs.join('');
}
var flag;
//打开添加页面
function openAdd(){
	loadOrgTree();
	$("#form_add_org").form("clear");
	$("#dlg_add_org").dialog("open");
	$("#dlg_add_org").dialog("setTitle","添加组织");
	flag = "add";
}
//修改页面
function openUpdate(id){
	//加载所组织的树型菜单
	loadOrgTree();
	$('#treegrid_list_org').treegrid('select',id);
	//选中当前行
	var arr=$("#treegrid_list_org").treegrid("getSelections");
	if(arr.length !=1){
		toptip('请选择一行记录进行修改!');
	}else{
		$("#dlg_add_org").dialog("open");
		$("#dlg_add_org").dialog("setTitle","修改组织");
		flag="update";
		var rows = $("#treegrid_list_org").treegrid("getSelections");
		if(rows){
			$("#form_add_org").form("load",rows[0]);
		}
	}
}
/**
 * 保存按钮执行的方法
 * @param val
 */
function saveOrg(val){
	if(!$('#form_add_org').form('validate')){
		return;
	}
	if(flag == "add"){
		saveAdd();
	}
	if(flag == "update"){
		saveUpdate();
	}
}
/**
 * 添加组织
 */
function saveAdd(){
	var obj = $("#form_add_org").form("getData");
	obj.orgid = obj.id;
	obj.orgName = obj.text;
	obj.state="open";
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.save(
			Swet.server.cloud +'/org',  //提交的路径
			obj,
			function(result){
				successCallback(result);
				$.messager.progress('close');
			},
			function(result){
				errorCallback(result);
				$.messager.progress('close');
			}
	);
}
/**
 * 修改组织
 */
function saveUpdate(){
	var obj = $("#form_add_org").form("getData");
	obj.orgid = obj.id;
	obj.orgName = obj.text;
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.update(
			Swet.server.cloud +'/org/'+obj.orgid,  //提交的路径
			obj,
			function(result){
				$.messager.progress('close');
				successCallback(result);
			},
			function(result){
				$.messager.progress('close');
				errorCallback(result)
			}
	);
}
function deleteRow(id){
	//选中当前行
	$('#treegrid_list_org').treegrid('select',id);
	var rows=$("#treegrid_list_org").treegrid("getSelections");
	deleteOrg(rows);
}
function deleteRows(){
	var rows = $('#treegrid_list_org').treegrid('getChecked');
	deleteOrg(rows);
}
/**
 *删除或批量删除组织
 */
function deleteOrg(rows){
    if (rows.length==0) { 
    	toptip("请选择要删除的行！");
        return; 
    }else{
    	var  orgids="";
        for (var i = 0; i < rows.length; i++) {
        	if(orgids==""){
        		orgids=rows[i].id;
        	}else{
        		orgids=rows[i].id+","+orgids;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		$.messager.progress({
        			text:'数据删除中,请稍候.....'
        		});
        		Swet.request.delete(Swet.server.cloud+"/org/"+orgids,orgids,function(result){ 
        			$.messager.progress('close');
        			toptip('删除成功!');
        			$('#dlg_add_org').dialog('close');
        			var orgRow= $("#treegrid_list_org").treegrid("getSelected");
        			var orgid= 0;
        			$("#treegrid_list_org").treegrid("reload",orgid);
        		},
        		function(result){ 
        			$.messager.progress('close');
    				errorCallback(result);
    				$("#treegrid_list_org").treegrid("reload")
        		});
        	}
        });
    }  
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
		//var orgid= (result.rows!=null) ?result.rows:0;
		$("#treegrid_list_org").datagrid("clearChecked");
		$("#treegrid_list_org").datagrid("clearSelections");	
		var	orgid = 0;
		$("#treegrid_list_org").treegrid("reload",orgid);
	}
}
/**
 * 返回成功后执行方法
 */
function successCallback(result){
	if(result.success){
		$('#dlg_add_org').dialog('close');
		//var orgRow= $("#treegrid_list_org").treegrid("getSelected");
		$("#treegrid_list_org").datagrid("clearChecked");
		$("#treegrid_list_org").datagrid("clearSelections");
	//	var orgid= (result.rows!=null) ?result.rows:0;
		var orgid=0;
		$("#treegrid_list_org").treegrid("reload",orgid);
	}
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
/**
 * 条件查询
 */
function doSearch(){
	 $("#treegrid_list_org").treegrid("reload"); 
}
/**
 * 取消时关闭dailog函数
 */
function closeForm(){
	$('#dlg_add_org').dialog('close');
}
/**
 * 清除表单中的数据
 */
function clearDate(){
	$('#dlg_add_org').form("clear");
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
//加载所组织的树型菜单
function loadOrgTree(){
	$("#combotree_add_parentId").combotree({
		method:'get',
		url:Swet.server.cloud+'/org/serchByParentId',
		valueField:'id',
		textField:'text',
		prompt:'请选择'
	});
}
//离职时间设置
function setCloseDate(newDate,oldDate){
	var closeDate = new Date(newDate);
    $('#dtb_closeDate').datebox().datebox('calendar').calendar({
        validator: function(date){
            return closeDate<=date;
        }
    });
}
