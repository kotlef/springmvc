/*
 * user模块的功能
 * 用户信息的增删改查
 * 
 */
var checkEmailUrl,checkCellUrl,checkIdNoUrl;
//邮箱验证
checkEmail = 'email';
checkEmailUrl = Swet.server.cloud+'/user/checkEmail';
//手机号验证
checkCell = 'cell';
checkCellUrl = Swet.server.cloud+'/user/checkCell';
//证件号验证
checkIdNo = 'idNo';
checkIdNoUrl = Swet.server.cloud+'/user/checkIdNo';

$(function(){
	$('#cmb_query_user_idType').combobox({
		url:Swet.server.mainArchive+'/wordbook/list/ID_TYPE',
		method:'GET',
		//客户类型数据加载完后默认设置为为：家庭
		onLoadSuccess: function(){
			$('#cmb_query_user_idNo').combobox("select","0");
		},
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
	$('#cmb_query_user_regionId').combobox({
		onSelect: function(record){
			Swet.request.get(
				Swet.server.mainArchive+"/region/"+record.regionId,"",function(data){
					$('#cmb_query_user_cityId').combobox('clear');
					$('#cmb_query_user_districtId').combobox('clear');
					$('#cmb_query_user_provinceId').combobox('loadData',data);
					$('#cmb_query_user_provinceId').combobox('setValue', '');
				}
			);
		}
	});
	$('#cmb_query_user_provinceId').combobox({
		onSelect: function(record){
			Swet.request.get(
				Swet.server.mainArchive+"/region/"+record.regionId,"",function(data){
					$('#cmb_query_user_districtId').combobox('clear');
					$('#cmb_query_user_cityId').combobox('loadData',data);
					$('#cmb_query_user_cityId').combobox('setValue', '');
				}
			);
		}
	});
	$('#cmb_query_user_cityId').combobox({
		onSelect: function(record){
			Swet.request.get(
				Swet.server.mainArchive+"/region/"+record.regionId,"",function(data){
					$('#cmb_query_user_districtId').combobox('loadData',data);
					$('#cmb_query_user_districtId').combobox('setValue', '');
				}
			);
		}
	});
	$("#graduationDate").datebox({
		onSelect:function(startDate){
			$('#birthdate').datebox().datebox('calendar').calendar({
				validator: function(endDate){
					var d1 = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate());
					return endDate<d1;
				}
			});
		}
	});
	$('#dialog_edit_user').dialog({
		onClose:function(){
			$("#datagrid_list_user").datagrid("clearSelections");
			$("#form_edit_user").form("clear");
		}
	});	
	$("#datagrid_list_user").datagrid({
		method:"GET",
		nowrap:"false",
		striped:"true",
		sortName:"uid",
		sortOrder:"asc",	
		collapsible:"true",
		idField:"uid",
		remoteSort:"false",
		fitColumns:"true",
		rownumbers:"true",
		pagination:true,
		singleSelect:false,
		pageSize:30,
		fit:true,
		columns:[[
		          {field:"ck",title:"批量删除",align:"left",width:50,checkbox:true},
				  {field:"userName",title:"用户名称",align:"left",width:50},
				  {field:"userType",title:"用户类型",align:"left",width:50,formatter:translateUtype},
				  {field:"idType",title:"证件类型",align:"left",width:50,formatter:translateItype},
				  {field:"idNo",title:"证件号码",align:"left",width:70 },
				  {field:"cell",title:"手机号",align:"left",width:50 },
				  {field:"email",title:"邮箱",align:"left",width:60 }
				]],
		toolbar:"#btn_operate_user",
		onBeforeLoad:function(param){
			var key = $("#searchBox_user").searchbox("getName");
			var value = $("#searchBox_user").searchbox("getValue");
			$("#datagrid_list_user").datagrid("clearSelections");
			if(!value == ""){
				$('#datagrid_list_user').datagrid('options').url = Swet.server.cloud+ '/user/searchByKeyValueLike/toPage';
				param.key=key;
				param.value=value;
			}else{
				$('#datagrid_list_user').datagrid('options').url = Swet.server.cloud+ '/user/toPage';
				
			}
		} 
	});
	
})

function translateUtype(v){
	return Swet.dic.translate("USER_TYPE_"+v);
}
function translateItype(v){
	return Swet.dic.translate("ID_TYPE_"+v);
}

/**
 * 用户详情浏览界面
 * @param row
 * @returns {String}
 */
function createDetailDIV(row){
	var idType=row.idType_name = Swet.dic.translate("ID_TYPE_"+row.idType);
	var education= Swet.dic.translate("EDUCATION_"+row.education);
	var userType=Swet.dic.translate("USER_TYPE_"+row.userType);
	var blood=Swet.dic.translate("BLOOD_"+row.blood);
	var gender=Swet.dic.translate("GENDER_"+row.gender);
	var marriage=Swet.dic.translate("MARRIAGE_"+row.marriage);
	var status=Swet.dic.translate("STATUS_"+row.status);   
	var polityFace=Swet.dic.translate("POLITYFACE_"+row.polityFace);
	var nationality=Swet.dic.translate("NATIONALITY_"+row.nationality);
	var religion=Swet.dic.translate("RELIGION_"+row.religion);
	var div=
	'<div style="margin:2px;width:100%;float:center;">'+
		'<table style="width:100%" cellpadding="5" border="0">'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "用户名称:" + '</td>'+
				'<td style="width:160px;text-align:left;border-bottom:1px solid #ccc">'+ row.userName+ '</td>'+
				'<td style="width:70px;text-align:right">'+ "用户类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ userType + '</td>'+
				'<td style="width:70px;text-align:right">'+ "性别:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+gender +  '</td>'+
				'<td style="width:60px;text-align:right">'+ "婚姻:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+marriage+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "证件类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ idType + '</td>'+
				'<td style="width:60px;text-align:right">'+ "证件号码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.idNo + '</td>'+
				'<td style="width:60px;text-align:right">'+ "邮箱:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.email + '</td>'+
				'<td style="width:60px;text-align:right">'+ "学历:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ education + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "联系手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.cell+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "备用手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.cell2 + '</td>'+
				'<td style="width:60px;text-align:right">'+ "血型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ blood + '</td>'+
				'<td style="width:60px;text-align:right">'+ "状态:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ status + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "备注信息:" + '</td>'+
				'<td colspan="5" style="width:150px;text-align:left;border-bottom:1px solid #ccc">'+ row.note + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "联系电话:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.tel) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "传真:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.fax) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "QQ号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.qq) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "紧急联系人:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.urgLinkman)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "毕业学校:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.gradSchool)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "民族:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(nationality)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "宗教:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(religion)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "联系人手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.urgTel)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "毕业时间:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.graduationDate)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "生日:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.birthdate)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "籍贯:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.birthPlace)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "现居住地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.addr2)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "政治面貌:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(polityFace)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "体重:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.weight)+'</td>'+
				'<td style="width:60px;text-align:right">'+ "身高:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.height)+'</td>'+
				'<td style="width:60px;text-align:right">'+ "通讯邮编:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.zip) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "户籍地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.addr1) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "专业:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.major) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "职称:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.jobTitle) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "通讯地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.addr3) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;域:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.regionId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "省:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.provinceId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "市:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.cityId) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "区:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.districtId) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "微&nbsp;信&nbsp;号&nbsp;:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.weChat) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "街道:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.town)+ '</td>'+
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
//打开添加页面
function openAdd(){
	openCheck();
	$('#dialog_edit_user').dialog('open');
}
/*
 * 打开修改或浏览页面并进行初始化
 */
function updateOrBrowseUser(val){
	//关闭验证唯一
	closedCheck();
	var selRow = $('#datagrid_list_user').datagrid('getSelections');
	 if (selRow.length==0) {
		 $.messager.alert("提示", "请选择一行数据 ！", "info");
		 return;
	 }else if(selRow.length>=2){
		 $.messager.alert("提示", "一次只能操作一条信息 ！", "info");  
	     return; 
	 }else{
		var row = $("#datagrid_list_user").datagrid("getSelected");
		if(val==0){// 0--浏览  1--修改
			$("#panel_userDetail").panel({
				 href:"userDetail.html",
				 onLoad:function(){
					$('#form_detail_user').append(createDetailDIV(row));
					$("#dialog_detail_user").dialog("open");
				}
			})
	    }else{
	    	$('#form_edit_user').form('load',row);
	     	$("#dialog_edit_user").dialog("open");
	     }
	}
}
	
/*
 * 模糊查询
 */
function getUserByParams(){
	$("#datagrid_list_user").datagrid("load");
}

/*
 * 单条或批量删除用户
 */
function deleteUser() { 
	var rows = $('#datagrid_list_user').datagrid('getSelections');
    if (rows.length==0) { 
    	$.messager.alert("提示", "请选择要删除的行！", "info");  
        return; 
    }else{
    	var  UID="";
        for (var i = 0; i < rows.length; i++) {
        	if(UID==""){
        		UID=rows[i].uid;
        	}else{
        		UID=rows[i].uid+","+UID;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		Swet.request.delete(Swet.server.cloud+"/user/"+UID," ",function(result){ 
        			if (result.success){
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
        				$("#datagrid_list_user").datagrid("reload");
        				$('#datagrid_list_user').datagrid("clearSelections");
        			}
        		});
        	}
        });
    }  
}
	
/*
 * 确定添加或修改用户
 */
function saveUser(){
	var uid=$("#uid").val();
	var url=Swet.server.cloud+"/user";
	if(uid!=null&&uid>0){//uid>0--修改用户
		 url=Swet.server.cloud+"/user/"+uid;
	 }
	$("#form_edit_user").form("submit",{
		url:url,
		method:"POST",
		onSubmit:function(){
			return $(this).form('validate');
		},success:function(result){
			result=$.parseJSON(result);
			if (result.success){
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
				$('#dialog_edit_user').dialog("close");
				$("#datagrid_list_user").datagrid("reload");
			}
		}
	});
}
//打开验证
function openCheck(){
	$('#cmb_query_user_idNo').textbox({
		novalidate:false
	}); 
	
	$('#cell').textbox({
		novalidate:false
	});
}
//关闭验证
function closedCheck(){
	$('#cmb_query_user_idNo').textbox({
		novalidate:true
	}); 
	$('#cell').textbox({
		novalidate:true
	});
}


