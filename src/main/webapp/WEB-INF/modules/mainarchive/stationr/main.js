/***
 * 服务中心关联模块
 * 孟话然
 */

$(function(){
	$("#datagrid_list_station").datagrid({
		singleSelect:true,
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
			{field:"stationType",title:"服务站类型",align:"center",width:30,formatter:translateTtype()},
			{field:"stationLevel",title:"服务站级别",align:"center",width:30},
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
		},
		onClickRow:function(index,row){
			$('#datagrid_editor_station').datagrid('options').url = Swet.server.mainArchive+'/stationR/searchByOrgid/'+row.orgid;
			$('#datagrid_editor_station').datagrid('reload');
			editIndex = undefined;
		}
	});
	
	$("#datagrid_editor_station").datagrid({
		singleSelect:true,
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
		onClickCell: onClickCell,
		columns:[[
			{field:"orgid",title:"服务站名称",align:"center",width:75,
				formatter:function(value,row){return row.stationName;},
		    	editor:{type:'combobox',options:{valueField:'orgid',textField:'stationName',method:'get',url:Swet.server.mainArchive+'/station',required:true}}
		    },
		]],
		toolbar:"#btn_operate_stationR",
	});
	
})

/*
 * datagrid编辑器方法
 */
var editIndex = undefined;
function endEditing(){
    if (editIndex == undefined){return true}
    if ($('#datagrid_editor_station').datagrid('validateRow', editIndex)){
        var ed = $('#datagrid_editor_station').datagrid('getEditor', {index:editIndex,field:'orgid'});
        var stationName = $(ed.target).combobox('getText');
        $('#datagrid_editor_station').datagrid('getRows')[editIndex]['stationName'] = stationName;
        $('#datagrid_editor_station').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
/*
 * datagrid编辑器方法
 */
function onClickCell(index, field){
    if (editIndex != index){
    	if (endEditing()){
    	   $('#datagrid_editor_station').datagrid('selectRow', index).datagrid('beginEdit', index);
    	   var ed = $('#datagrid_editor_station').datagrid('getEditor', {index:index,field:field});
    	   ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
    	   editIndex = index;
    	}else{
    		$('#datagrid_editor_station').datagrid('selectRow', editIndex);
    	}
    }
}

/*
 * 字典翻译
 */
function translateTtype(v){
	return Swet.dic.translate("STATION_TYPE"+v);
}

/*
 * 搜索station
 */
function getStationByParams(){
	$("#datagrid_list_station").datagrid("load");
}

/*
 * 添加关联站
 */
function addStationR(){
	if (endEditing()){
        $('#datagrid_editor_station').datagrid('appendRow',{orgid:"0"});
        editIndex = $('#datagrid_editor_station').datagrid('getRows').length-1;
        $('#datagrid_editor_station').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
    }
}

/*
 * 移除关联站
 */
function removeStationR(){
	if (editIndex == undefined){
		$.messager.show({
			title:'提示',
			msg:'请点击要移除的行.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
		 });
		return;
	}
    $('#datagrid_editor_station').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
}

/*
 * 保存关联站
 */
function saveStationR(){
	endEditing();
	var lRow = $("#datagrid_list_station").datagrid("getSelected");
	var selRows = $('#datagrid_editor_station').datagrid("getRows");
	if(lRow==null){
		$.messager.show({
			title:'提示',
			msg:'请选择一个服务站.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
		 });
		return;
	}
	var RORGIDS = ""; 	
	for(var a=0; a<selRows.length; a++){
		if(RORGIDS==""){
			RORGIDS=selRows[a].orgid;
		}else{
			RORGIDS=selRows[a].orgid+","+RORGIDS;
		}
	}
	var obj=new Object();
	obj.orgid=lRow.orgid;
	obj.rOrgids=RORGIDS;
	Swet.request.save(
			url=Swet.server.mainArchive+'/stationR',  
			obj,
			function(result){
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
	            	$('#datagrid_editor_station').datagrid('reload');
	    			editIndex = undefined;
	            }
			},
			function(result){
				if(result.error){
					$.messager.alert("提示", "操作失败！", "info"); 
				}
			}
		);	
}