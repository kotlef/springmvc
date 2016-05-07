/***
 * 服务中心模块
 * 孟话然
 */

$(function(){
	
	$("#orgid").combotree({
		url: Swet.server.cloud+'/org/serchOrgByParentIdNoSation',
		valueField:"id",
		textField:"text",
		method:"get",
	});
	
	$("#datagrid_list_station").datagrid({
		method:"GET",
		nowrap:"false",
		sortName:"orgid",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"orgid",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		pageSize:25,
		pageList:[15,25,35],
		fit:true,
		columns:[[
		    {field:"stationNo",title:"服务站编号",align:"center",width:30 },
			{field:"stationName",title:"服务站名称",align:"center",width:75 },
			{field:"stationType",title:"服务站类型",align:"center",width:30,formatter:translateTtype},
			{field:"stationAdd",title:"服务站地址",align:"center",width:81 },
			{field:"stationTel",title:"服务站电话",align:"center",width:50 },
			{field:"stationLevel",title:"服务站级别",align:"center",width:30},
			{field:"updataRow",title:"修改",align:"center",width:15,formatter:updateR},
		    {field:"browRow",title:"浏览",align:"center",width:15,formatter:browR},
			{field:"orgid",title:"组织内码",align:"center",width:30,checkbox:"true" },
		]],
		toolbar:"#btn_operate_station",
		onBeforeLoad:function(param){
			var value = $('#searchBox_station').searchbox('getValue');
			if(!value==""){
				$('#datagrid_list_station').datagrid('options').url = Swet.server.mainArchive+'/station/searchByKeyValueLike/params';
				param.value = value;
			}else{
				$('#datagrid_list_station').datagrid('options').url = Swet.server.mainArchive+'/station/toPage';
			}
		}
	});
})


var dec = null;//判别修改或添加

/*
 * 字典翻译
 */
function translateTtype(v){
	return Swet.dic.translate("STATION_TYPE_"+v);
}

/*
 * 修改删除快捷
 */
function updateR(val,row,index){
	tem = 0;
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-edit' onclick='updateStation("+index+")' plain='true'></a>";
}

function browR(val,row,index){
	return "<a href='javascript:void(0)' style='margin: 0 5px; padding: 0 0 0 16px;cursor:pointer;' class='btn-search' onclick='browseStation("+index+")' plain='true'></a>";
}

/*
 * 搜索station
 */
function getStationByParams(){
	$("#datagrid_list_station").datagrid("load");
}

/*
* 添加Sation
*/
function addStation(){
	$("#EditButton").show();
	$("#orgid").combotree("reload");
	$("#stationEdit").dialog("open");
	$('#stationEditForm').form('clear');
	$("input",$("form[id=stationEditForm]")).prop("readonly",false);
	dec = 1;
}

/*
* 浏览Sation
*/
function browseStation(index){
	$('#datagrid_list_station').datagrid('selectRow',index);
	$("#EditButton").hide();
	var row = $("#datagrid_list_station").datagrid("getSelected");
	if (row){
		$('#stationEditForm').form('load',row);
		$("input",$("form[id=stationEditForm]")).prop("readonly",true);
	}
	$("#orgid").combotree("reload");
	$('#stationEdit').dialog('setTitle','浏览服务站资料');
	$("#stationEdit").dialog("open");
}

/*
* 修改Sation
*/
function updateStation(index){
	$('#datagrid_list_station').datagrid('selectRow',index);
    var row = $('#datagrid_list_station').datagrid('getSelected'); 
	$("#EditButton").show();
	if (row){
		$('#stationEditForm').form('load',row);
		$("input",$("form[id=stationEditForm]")).prop("readonly",false);
	}
	$("#orgid").combotree("reload");
	$('#stationEdit').dialog('setTitle','修改服务站资料');
	$("#stationEdit").dialog("open");
	dec = 2;
}

/*
* 取消按钮Sation
*/
function CancelEdit(){
	$('#stationEdit').dialog('close');
	$('#stationEditForm').form('clear');
	$("#datagrid_list_station").datagrid("clearSelections");
}

/*
* 删除Sation
*/
function deleteStation(){
	var selRow = $('#datagrid_list_station').datagrid('getSelections');
	if (selRow.length==0) { 
		$.messager.show({
			title:'提示',
			msg:'请选择要删除的数据.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
	     }); ;  
        return; 
    }else{
    	var ORGID="";
        for (var i = 0; i < selRow.length; i++) {
        	if(ORGID==""){
        		ORGID=selRow[i].orgid;
        	}else{
        		ORGID=selRow[i].orgid+","+ORGID;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		Swet.request.delete(Swet.server.mainArchive+"/station/"+ORGID," ",function(result){ 
        			$.messager.show({
		        	    title: "提示",
		        	    msg: result.msg,
		        	    showType: 'slide',
	        	    })
        			$("#datagrid_list_station").datagrid("reload");
        		});
        	}
        });
    }
	$("#datagrid_list_station").datagrid("clearSelections");
}

/*
* 保存Sation
*/
function saveStation(){
	//验证
	if(!$('#stationEditForm').form('validate')){
		return;
	}
	//得到表单数据
	var obj = $("#stationEditForm").form("getData");
	var url=Swet.server.mainArchive+"/station";
	if(dec==2){//改为修改station功能
		 url=Swet.server.mainArchive+"/station/"+obj.orgid;
	 }
	Swet.request.save(
			url,  
			obj,
			function(result){
				if(result.success){
					$("#stationEditForm").form("clear");
					$('#stationEdit').dialog('close');
					$("#datagrid_list_station").datagrid("reload");
					dec = 0;
					$.messager.show({
		        	    title: "操作成功",
		        	    msg: result.msg,
		        	    showType: 'slide',
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