/**
 * 员工信息
 */
//用户信息界面:使用组件的validType属性对手机号、邮箱、证件号进行唯一性的异步验证
var checkEmailUrl,checkCellUrl,checkIdNoUrl;
checkEmailUrl = Swet.server.cloud+'/user/checkEmail';checkEmail = 'email';//邮箱验证
checkCellUrl = Swet.server.cloud+'/user/checkCell';checkCell = 'cell';//手机号验证
//证件号验证
checkIdNo = 'idNo';
checkIdNoUrl = Swet.server.cloud+'/user/checkIdNo';
var checkEmpCodeUrl =Swet.server.cloud+'/emp/checkEmpCode';checkempCode='empCode';
var currentUser,orgIds,currentOrg,currentRole;
$(function(){
	translateCombo();
	$('#cmb_add_idType').combobox({
		url:Swet.server.mainArchive+'/wordbook/list/ID_TYPE',
		method:'GET',
		onLoadSuccess: function(){
			$('#cmb_add_idNo').combobox("select","0");
		}
	});
	$('#cmb_search_emp_empType').combobox('clear');
	$("#datagrid_list_emp").datagrid({
		title:'员工管理',
		method:'get',
		iconCls: 'icon-view',
		nowrap:true,
		autoRowHeight: false,
        striped: true,
        collapsible: false,//是否可折叠
        pagination: true,
		pageSize:25,
		pageList:[15,25,35],
        rownumbers:true,
        sortOrder: 'asc',
        remoteSort: false,
        fitColumns:true,
        checkOnSelect:false,
        singleSelect:true,
        selectOnCheck:false,
        loadMsg:'数据加载中...',
        columns:[[
                 {title:'用户内码',field:'uid',width:60,hidden:'true'},
                 {title:'员工姓名',field:'userName',width:60},
                 {title:'员工工号',field:'empCode',width:60},
                 {title:'工号密码',field:'empPassword',width:40},
                 {title:'员工类型',field:'empType',width:40,formatter:translateEmpType},
                 {title:'证件号',field:'idNo',width:100},
                 {title:'联系电话',field:'cell',width:80},
                 {title:'邮箱',field:'email',width:100},
                 {title:'状态',field:'status',width:30,formatter: translateStatus},
                 {title:'个人简历',field:'resume',width:100},
                 {title:'备注',field:'note',width:100},
                 {title:'操作列',field:'action',formatter: formatterActionColumns},
                 {field:'cl',checkbox:true}
                 ]],
             toolbar:'#tb_emp',
	         onRowContextMenu:showMenu,//右键菜单事件
		     onBeforeLoad:function(param){
				var value = $.trim($("#searchbox_emp").searchbox("getValue"));
				if(!value == ""){
					$('#datagrid_list_emp').datagrid('options').url = Swet.server.cloud+ '/emp/searchBykeyValue/toPage';
					param.value=value;
				}else{
					$('#datagrid_list_emp').datagrid('options').url = Swet.server.cloud+ '/emp/toPage';
				}
			} 
	});
});
/**
 * 联动加载角色
 */
function loadRole(org){
	var roleIds= org.id;
	$("#cmb_add_emp_roleId").combobox({
		  valueField:'roleId',
		  textField:'roleName',
		  prompt:'请选择',
		  method:'get',
		  url:Swet.server.cloud+'/orgrole/findByOrgidToRole/'+roleIds,
		  required:true
	});
}

//翻译员工的类型
function translateEmpType(empType){
	return Swet.dic.translate("EMP_TYPE_"+empType);
}
//翻译婚姻状况
function translateMarriage(marriage){
	return Swet.dic.translate("MARRIAGE_"+marriage);
}
//翻译血型
function translateBlood(blood){
	return Swet.dic.translate("BLOOD_"+blood);
}
//翻译性别
function translateGender(gender){
	return Swet.dic.translate("GENDER_"+gender);
}
//翻译学历
function translateEducation(education){
	return Swet.dic.translate("EDUCATION_"+education);
}
//翻译员工岗级
function translatePostRank(postRank){
	return Swet.dic.translate("POST_RANK_"+postRank);
}
//翻译状态
function translateStatus(status){
	return Swet.dic.translate("STATUS_"+status);
}
function formatterActionColumns(val,row,index){
	//index:当前行的索引
   var ctrs = [
      '<span  title="修改" class="kit-img-btn kit-btn-edit" onclick="openUpdate('+index+');" ></span>',
      '<span  title="删除" class="kit-img-btn kit-btn-del" onclick="deleteRow('+index+');" ></span>'
      ];
   return ctrs.join('');
}
var flag;//判断保存 (添加 修改)
//打开添加界面
function openAdd(){
	openCheck();
	selectIdType();
	$('#txb_add_emp_empPassword').textbox({
		editable:true
	});
	$("#dialog_add_emp").dialog("open");
	$("#dialog_add_emp").dialog("setTitle","添加员工");
	$('#numb_add_cell').numberbox({
		validType:['mobile','remote[checkCellUrl,checkCell]'] 
	});
	flag= "add";
	return;
}
//打开修改界面
function openUpdate(index){
	colsedCheck();
	$('#txb_add_emp_empPassword').textbox({
		editable:false
	});
	translateCombo();
	$('#numb_add_cell').numberbox({
		validType:'mobile'
	});
	$('#idNo').textbox({
		validType:'idcard'
	});
	//选中当前行
	$('#datagrid_list_emp').datagrid('selectRow',index);
	var arr =$("#datagrid_list_emp").datagrid("getSelections");
	if(arr.length !=1){
		toptip('请选择一行记录进行修改!');
	}else{
		$("#dialog_add_emp").dialog("open");
		$("#dialog_add_emp").dialog("setTitle","修改员工");
		flag="update";
		//选中当前行
		$('#datagrid_list_emp').datagrid('selectRow',index);
		var row = $("#datagrid_list_emp").datagrid("getSelected");
		if(row){
			$('#form_add_emp').form('load',row);
		}
	}
}
/**
 * 保存员工的方法
 */
function saveEmp(){
	if(!$('#form_add_emp').form('validate')){
		return;
	}
	if(flag == "add"){
		saveAdd();
	}else if(flag == "update"){
		saveUpdate();
	}
}
/**
 * 添加员工
 */
function saveAdd(){
	var obj = $("#form_add_emp").form("getData");
	obj.parentId=0;
	$.messager.progress({
		text:'数据保存中,请稍候.....'
	});
	Swet.request.save(
			Swet.server.cloud +'/emp',  //提交的路径
			obj,
    		function(result){
				$.messager.progress('close');
				successCallback(result);
			},
			function(result){
				$.messager.progress('close');
				errorCallback(result);
			}
	);
}
/**
 * 修改员工
 */
function saveUpdate(){
		var obj = $("#form_add_emp").form("getData");
		var row = $("#datagrid_list_emp").datagrid("getSelected");
		obj.parentId = row.parentId;
		$.messager.progress({
			text:'数据保存中,请稍候.....'
		});
		Swet.request.update(
				Swet.server.cloud +'/emp/'+row.uid,  //提交的路径
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
//单行删除
function deleteRow(index){
	//选中当前行
	$('#datagrid_list_emp').datagrid('selectRow',index);
	var rows =$("#datagrid_list_emp").datagrid("getSelections");
	deleteEmp(rows);
}
//批量删除
function deleteRows(){
	var rows = $('#datagrid_list_emp').datagrid('getChecked');
	deleteEmp(rows);
}
/**
 * 删除员工
 */
function deleteEmp(rows) {
    if (rows.length==0) { 
    	toptip('请选择要删除的行!');
        return; 
    }else{
    	var  uids="";
        for (var i = 0; i < rows.length; i++) {
        	if(uids==""){
        		uids=rows[i].uid;
        	}else{
        		uids=rows[i].uid+","+uids;
			}
        }
        $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        	if (r){
        		$.messager.progress({
        			text:'数据删除中,请稍候.....'
        		});
        		Swet.request.delete(Swet.server.cloud+"/emp/"+uids,uids,function(result){ 
        			$.messager.progress('close');
	        	    toptip('删除成功!');
	        	    $("#datagrid_list_emp").datagrid("reload");
        		});
        	}
        });
    }
}
/**
 * 失败时提示方法
 */
function errorCallback(result){
	if(result.error){
		$.messager.alert({  
            title: 'Error',  
            msg: '操作失败' 
       });
	}
}
/**
 * 返回成功后执行方法
 */
function successCallback(result){
	if(result.success){
		$("#dialog_add_emp").dialog("close");          // 关闭dialog  
        $("#datagrid_list_emp").datagrid("reload");    // 加载数据 
	}
}
/**
 * 右键时触发事件
 */
var showMenu=function(e, rowIndex, rowData){
    e.preventDefault(); //阻止浏览器捕获右键事件
    $(this).datagrid("clearSelections"); //取消所有选中项
    $(this).datagrid("selectRow", rowIndex); //根据索引选中该行
    $('#menu_list_emp').menu('show', {
        left: e.pageX,//在鼠标点击处显示菜单
        top: e.pageY
    });
}
/**
 * 绑定搜索按钮的的点击事件
 */
function doSearch(){
	$("#datagrid_list_emp").datagrid("load");
}
/**
 * 取消时关闭dailog函数
 */
function closeForm(){
	$('#dialog_add_emp').dialog('close');
}
/**
 * 清除表单中的数据
 */
function clearDate(){
	$('#dialog_add_emp').form("clear");
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
//加载下拉框选项
function translateCombo(){
	//性别
	$('#cmb_add_gender').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/GENDER'
	});
	//直接领导
	$('#cmb_add_emp_parentId').combobox({
		method:'get',
		textField:'userName',
		valueField:'uid',
		prompt:'请选择',
		url:Swet.server.cloud+ '/emp/searchByPostRank'
	});
	//员工类型
	$('#cmb_add_empType').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/EMP_TYPE'
	});
	//婚姻情况
	$('#txb_add_marriage').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/MARRIAGE'
	});
	//血型
	$('#dtb_add_blood').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/BLOOD'
	});
	//教育
	$('#txb_add_emp_education').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/EDUCATION'
	});
	//岗级
	$('#cmb_add_emp_postRank').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/POST_RANK'
	});
	//状态
	$('#cmb_add_status').combobox({
		method:'get',
		textField:'wordDisplay',
		valueField:'wordValue',
		prompt:'请选择',
		url:Swet.server.mainArchive+'/wordbook/list/STATUS'
	});
	//组织
	$('#cmt_add_emp_orgid').combotree({
		valueField:'id',
		textField:'tex',
		prompt:'请选择',
		method:'get',
		url:Swet.server.cloud+ '/org/serchByParentId',
		required:true,
		onSelect:loadRole
	});
}
/**
 * 选择员工浏览详细
 */
function detailed(){
	var row =$("#datagrid_list_emp").datagrid("getChecked");
	if(row.length !=1){
		toptip('请选择一行记录进行浏览!');
	}else{
		var row = $("#datagrid_list_emp").datagrid("getChecked");
		$("#panel_empDetail").panel({
			 href:"empDetail.html",
			 onLoad:function(){
				$('#form_detail_emp').append(createDetailDIV(row[0]));
				$("#dialog_detail_emp").dialog("open");
			}
		});
	}
}
/**
 * 员工详情浏览界面
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
				'<td style="width:60px;text-align:right">'+ "员工工号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.empCode) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "工号密码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.empPassword) + '</td>'+
				'<td style="width:70px;text-align:right">'+ "员工名称:" + '</td>'+
				'<td style="width:160px;text-align:left;border-bottom:1px solid #ccc">'+ row.userName+ '</td>'+
				'<td style="width:70px;text-align:right">'+ "员工类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ userType + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:70px;text-align:right">'+ "性别:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+gender +  '</td>'+
				'<td style="width:60px;text-align:right">'+ "婚姻:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+marriage+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "证件类型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ idType + '</td>'+
				'<td style="width:60px;text-align:right">'+ "证件号码:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.idNo + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "邮箱:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.email + '</td>'+
				'<td style="width:60px;text-align:right">'+ "学历:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ education + '</td>'+
				'<td style="width:60px;text-align:right">'+ "联系手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.cell+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "备用手机:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ row.cell2 + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "血型:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ blood + '</td>'+
				'<td style="width:60px;text-align:right">'+ "状态:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ status + '</td>'+
			'</tr>'+
			'<tr>'+
			'<td style="width:60px;text-align:right">'+ "个人简历:" + '</td>'+
			'<td colspan="5" style="width:150px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.resume) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "备注信息:" + '</td>'+
				'<td colspan="5" style="width:150px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.note) + '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "籍贯:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.birthPlace)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "户籍地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.addr1) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "现居地址:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.addr2) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "生日:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.birthdate)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "毕业学校:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.gradSchool)+ '</td>'+
				'<td style="width:60px;text-align:right">'+ "专业:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.major) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "毕业时间:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.graduationDate)+ '</td>'+
				
				'<td style="width:60px;text-align:right">'+ "民族:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(nationality)+ '</td>'+
			'</tr>'+
			'<tr>'+
				'<td style="width:60px;text-align:right">'+ "入职时间:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.entryDate) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "离职时间:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+  getValue(row.departDate) + '</td>'+
				'<td style="width:60px;text-align:right">'+ "QQ号:" + '</td>'+
				'<td style="width:50px;text-align:left;border-bottom:1px solid #ccc">'+ getValue(row.qq) + '</td>'+
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
function closeDetile(){
	$('#dialog_detail_emp').dialog('close');
}
//设置毕业时间
function setGraduationDate(newDate,oldDate){
	var birthdate = new Date(newDate);;
    $('#graduationDate').datebox().datebox('calendar').calendar({
        validator: function(date){
            return birthdate<date;
        }
    });
    $('#dtb_add_emp_entryDate').datebox().datebox('calendar').calendar({
        validator: function(date){
            return birthdate<date;
        }
    });
}
//离职时间设置
function setDepartDate(newDate, oldDate){
	var entryDate = new Date(newDate);
    $('#dtb_add_emp_departDate').datebox().datebox('calendar').calendar({
        validator: function(date){
            return entryDate<=date;
        }
    });
}
//选择证件类型
function selectIdType(){
	$('#cmb_add_idType').combobox({
		onChange: function(record,old){
			switch (record) {
			case "0":
				//证件类型-身份证
				$('#cmb_add_idNo').textbox({
					validType:['idcard','remote[checkIdNoUrl,checkIdNo]']
				}); 
				break;
			case "C":
				//证件类型-护照
				$('#cmb_add_idNo').textbox({
					validType:['passport','remote[checkIdNoUrl,checkIdNo]']
				}); 
				break;
			case "P":
				//证件类型-军官证
				$('#cmb_add_idNo').textbox({
					validType:['officerCertificate','remote[checkIdNoUrl,checkIdNo]']
				});
				break;
			default:
				break;
			}
		}
	});
}
function openCheck(){
	$('#txb_add_emCode').textbox({
		novalidate:false
	});
	$('#cmb_add_idNo').textbox({
		novalidate:false
	});
	$('#numb_add_cell').numberbox({
		novalidate:false
	});
}
function colsedCheck(){
	$('#txb_add_emCode').textbox({
		novalidate:true
	});
	$('#cmb_add_idNo').textbox({
		novalidate:true
	});
	$('#numb_add_cell').numberbox({
		novalidate:true
	});
}

