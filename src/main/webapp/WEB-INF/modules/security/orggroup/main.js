/**
 * 机构信息
 */
var checkorggroupNameUrl = Swet.server.cloud + '/orggroup/checkorggroupName';checkorggroupName='text';//验证机构名称的唯一
var checkorggroupIdUrl = Swet.server.cloud + '/orggroup/checkorggroupName';checkorggroupId='id';//验证机构编码的唯一
$(function(){
	loadOrggrouTree();
	$('#table_list_orggroup').treegrid({
		onContextMenu:showMenu,//右键菜单事件
		iconCls: 'icon-ok',
		rownumbers: true, 
		animate: true,
		collapsible: true,
		fitColumns: true,
		method: 'post',
		idField: 'id',
		treeField: 'text',
        pagination: true,
        loadMsg:'数据加载中...',
		fitColumns:true,
        singleSelect:true,
		columns:[[	
		          	{title:'节点',field:'state',width:20,hidden:'true'},
		          	{title:'机构名称',field:'text',width:120},
		          	{title:'机构编码',field:'id',width:20},
		          	{title:'机构类型',field:'orggroupType',width:20,formatter:translateOrggrouType},
		          	{title:'所属机构', field:'parentId',width:30,hidden:'true'},
		          	{title:'机构级别',field:'treeLevel',width:30,hidden:'true'},
		          	{title:'启用日期',field:'openDate',width:30,hidden:'true'},
		          	{title:'停用日期',field:'closeDate',width:30,hidden:'true'},
		          	{title:'状态',field:'status',width:20,formatter:translateStatus},
		          	{title:'记录建立时间',field:'createTime',width:30,hidden:'true'},
		          	{title:'最后修改时间',field:'modifyTime',width:30,hidden:'true'},
		          	{title:'备注',field:'note',width:40},
		          	{title:'操作列',field:'action',formatter: formatterActionColumns},
		          	{field:'cl',checkbox:true}
		          ]],
		toolbar:'#tb_orggroup',
	    onBeforeLoad:function(row,param){
			var key = $("#searchbox_orggroup").searchbox("getName");
			var value = $("#searchbox_orggroup").searchbox("getValue");
			if(!value == ""){
				$('#table_list_orggroup').treegrid('options').url = Swet.server.cloud+ '/orggroup/searchBykeyValue/toPage';
				param.key=key;
				param.value=value;
			}else{
				$('#table_list_orggroup').treegrid('options').url = Swet.server.cloud+ '/orggroup/serchByParentId';
			}
		} 
	});
});
//翻译状态
function translateStatus(status){
	return Swet.dic.translate("STATUS_"+status);
}
function translateOrggrouType(orggroupType){
	return Swet.dic.translate("ORGGROUP_TYPE_"+orggroupType);
}

function formatterActionColumns(val,row,index){
	//index:当前行的索引
	   var ctrs = [
	      '<span  title="修改" class="kit-img-btn kit-btn-edit" onclick="openUpdate('+row.id+');" ></span>',
	      '<span  title="删除" class="kit-img-btn kit-btn-del" onclick="deleteRow('+row.id+');" ></span>'
	      ];
	   return ctrs.join('');
}

function updt(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='openUpdate("+row.id+")' plain='true'></a>";
}
function dlt(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-del' onclick='deleteRow("+row.id+")' plain='true'></a>";
}


var flag; //判断调用添加方法还是修改方法
var checkOrggroupIdUrl = Swet.server.cloud+'/orggroup/checkOrggroupId';
var checkOrggroupId = 'id';
//打开添加界面
function openAdd(){
	openCheck();
	loadOrggrouTree();
	$('#txb_add_orggroup_orggroupId').numberbox({
		editable:true,
		disabled:false,
	});
	$("#dlg_add_orggroup").dialog("open");
	$("#dlg_add_orggroup").dialog("setTitle","添加机构");
	flag = "add";
}
//打开修改界面
function openUpdate(id){
	colsedCheck();
	loadOrggrouTree();
	$('#txb_add_orggroup_orggroupId').numberbox({
		editable:false,
		disabled:true
	});
	clearDate();
	//选中当前行
	$('#table_list_orggroup').treegrid('select',id);
	var arr = $('#table_list_orggroup').treegrid('getSelections');
	if(arr.length != 1){
		toptip('请选择一行记录进行修改!');
	}else{
		$("#dlg_add_orggroup").dialog("open");
		$("#dlg_add_orggroup").dialog("setTitle","修改机构");
		var row= $("#table_list_orggroup").treegrid("getSelections");
		if(row){
			$("#form_add_orggroup").form("load",row[0]);
		}
		flag="update";
	}
}

/**
 * 保存方法
 */
function saveOrgGroup(){
	if(!$('#form_add_orggroup').form('validate')){
		return;
	}
	if(flag =="add"){
		saveAdd();
	}else if(flag=="update"){
		saveUpdate();
	}
}
/**
 * 添加机构的方法
 */
function saveAdd(){
	var addObj=$("#form_add_orggroup").form("getData");
	var addRow= $("#table_list_orggroup").treegrid("getSelected");
	addObj.orggroupId = addObj.id;
	addObj.orggroupName = addObj.text;
	addObj.state="open";
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.save(
			Swet.server.cloud + '/orggroup',  //提交的路径
			addObj,
			function(result){
				$.messager.progress('close');
				successCallback(result)
			},
			function(result){
				$.messager.progress('close');
				errorCallback(result)
			}
	);
}

/**
 * 修改机构的方法
 */
function saveUpdate(){
	var row= $("#table_list_orggroup").treegrid("getSelected");
	var obj=$("#form_add_orggroup").form("getData");
	obj.orggroupName = obj.text;
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.update(
			Swet.server.cloud + '/orggroup/' + row.id,  //提交的路径	
			obj,
			function(result){
				$.messager.progress('close');
				successCallback(result)
			},
			function(result){
				$.messager.progress('close');
				errorCallback(result)
			}
	);
}
/**
 * 成功执行的方法
 */
function successCallback(result){
	if(result.success){
		$('#dlg_add_orggroup').dialog('close');
		//var orggroupId= (result.rows!=null) ? result.rows:0;
		$("#table_list_orggroup").datagrid("clearChecked");
		$("#table_list_orggroup").datagrid("clearSelections");
		var orggroupId=0;
		$("#table_list_orggroup").treegrid("reload",orggroupId);
	}
}
/**
 *失败时执行的方法
 */
function errorCallback(result){
	if(result.error){
		$("#form_add_orggroup").form("close");
		$.messager.alert({  
            title: 'Error',  
            msg: '操作失败' 
       });
	   $("#table_list_orggroup").datagrid("clearChecked");
	   $("#table_list_orggroup").datagrid("clearSelections");
       var orggroupId=0;
	   $("#table_list_orggroup").treegrid("reload",orggroupId);
	}
}

function deleteRow(id){
	//选中当前行
	$('#table_list_orggroup').treegrid('select',id);
	var rows = $('#table_list_orggroup').treegrid('getSelections');
	deleteorggroup(rows)
}
function deleteRows(){
	var rows = $('#table_list_orggroup').treegrid('getChecked');
	deleteorggroup(rows)
}
/**
 * 根据id删除机构
 */
function deleteorggroup(rows) {
	    if (rows.length==0){
	    	toptip('请选择要删除的行!');
	        return; 
	    }else{
	    	var  orggroupIds="";
	        for (var i = 0; i < rows.length; i++) {
	        	if(orggroupIds==""){
	        		orggroupIds=rows[i].id;
	        	}else{
	        			orggroupIds=rows[i].id+","+orggroupIds;
	        	}
	        }
	        $.messager.confirm('提示','你确定要删除选中的机构及所有子机构 ?',function(r){
	        	if (r){
	        		$.messager.progress({
	        			text:'数据删除中,请稍候.....'
	        		});
	        		Swet.request.delete(Swet.server.cloud +'/orggroup/' +orggroupIds," ",function(result){ 
	        			$.messager.progress('close');
		        	    toptip('删除成功!');
		        	    $("#table_list_community").datagrid("clearChecked");
		        	    $("#table_list_orggroup").treegrid("reload");
	        		},function(result){
	        			$.messager.progress('close');
	        		});
	        	}
	        });
	    }  
	}
/**
 * 右键时触发事件
 */
var showMenu=function(e, row){
	e.preventDefault();
	//清除所有选中项
	$('#treegrid_list_orggroup').treegrid('unselectAll');
	// 查找节点
	$('#treegrid_list_orggroup').treegrid('select', row.id);
	// 显示快捷菜单
	$('#menu_list_orggroup').menu('show', {
		left: e.pageX,
		top: e.pageY
	});
}
		

/**
 * 绑定搜索按钮的的点击事件
 */	
function doSearch(){
	$('#table_list_orggroup').treegrid('load');
}

/**
 *取消时关闭dailog函数
 */
function closeForm(){
	$('#dlg_add_orggroup').dialog('close');
}
/**
 * 清除表单中的数据
 */
function clearDate(){
	$("#form_add_orggroup").form("clear");
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
//加载所属机构的树型菜单
function loadOrggrouTree(){
	$("#combotree_add_orggroupId").combotree({
		method:'post',
		url:Swet.server.cloud+'/orggroup/find/parentId',
		valueField:'id',
		textField:'text',
		prompt:'请选择'
	});
}
//设置时间
function setCloseDate(newDate,oldDate){
	var closeDate = new Date(newDate);
    $('#dtb_add_orggroup_closeDate').datebox().datebox('calendar').calendar({
        validator: function(date){
            return closeDate<=date;
        }
    });
}
//验证唯一性
function check(){
	$('#txb_add_orggroup_orggroupId').numberbox({
		validType:'remote[checkOrggroupIdUrl,checkOrggroupId]'
	});
}
function openCheck(){
	$('#txb_add_orggroup_orggroupName').textbox({
		novalidate:false
	});
}
function colsedCheck(){
	$('#txb_add_orggroup_orggroupName').textbox({
		novalidate:true
	});
}
