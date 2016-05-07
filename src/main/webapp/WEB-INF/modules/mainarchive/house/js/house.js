var currentUser=undefined;
var info=undefined;
var currentOrg=undefined;
var currentCid;
var checkEmailUrl,checkCellUrl,checkIdNoUrl;
//手机号验证
checkCell = 'cell';
checkCellUrl = Swet.server.cloud+'/user/checkCell';
//证件号验证
checkIdNo = 'idNo';
checkIdNoUrl = Swet.server.cloud+'/user/checkIdNo';

$(function(){
	$.getScript("http://webapi.amap.com/maps?v=1.3&key=34784e95605094b5dfef5b7aa0505790")
	.done(function() {
	})
	.fail(function() {
	});
	$('#dialog_add_house').dialog({
		onClose:function(){
			$('#form_save_house').form('clear');
			$("#datagrid_list_house").datagrid("clearSelections");
		}
	});

	info=JSON.parse(Swet.dic.translate("currentUserInfo"));
	currentUser=info.user;
	currentOrg=info.org[0];
	var cids=new Array();
	for(var i=0;i<info.res.length;i++){
		if(info.res[i].resType == 'C'){
			cids.push(info.res[i].itemId)
		}
	}
	currentCid = cids.join(',');
	
	$('#cmb_query_user_idType').combobox({
		url:Swet.server.mainArchive+'/wordbook/list/ID_TYPE',
		method:'GET',
		onSelect: function(record){
			switch (record.wordValue) {
			case "0":
				//证件类型-身份证
				$('#cmb_query_user_idNo').textbox({
					validType:['idcard','remote[checkIdNoUrl,checkIdNo]']
				}); 
				break;
			case "C":
				//证件类型-护照
				$('#cmb_query_user_idNo').textbox({
					validType:['passport','remote[checkIdNoUrl,checkIdNo]']
				}); 
				break;
			case "P":
				//证件类型-军官证
				$('#cmb_query_user_idNo').textbox({
					validType:['officerCertificate','remote[checkIdNoUrl,checkIdNo]']
				});
				break;
			default:
				break;
			}
		}
	});
	$("#datagrid_list_house").datagrid({
		method:"GET",
		nowrap:"false",
		sortName:"houseId",
		sortOrder:"desc",
		collapsible:"true",
		loadMsg:"数据加载中...",
		idField:"houseId",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		fit:true,
		pageSize:30,
		singleSelect:true,
		columns:[[
				  {field:"houseNo",title:"房屋编号",align:"left",width:50 },
				  {field:"houseType",title:"房屋类型",align:"left",width:50,formatter:translateHtype},
				  {field:"livingRoomCount",title:"厅数量",align:"left",width:50 },
				  {field:"bedRoomCount",title:"卧室数量",align:"left",width:50 },
				  {field:"kitchenCount",title:"厨房数量",align:"left",width:50 }
				  ]],
		toolbar:"#btn_operate_house",
		onBeforeLoad: function (param) {
			$("#datagrid_list_house").datagrid("clearSelections");
			var value = $('#searchBox_house').searchbox('getValue');
			var key = $('#searchBox_house').searchbox('getName');
			if(!value==""){//添加数据权限
				$('#datagrid_list_house').datagrid('options').url = Swet.server.mainArchive+'/house/res/searchByKeyValue/toPage?cids='+currentCid;
				param.key = key;
				param.value = value;
			}else{//添加数据权限
				$('#datagrid_list_house').datagrid('options').url = Swet.server.mainArchive+'/house/res/toPage?cids='+currentCid;
			}
		},
		onClickRow:function(rowIndex,rowDate){
			document.getElementById("houseId").value=rowDate.houseId;
			searchUser(rowDate.houseId);
		}
	})
})

function translateHtype(v){//翻译房屋类型
	return Swet.dic.translate("HOUSE_TYPE_"+v);
}
/**
 * 创建房屋详细页面
 */
function createImgDIV(row){
	var houseType = Swet.dic.translate("HOUSE_TYPE_"+row.houseType);
	var div=
	'<div style="margin:2px;width:100%;float:center;">'+
		'<table style="width:100%" cellpadding="5" border="0">'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "房屋类型:" + '</td>'+
				'<td style="width:60px;text-align:left;border-bottom:1px solid #ccc">'+getValue(houseType)+ '</td>'+
				'<td style="width:70px;text-align:right">'+ "卧室数量：" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.bedRoomCount) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "厅数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.livingRoomCount) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "阳台数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.verandaCount) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "卫生间数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.washRoomCount) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "设计图:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.houseDesignDid) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "大门数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.gateCount)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "厨房数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.kitchenCount)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "户型图:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.houseModelDid) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "窗户数量:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.windowCount)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "围墙长度:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.wallLength) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "总面积:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.houseAcreage) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "房屋编号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.houseNo) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "花园面积:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.gardenAcreage) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "经&nbsp;&nbsp;&nbsp;度:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.longitude) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "小区内码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+getValue(row.kitchenCount) +  '</td>'+
				'<td style="width:60px;text-align:right">'+ "纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.latitude) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "备注信息:" + '</td>'+
				'<td colspan="5" style="width:150px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.note) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "房屋描述:" + '</td>'+
				'<td colspan="5" style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.houseDesc) + '</td>'+
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
function udpateOrBrowseHouse(val){
	var selRow = $('#datagrid_list_house').datagrid('getSelections');
	if (selRow.length==0) { 
		$.messager.alert("提示", "请选择一行数据 ！", "info");  
	    return; 
	}else if(selRow.length>=2){ 
		$.messager.alert("提示", "一次只能操作一条信息 ！", "info");  
	    return; 
	}else{
		var row = $("#datagrid_list_house").datagrid("getSelected");
		if (row){
			row.houseType_name = Swet.dic.translate("HOUSE_TYPE_"+row.houseType);
			$('#form_edit_house').form('load',row);
		}
		if(val==0){// 0--浏览  1--修改
			$("#panel_HouseDetail").panel({
				 href:"subHtml/houseDetail.html",
				 onLoad:function(){
					$('#form_detail_house').append(createImgDIV(row));
					$("#dialog_detail_house").dialog("open");
				}
			})
	    }else{
	     	$("#dialog_edit_house").dialog("open");
	    }
	}
}
	

function getHouseByParams(){
	$("#datagrid_list_house").datagrid("load");
}
  	
/**
 * 批删除数据
 */
function deleteHouse() {
	var rows = $('#datagrid_list_house').datagrid('getSelections');
    if (rows.length==0){
    	$.messager.alert("提示", "请选择要删除的行！", "info");  
        return; 
    }else{
    	var  HID="";
        for (var i = 0; i < rows.length; i++) {
        	if(HID==""){
        		HID=rows[i].houseId;
        	}else{
        			HID=rows[i].houseId+","+HID;
        	}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		Swet.request.delete(Swet.server.mainArchive+"/house/"+HID," ",function(result){ 
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
        	    $("#datagrid_list_house").datagrid("reload").datagrid("clearSelections");
        	    $('#datagrid_list_user').datagrid('loadData', { total: 0, rows: [] });
        		});
        	}
        });
    }  
}
	
/**
 * 取消添加或修改按钮方法
 */
function CancelEditHouse(){
	$('#dialog_edit_house').dialog('close');
	$('#form_edit_house').form('clear');
	$('#datagrid_list_house').datagrid("clearSelections");
}

/**
 * 确定修改房屋信息
 */
/*function saveHouse(){
	var houseId=$("#houseId").val();
	var url=Swet.server.mainArchive+"/house/"+houseId;
	$("#form_edit_house").form("submit",{
		url:url,
		method:"POST",
		onSubmit:function(){
		return $(this).form('validate');
		},
		success:function(result){
			data=$.parseJSON(result);
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
			$("#dialog_edit_house").dialog("close");
			$('#form_edit_house').form('clear');
			$("#datagrid_list_house").datagrid("reload").datagrid("clearSelections");
			}
   }); 
}*/
/**
 * 关闭浏览房屋详细窗口
 */
function closeHouseDetail(){
	$("#dialog_detail_house").dialog("close");
	$('#form_detail_house').form('clear');
	$("#datagrid_list_house").datagrid("clearSelections");
}
/****************************************************************************************/
/**
 * 房屋信息界面字典翻译
 */
function gridTranslateHouseType(v){
	return Swet.dic.translate("HOUSE_TYPE_"+v);//房屋类型翻译
}
function gridTranslateHouseStatus(v){
	return Swet.dic.translate("STATUS_"+v);//房屋状态翻译
}
/**
 * 街道地址修改
 */
function editHouseTown(e) {
	var town = $(e.data.target).combobox('getValue');
	$('#customer_add_house_town').combobox({
		editable:true,
		value:town
	});
}

/**
 * 房屋信息界面操作列中的修改操作实现
 */
function editHouse(index){
	var row = $("#datagrid_list_house").datagrid("getSelected");
	if(row==null||row==""){
		$.messager.alert("提示", "请选择你要编辑的行 ！", "info");
		return;
	}
	$('#dialog_add_house').dialog({iconCls: 'icon-edit'});
	$('#dialog_add_house').dialog("open").dialog('setTitle','修改房屋信息');
 	$('#panel_add_house').panel({
 	    href:'subHtml/houseEdit.html',
 	    onLoad:onLoadEditHouse(row)
 	});
}
/**
 * 修改房屋信息href加载
 */
function onLoadEditHouse(row){
	$.messager.progress({
		title:'请稍后',
		msg:'系统正查询数据...'
	});
	
	$.getScript("js/initRegion.js")
		.done(function(){
			Swet.request.get(
				Swet.server.mainArchive+"/community/findByCommunityId/"+row.communityId,"",
				function(community){
					row.regionId = community.regionId;
					row.provinceId = community.provinceId;
					row.cityId = community.cityId;
					row.districtId = community.districtId;
					row.town = community.town;
					$('#combobox_region_house').combobox('reload',Swet.server.mainArchive+"/region/"+0);
					$('#combobox_province_house').combobox('reload',Swet.server.mainArchive+"/region/"+community.regionId);
					$('#combobox_city_house').combobox('reload',Swet.server.mainArchive+"/region/"+community.provinceId);
					$('#combobox_district_house').combobox('reload',Swet.server.mainArchive+"/region/"+community.cityId);
					Swet.request.get(Swet.server.mainArchive+"/community/searchByDistrictId/"+community.districtId,"",function(communitys){
							$('#customer_add_house_community').combobox("loadData",communitys);
							$('#form_save_house').form('load',row);
							$.getScript("js/initUpload.js")
								.done(function(){
									$("#img_up_houseModel").attr('src',row.houseModelUrl);
									$("#img_up_houseDesign").attr('src',row.houseDesignUrl);
									$.getScript("js/initMap.js")
										.done(function() {
											var x=row.longitude;
											var y=row.latitude;
											if(x!=null&&x!=""&&y!=null&&y!=""){
												$('#txt_house_longitude').textbox('setValue',x);
												$('#txt_house_dimension').textbox('setValue',y);
												var pos=new AMap.LngLat(x,y);
												house_handler.marker.setPosition(pos);
												house_handler.mapObjAdd.setZoomAndCenter(14,pos);
											}
											$.messager.progress('close');
										})
										.fail(function() {
											/* 靠，马上执行挽救操作 */
											console.log("地图初始化失败!");
											$.messager.progress('close');
										});
								})
								.fail(function(){
									console.log("上传控件初始化失败!");
									$.messager.progress('close');
								})
						}
					);
				}
			);
		})
		.fail(function(){
			console.log("区域信息初始化失败!");
			$.messager.progress('close');
		})
}
/**
 * 保存房屋信息按钮
 */
function saveHouse(){
	var houseId,url,house;
	if(!$('#form_save_house').form('validate')){
		$.messager.alert('提示','请核对房屋信息必填项!','warning');
		return;
	}
	houseId = $('#text_add_house_id').textbox('getValue');
	if(houseId==""||houseId==null){
		url=Swet.server.mainArchive+"/house";
	}else{
		url=Swet.server.mainArchive+"/house/"+houseId;
	}
	house = $('#form_save_house').form('getData');
	var tude = $('#form_save_house_tude').form('getData');
	var cityText = $('#combobox_city_house').combobox('getText');
	var districtText = $('#combobox_district_house').combobox('getText');
	var townText = $('#customer_add_house_town').combobox('getText');
	var communityText = $('#customer_add_house_community').combobox('getText');
	house.status = "0";
	house.communityAdd = cityText+" "+districtText+" "+townText+" "+communityText+" "+house.houseNo;
	house.longitude = tude.longitude;
	house.latitude = tude.latitude;
	//判断房屋的唯一性
	$.messager.progress({
		title:'请稍后',
		msg:'系统正保存数据...'
	});
	Swet.request.get(Swet.server.mainArchive+"/house/checkHouse",{
			communityId:house.communityId,
			houseNo:house.houseNo
		},function(checkHouseresult){
			if(!checkHouseresult.success){
				if(houseId!=checkHouseresult.rows){
					$.messager.progress('close');
					var communityName = $('#customer_add_house_community').combobox('getText');
					$.messager.alert("提示","“"+communityName+"”小区下已有房屋:“"+house.houseNo+"”存在！请重新输入","warning");
					return;
				}
			}
			Swet.request.save(url,house,function(result){
				if(result.success){
					$.messager.progress('close');
					$.messager.show({
						title:'提示',
						msg:"房屋信息保存成功！",
						showType:'slide',
						timeout:3000,
						style:{
							top:document.body.scrollTop+document.documentElement.scrollTop,
						}
					});
					$('#dialog_add_house').dialog('close');
					$("#datagrid_list_house").datagrid("reload");
				}
			});
		 }
	);
}