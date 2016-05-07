var orgid;
$(function(){
	var grid = $("#datagrid_list_role").datagrid();
});
function loadRole(rowData,rowIndex){
	$("#datagrid_list_role").datagrid("getSelected");
	orgid=undefined;
	var org = rowData;
	orgid=org.id;
	if(orgid !=undefined){
		Swet.request.get(
			Swet.server.cloud +'/orgrole/searchByorg/'+orgid, 
			"",
			function(result){
				$("#datagrid_list_role").datagrid('uncheckAll');
				if(result.length>0){
					for(var i=0;i<result.length;i++){
						var rowIndex = $("#datagrid_list_role").datagrid("getRowIndex",result[i]);
						if(rowIndex >=0){
							$("#datagrid_list_role").datagrid("checkRow",rowIndex);
						}
					}
				}
			},
			function(result){
				errorCallback(result);
			}
		)
	}
}
function saveOrgrole(){
	var obj = new Object();
	var roles =$("#datagrid_list_role").datagrid("getSelections");
	if(!roles == ""){
		obj.orgid=orgid;
		var roleIds=""
		for(var i = 0;i<roles.length;i++){
			if(roleIds == ""){
				roleIds=roles[i].roleId;
			}else{
				roleIds=roles[i].roleId +","+roleIds;
			}
		}
		obj.roleIds=roleIds;
		addOrgrole(obj);
	}
}
function addOrgrole(obj){
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.save(
			Swet.server.cloud +'/orgrole',
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

function getrow(){
	var rowIndex = $("#datagrid_list_role").datagrid("getRowIndex",6);
	alert(rowIndex);
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
	}
}
/**
 * 成功提示方法
 */
function successCallback(result){
	if(result.success){
		toptip('操作成功!');
	}
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
