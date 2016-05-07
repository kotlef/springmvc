/**
 * 用户界面模块功能
 * 孟话然
 * 2015-10-27
 */
//远程验证参数
var checkEmailUrl,checkCellUrl,checkIdNoUrl;
	checkEmailUrl = Swet.server.cloud+'/user/checkEmail';checkEmail = 'email';//邮箱验证
	checkCellUrl = Swet.server.cloud+'/user/checkCell';checkCell = 'cell';//手机号验证
	checkIdNoUrl = Swet.server.cloud+'/user/checkIdNo';checkIdNo = 'idNo';//证件号验证

/**
 * 用户其他地址编辑
 */
function addAddressAddr1(e) {
	addCustomerAddress("addr1","户籍地址");
}
function addAddressAddr2(e) {
	addCustomerAddress("addr2","现居住地址");
}
function addAddressAddr3(e) {
	addCustomerAddress("addr3","通讯地址");
}
function addCustomerAddress(key,addr) {
	$('#dialog_edit_addr').dialog("open").dialog('setTitle','添加'+addr);
	$("#form_edit_addr").form('load',{
		keyAddr:key
	});
}
function saveAddress() {
	var values = $('#form_edit_addr').form('getData');
	var region = $('#addr_region').combobox('getText');
	var province = $('#addr_province').combobox('getText');
	var city = $('#addr_city').combobox('getText');
	var district = $('#addr_district').combobox('getText');
	var community = $('#addr_community').combobox('getText');
	var town = $('#addr_town').combobox('getText');
	var addrValue = region+province+city+district+town+community; 
	switch(values.keyAddr){
		case "addr1":
			$('#addr1').textbox('setValue',addrValue);
		break;
		case "addr2":
			$('#addr2').textbox('setValue',addrValue);
		break;
		case "addr3":
			$('#addr3').textbox('setValue',addrValue);
		break;
	}
	$('#dialog_edit_addr').dialog("close");
}
	
	
/**
 * 搜索用户
 */
function getUser(value){
	if(value==""){
		$.messager.show({
			title:'提示',
			msg:'请输入关键字查询.',
			showType:'slide',
			timeout:3000,
			style:{
				top:document.body.scrollTop+document.documentElement.scrollTop,
			}
		});
		return;
	}
	$.messager.progress({
		title:'请稍后',
		msg:'正在查询数据...'
	});
	Swet.request.get(
		Swet.server.cloud+"/user/searchByKeyValue",
		{key:$('#SearchBox').searchbox('getName'),
		 value:value},
		function(result){
			if(result.success){
				$("#form_edit_user").form('clear');
				$("#form_edit_user").form('load',result.rows);
				//用户id
				impoUID = $("#uid").val();
				Swet.request.get(
					Swet.server.cloud+"/emp/searchByUid/"+impoUID,
					{uid:impoUID},
					function(emp){
						$.messager.progress('close');
						if(emp.success){
							$("#form_edit_user").form('load',emp.rows);
							$('#idNo').textbox("disableValidation");
							$('#idNo').textbox('readonly',true);
							$('#email').textbox("disableValidation");
							$('#email').textbox('readonly',true);
							$('#cell').textbox("disableValidation");
							$('#cell').textbox('readonly',true);
							$('#indexLayout').show();
							$('#indexTabs').tabs('select',0);
							$("#indexTabs").tabs("enableTab",0);
							$('#indexWel').hide();
							$('#indexNextButton').show();
							$('#indexUpButton').show();
							
						}else{
							$.messager.show({
								title:'提示',
								msg:"该用户不是员工",
								showType:'slide',
								timeout:3000,
								style:{
									top:document.body.scrollTop+document.documentElement.scrollTop,
								}
							});
						}
				});
			}else{
				$.messager.progress('close');
				$.messager.show({
					title:'提示',
					msg:"未查询到该员工",
					showType:'slide',
					timeout:3000,
					style:{
						top:document.body.scrollTop+document.documentElement.scrollTop,
					}
				});
			}
		})
}

/**
 * 保存用户
 */
function saveOrUpdataUser(){
	$.messager.progress({
		title:'请稍后',
		msg:'正在整理和保存数据...'
	});
	var obj = $("#form_edit_user").form('getData');
	obj.userType = '0';//固定员工
	obj.empType = '0';//固定员工
	obj.parentId = '0'//上级id
	if(addOrUp==2 && $("#uid").val()!=null){
		url = Swet.server.cloud+"/emp/"+$("#uid").val();
		impoUID = $("#uid").val();
		addOrUp=2;
	}else{
		url = Swet.server.cloud+"/emp";
		delete obj.uid;
		addOrUp=1;
	}
	Swet.request.save(
		url, 
		obj,
		function(result){
			$.messager.progress('close');
			//用户id
			if(addOrUp == 1){
				impoUID = result.rows;
				var index = getTabsIndex();
				$('#indexTabs').tabs('select',index+1);
				$("#indexTabs").tabs("disableTab",index);
				$('#indexTabs').tabs('enableTab',index+1);
			}
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
			}else{
				$.messager.alert("提示", result.msg, "info"); 
			}
		}
	);
}