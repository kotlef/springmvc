var url="";
var supplyId="";
var supplyName="";
var rows="";
var row="";
$(function(){
	//点击绑定阴影
	/*$('#y-test').bind('click',function(){
		console.info($(this).parents('.window-shadow'));
		if($(this).is(':checked')){
			$('#div_supply_otherinfo').show();
			$(this).parents('.window').next().css('height','450px');
		}else{
			$('#div_supply_otherinfo').hide();
			$(this).parents('.window').next().css('height','340px');
		}
	});*/
	$('#foundDate').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date<=d1;
		}
	});
	$('#regDate').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date<=d1;
		}
	});
	
	
	$('#dialog_edit_supply').dialog({
		onClose:function(){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			$('#foundDate').datebox().datebox('calendar').calendar('moveTo', d1);
			$('#regDate').datebox().datebox('calendar').calendar('moveTo', d1);
			$("#datagrid_list_supply").datagrid("clearSelections");
			$("#form_edit_supply").form("clear");
		}
	});
	$("#datagrid_list_supply").datagrid({
		method:"GET",
		nowrap:"false",
		striped:"true",
		sortName:"supplyId",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"supplyId",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		pageSize:"30",
		singleSelect:true,
		fit:true,
		columns:[[
				  {field:"supplyName",title:"供应商名称",align:"left",width:80},
				  {field:"supplyType",title:"供应商类型",align:"left",width:60,formatter:translateStype},
				  {field:"corpClass",title:"企业性质",align:"left",width:60,formatter:translateCorpClass},
				  {field:"supplyDesc",title:"供应商描述",align:"left",width:100 },
				  {field:"note",title:"备注",align:"left",width:100 }
				]],
	toolbar:"#btn_operate_supply",
	onBeforeLoad:function(param){
		$('#datagrid_list_supply').datagrid('clearSelections');
		var value = $('#searchBox_supply').searchbox('getValue');
		var key = $('#searchBox_supply').searchbox('getName');
		if(!value==""){
			$('#datagrid_list_supply').datagrid('options').url = Swet.server.mainArchive+'/supply/searchByKeyValueLike/toPage';
			param.key = key;
			param.value = value;
		}else{
			$('#datagrid_list_supply').datagrid('options').url = Swet.server.mainArchive+'/supply/toPage';
		}
	}
	});
})
	
function translateStype(v){
	return Swet.dic.translate("SUPPLY_TYPE_"+v);
}

function translateCorpClass(v){
	return Swet.dic.translate("CORP_CLASS_"+v);
}
/**
 * 创建供应商详细页面
 */
function createImgDIV(row){
	alert("有数据："+row.corpClass);
	var paidType = Swet.dic.translate("PAID_WAY_"+row.paidWay);
	var status = Swet.dic.translate("STATUS_"+row.status);
	var corpClass = translateCorpClass(row.corpClass);
	alert("wori:"+corpClass);
	var supplyType = Swet.dic.translate("SUPPLY_TYPE_"+row.supplyType);
	var taxPayerType = Swet.dic.translate("TAX_PAYER_TYPE_"+row.taxPayerType);
	var div=
	'<div style="margin:2px;width:100%;float:center;">'+
		'<table style="width:100%" cellpadding="5" border="0">'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "供应商名称:" + '</td>'+
				'<td style="width:160px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.supplyName)+ '</td>'+
				'<td style="width:70px;text-align:right">'+ "供应商类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(supplyType) + '</td>'+
				'<td style="width:70px;text-align:right">'+ "企业性质:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(corpClass) +  '</td>'+
				'<td style="width:60px;text-align:right">'+ "状态:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(status)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "纳税人类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(taxPayerType) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "供应商描述:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.supplyDesc) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "付款路径:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(paidType) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "备注:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.note) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "营业执照号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.licenseNo)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "法定代表人:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.corpPer) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "企业代码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.corpCode) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "经度:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.longitude) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "机构代码证:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.corpCertificate) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "成立日期:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.foundDate) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "注册日期:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.regDate) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "纬度:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.latitude)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "银行帐户名:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.accountName) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "注册地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.regAddr) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "注册商标:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.regMark) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "传真:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.fax) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "银行注册国家:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bankState)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "公司电话:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.tel) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "注册资本:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.regCaptial) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "主页:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.url) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "税务登记名称:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.taxName) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "银行名称:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bankName) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "银行账号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bankNo)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "经营区域描述:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bizAreaDesc) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "联系人名称:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.linkman)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "纳税编号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.taxNo) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "邮政编码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.zip) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "经营品牌描述:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bizBrandDesc) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "联系人手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.cell) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "经营范围:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bizScope) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "经营规模:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bizScaleid) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "经营类别描述:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bizCateDesc) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "通讯地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.addr1) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "经营地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.addr2) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "地址三:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.addre3) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "区域:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.regionId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "省:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.provinceId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "市:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.cityId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "区:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.districtId) + '</td>'+
			'</tr>'+
		'</table>'+ 
	'</div>';
	return div;
}
function getValue(val){
	if(val==undefined||val=="undefined"||val==null||val=="null"){
		return "";
	} return val;
}
/**
 * 打开修改或浏览页面并进行初始化
 */
function updateOrBrowseSupply(val){
	 rows = $('#datagrid_list_supply').datagrid('getSelections');
	 if (rows.length==0) {
		 $.messager.alert("提示", "请选择一行数据 ！", "info");
		 return;
	 }else if(rows.length>=2){
		 $.messager.alert("提示", "一次只能操作一条信息 ！", "info");  
	     return; 
	 }else{
		row = $("#datagrid_list_supply").datagrid("getSelected");
		if (row){
			/*$('#combobox_region_house').combobox('reload',Swet.server.mainArchive+"/region/"+0);
			//$('#combobox_region_house').combobox('select',row.regionId);
			$('#combobox_province_house').combobox('reload',Swet.server.mainArchive+"/region/"+row.regionId);
			$('#combobox_city_house').combobox('reload',Swet.server.mainArchive+"/region/"+row.provinceId);
			$('#combobox_district_house').combobox('reload',Swet.server.mainArchive+"/region/"+row.cityId);*/
			$('#form_edit_supply').form('load',row);
			}
		if(val==0){// 0--浏览  1--修改
			$("#panel_SupplyDetail").panel({
				 href:"supplyDetail.html",
				 onLoad:function(){
					$('#form_detail_supply').append(createImgDIV(row));
					$("#dialog_detail_supply").dialog("open");
				}
			})
	    }else{
	     	$("#dialog_edit_supply").dialog("open");
	     }
	}
}
	
/**
 * 模糊查询
 */
function getsupplyByParams(){
	$("#datagrid_list_supply").datagrid("load");
}

/**
 * 单条或批量删除供应商
 */
function deleteSupply() { 
	rows = $('#datagrid_list_supply').datagrid('getSelections');
    if (rows.length==0) { 
    	$.messager.alert("提示", "请选择要删除的行！", "info");  
        return; 
    }else{
    	var  SID="";
        for (var i = 0; i < rows.length; i++) {
        	if(SID==""){
        		SID=rows[i].supplyId;
        	}else{
        		SID=rows[i].supplyId+","+SID;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		Swet.request.delete(Swet.server.mainArchive+"/supply/deleteSupply/"+SID," ",function(result){ 
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
			        	$("#datagrid_list_supply").datagrid("reload");
			        	$("#datagrid_list_supply").datagrid("clearSelections");
        			}
        		});
        	}
        });
    }  
}
	
/**
 * 取消添加或修改按钮方法
 */
function CancelEdit(){
	$('#dialog_edit_supply').dialog('close');
}
/**
 * 确定添加或修改供应商
 */

function check(){
	supplyName=$("#text_id_supplyName").val();
	url=Swet.server.mainArchive+"/supply/checkSupplyName/"+supplyName;
	Swet.request.get(url,"",function(data){
		if(data==false){
			$.messager.alert("提示", "该供应商名称已被占用,请重新输入！", "info");
			return;
		}else{
			saveSupply();
		}
	});
}
function checkSupply(){
	supplyName=$("#text_id_supplyName").val();
	row = $("#datagrid_list_supply").datagrid("getSelected");
	supplyId=$("#supplyId").val();
	if(supplyId!=null&&supplyId>0){//判断修改
		if(row.supplyName!=supplyName){
			check();
		}else{
			saveSupply();
		}
	}else{
		check();
	}
}
function saveSupply(){
	supplyId=$("#supplyId").val();
	url=Swet.server.mainArchive+"/supply";
	if(supplyId!=null&&supplyId>0){//uid>0--修改供应商
		url=Swet.server.mainArchive+"/supply/"+supplyId;
	}
	$("#form_edit_supply").form("submit",{
		url:url,
		method:"POST",
		onSubmit:function(){
			return $(this).form('validate');
		},success:function(data){
			data=$.parseJSON(data);
			$.messager.show({
				title:'提示',
				msg:data.msg,
				showType:'slide',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$("#dialog_edit_supply").dialog("close");
			$('#form_edit_supply').form('clear');
			$("#datagrid_list_supply").datagrid("reload").datagrid("clearSelections");
		}
	});
}

/**
 * 关闭浏览窗口并清空文本框内容
 */

function closeBrowse(){
	$("#dialog_detail_supply").dialog("close");
	$("#form_edit_supply").form("clear");
	$("#datagrid_list_supply").datagrid("clearSelections");
}
	