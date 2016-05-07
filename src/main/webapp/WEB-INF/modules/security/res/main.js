var idd=undefined;
var resId="";
var url="";
var resType="";
var resItemId="";
$(function(){ 
	$('#cmb_search_resType').combobox({
        url:Swet.server.mainArchive+'/wordbook/list/RES_TYPE',
        valueField:'wordValue',
        textField:'wordDisplay',
        editable:false,
        method:'GET',
        onSelect: function(record){
        	$('#selectId').val(record.wordValue);
        }
	});
    $('#cmb_query_res_resType').combobox({
        url:Swet.server.mainArchive+'/wordbook/list/RES_TYPE',
        valueField:'wordValue',
        textField:'wordDisplay',
        editable:false,
        method:'GET',
        onSelect: function(record){
        	if(record.wordValue=="S"){//进入服务站资源
        		searchStation(0);
        	}else{//进入小区资源
        		//$("#cmb_query_res_itemId").combobox('clear');
        		searchStation(1);
        	}
        }
    });  
    $('#dialog_edit_res').dialog({
		onClose:function(){
			$('#form_edit_res').form('clear');
			$("#datagrid_list_res").datagrid("clearSelections");
		}
	});
	$("#datagrid_list_res").datagrid({
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
		pageSize:"30",
		fit:true,
		singleSelect:true,
		columns:[[
		          {field:"resType",title:"资源类型",align:"left",width:50,formatter:translateRtype},
		          {field:"itemId",title:"资源内码",align:"left",width:50},
		          {field:"note",title:"备注",align:"left",width:50 },
				  {field:"createTime",title:"建立时间",align:"left",width:50 }
				]],
		toolbar:"#btn_operate_res",
		onBeforeLoad:function(param){
			$('#datagrid_list_res').datagrid('clearSelections');
			var value =$('#selectId').val();
			if(!value==""){
				$('#datagrid_list_res').datagrid('options').url = Swet.server.cloud+'/res/searchByRtype/toPage';
				param.value =value;
			}else{
				$('#datagrid_list_res').datagrid('options').url = Swet.server.cloud+'/res/toPage';
			}
		}
	})
});
/**
 * 字典表翻译
 */
function translateRtype(v){
	return Swet.dic.translate("RES_TYPE_"+v);
}

function searchByRtype(){
	$("#datagrid_list_res").datagrid("load");
}

function searchStation(val){
	if(val==0){
		$('#cmb_query_res_itemId').combobox({
	        url:Swet.server.mainArchive+'/station',
	        valueField:'orgid',
	        textField:'stationName',
	        editable:false,
	        method:'GET',onLoadSuccess: function(){
	        	if(idd!=undefined)
	        	$('#cmb_query_res_itemId').combobox('select',idd);
	        }
	        
		})
		return;
	}else{
		$('#cmb_query_res_itemId').combobox({
	        url:Swet.server.mainArchive+'/community',
	        valueField:'communityId',
	        textField:'communityNo',
	        editable:false,
	        method:'GET',onLoadSuccess: function(){
	        	if(idd!=undefined)
		        	$('#cmb_query_res_itemId').combobox('select',idd);
		        }
		})
	}
	
}
/**
 *删除资源
 */
function deleteRes(){ 
	var rows = $('#datagrid_list_res').datagrid('getSelections');
    if (rows.length==0){ 
    	$.messager.alert("提示", "请选择要删除的行！", "info");  
        return; 
    }else{
    	var  RID="";
        for (var i = 0; i < rows.length; i++) {
        	if(RID==""){
        		RID=rows[i].resId;
        	}else{
        		RID=rows[i].resId+","+RID;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		Swet.request.delete(Swet.server.cloud+"/res/"+RID," ",function(result){
        			if(result.success){
        				$.messager.show({
    						title:'提示',
    						msg:result.msg,
    						showType:'slide',
    						style:{
    							right:'',
    							top:document.body.scrollTop+document.documentElement.scrollTop,
    							bottom:''
    						}
    					});
        				$("#datagrid_list_res").datagrid("reload").datagrid("clearSelections");
        			}
        		});
        	}
        });
    }  
}
/**
*编辑资源信息
*/
function editRes(){
	var selRow = $('#datagrid_list_res').datagrid('getSelections');
	 if (selRow.length==0) {
		 $.messager.alert("提示", "请选择一行数据 ！", "info");
		 return;
	 }else if(selRow.length>=2){
		 $.messager.alert("提示", "一次只能操作一条信息 ！", "info");  
	     return; 
	 }else{	
		 var row = $("#datagrid_list_res").datagrid("getSelected");
		 if (row){
			$("#form_edit_res").form('load',row);
			idd=row.itemId
        	if(row.resType=="S"){//进入服务站资源
        		searchStation(0);
        	}else{//进入小区资源
        		//$("#cmb_query_res_itemId").combobox('clear');
        		searchStation(1);
        	}
			
		 }
	     $("#dialog_edit_res").dialog("open");
	 }
} 
function check(){
	resType=$("#cmb_query_res_resType").combobox("getValue");
	resItemId=$("#cmb_query_res_itemId").combobox("getValue");
	res=resType+","+resItemId;
	url=Swet.server.cloud+'/res/checkRes/'+res;
	Swet.request.get(url,"",function(data){
		if(data==false){
			$.messager.alert("提示", "该资源已存在,请重新输入！", "info");
			return;
		}else{
			saveRes();
		}
	});
}
function checkRes(){
	resType=$("#cmb_query_res_resType").combobox("getValue");
	resItemId=$("#cmb_query_res_itemId").combobox("getValue");
	row = $("#datagrid_list_res").datagrid("getSelected");
	resId=$("#resId").val();
	if(resId!=null&&resId>0){//判断修改
		if(row.resType!=resType||row.itemId!=resItemId){
			check();
		}else{
			saveRes();
		}
	}else{
		check();
	}
}
/**
*保存编辑后的资源
*/

function saveRes(){
	resId=$("#resId").val();
	if(resId!=null||resId!=""){//--修改资源
	    url=Swet.server.cloud+"/res/"+resId;
	}
	url=Swet.server.cloud+"/res/saveRes";
	$("#form_edit_res").form("submit",{
	   url:url,
	   method:"POST",
	   onSubmit:function(){
		   return $(this).form('validate');
	    },success:function(result){
	    	result=$.parseJSON(result);
	    	if(result.success){
	    		$.messager.show({
					title:'提示',
					msg:"保存成功！",
					showType:'slide',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
	    		$("#dialog_edit_res").dialog("close");
	    		$('#form_edit_res').form('clear');
	    		$("#datagrid_list_res").datagrid("reload").datagrid("clearSelections");
	    	idd=undefined;	}
	    }
	  });
}


