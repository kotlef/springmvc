/**
 * 快速用户配置模块功能
 * 孟话然
 * 2015-10-27
 */
var addOrUp;//用于其他页面判断是新增还是修改。0代表只读，1代表新增，2代表修改。
var impoUID;//用户id，又保存或修改后回写改全局变量。

/**
 * 初始界面
 */
$(function(){
	$('#indexSaveButton').hide();
	$('#indexUpButton').hide();
	$('#indexFinalButton').hide();
	$('#indexLastButton').hide();
	$('#indexNextButton').hide();
	$('#indexLayout').hide();
	$('#indexWel').show();
	$("#indexTabs").tabs("disableTab",0);
	$("#indexTabs").tabs("disableTab",1);
	$("#indexTabs").tabs("disableTab",2);
	
})

/**
 * 显示添加页面
 */
function pageAdd(){
	addOrUp = 1;//属于新增操作
	$("#form_edit_user").form('clear');
	$('#indexLayout').show();
	$('#indexTabs').tabs('select',0);
	$("#indexTabs").tabs("enableTab",0);
	$('#indexWel').hide();
	$('#indexSaveButton').show();
}

/**
 * 得到tabs的index索引
 */
function getTabsIndex(){
	var tab = $('#indexTabs').tabs('getSelected');
	var index = $('#indexTabs').tabs('getTabIndex',tab);
	return index;
}

/**
 * 上一步
 */
function indexLast(){
	var index = getTabsIndex();
	switch(index)
	{
	case 1://岗位界面只读
		$('#indexFinalButton').hide();
		$('#indexUpButton').show();
		$('#indexLastButton').hide();
		$('#indexNextButton').show();
		$('#indexTabs').tabs('select',index-1);
		$("#indexTabs").tabs("disableTab",index);
		$('#indexTabs').tabs('enableTab',index-1);
		var tabs = $("#tabs_post").tabs("tabs");
		for(var a=0;a<tabs.length;a++){
			var tabindex = $("#tabs_post").tabs("getTabIndex",tabs[a]);
			$("#tabs_post").tabs("close",tabindex);
		}
		break;
	case 2://尾页
		$('#indexFinalButton').hide();
		$('#indexUpButton').show();
		$('#indexLastButton').show();
		$('#indexNextButton').show();
		$('#indexTabs').tabs('select',index-1);
		$("#indexTabs").tabs("disableTab",index);
		$('#indexTabs').tabs('enableTab',index-1);
		break;
	}
}

/**
 * 下一步
 */
function indexNext(){
	var index = getTabsIndex();
	switch(index)
	{
	case 0://用户界面只读
		$('#indexLastButton').show();
		addOrUp = 0;
		getPost();
		break;
	case 1://岗位界面只读
		$('#indexLastButton').show();
		addOrUp = 0;
		$('#indexTabs').tabs('select',index+1);
		$("#indexTabs").tabs("disableTab",index);
		$('#indexTabs').tabs('enableTab',index+1);
		$('#indexUpButton').hide();
		$('#indexNextButton').hide();
		$('#indexFinalButton').show();
		break;
	}
}

/**
 * 修改
 */
function indexUp(){
	var index = getTabsIndex();
	switch(index)
	{
	case 0://用户界面修改
		$('#indexLastButton').show();
		addOrUp = 2;
		if(!$("#form_edit_user").form('validate')){return;}
		saveOrUpdataUser();
		getPost();
		break;
	case 1://岗位界面修改
		$('#indexLastButton').show();
		addOrUp = 2;
		saveOrUpdatapost();
		break;
	}
}

/**
 * 提交
 */
function indexCommit(){
	var index = getTabsIndex();
	switch(index)
	{
	case 0://用户界面新增
		addOrUp = 1;
		if(!$("#form_edit_user").form('validate')){return;}
		saveOrUpdataUser();
		break;
	case 1://岗位界面新增
		addOrUp = 1;
		saveOrUpdatapost();
		break;
	}
}

/**
 * 完成
 */
function indexFinal(){
	$("#form_edit_user").form('clear');
	$("#treegrid_org_post").treegrid("uncheckAll");
	var tabs = $("#tabs_post").tabs("tabs");
	for(var a=0;a<tabs.length;a++){
		var tabindex = $("#tabs_post").tabs("getTabIndex",tabs[a]);
		$("#tabs_post").tabs("close",tabindex);
	}
	$('#indexSaveButton').hide();
	$('#indexUpButton').hide();
	$('#indexFinalButton').hide();
	$('#indexLastButton').hide();
	$('#indexNextButton').hide();
	$('#indexLayout').hide();
	$('#indexWel').show();
	$("#indexTabs").tabs("disableTab",0);
	$("#indexTabs").tabs("disableTab",1);
	$("#indexTabs").tabs("disableTab",2);
}
