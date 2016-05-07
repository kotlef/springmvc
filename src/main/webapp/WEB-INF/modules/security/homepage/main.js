/**
 * 主页菜单管理
 */
$(function(){
    $('#dialog_homepage').dialog({
        onClose:function(){
            $('#form_homepage').form('clear');
        }
    });
    $('#textbox_searchHomePage').textbox({
        onClickButton: function () {
            $("#homapage_datagrid").datagrid('reload');
        }
    });
    $("#homapage_datagrid").datagrid({
        url:Swet.server.cloud+'/homepage/toPage',
        checkOnSelect:false,
        onBeforeLoad:function(param){
            var homepageName =$('#textbox_searchHomePage').textbox('getValue');
            param.homepageName = homepageName;
        }
    });
});
/*
 * 打开添加页面
 */
function addHomePage(){
    $('#dialog_homepage').dialog({iconCls: 'icon-add'});
    $("#dialog_homepage").dialog("open");
}
//关闭主页添加窗口
function homeCancelEdit(){
    $('#dialog_homepage').dialog('close');
}
//保存主页菜单
function saveHomePage(){
    if(!$('#form_homepage').form('validate'))return;
    var homePage = $('#form_homepage').form('getData');
    $.messager.progress({
        title:'请稍后',
        msg:'系统正保存数据...'
    });
    Swet.request.save(Swet.server.cloud+"/homepage",homePage,
    function(result){
        if(result.success){
            $.messager.progress('close');
            $('#dialog_homepage').dialog('close');
            $('#homapage_datagrid').datagrid('reload');
            $.messager.show({
                title: "操作提示",
                msg: "保存成功!",
                showType: 'slide',
                timeout:3000,
                style:{
                    top:document.body.scrollTop+document.documentElement.scrollTop
                }
            });
        }
    });
}

/**
 * 主页界面表格列formatter
 */
function formatterHomepageAction(value,row,index){
    var ctrs = [
        '<span  title="修改" class="kit-img-btn kit-btn-edit" onclick="editHomePage('+index+');" ></span>',
        '<span  title="删除" class="kit-img-btn kit-btn-del" onclick="delHomePageRow('+index+');" ></span>'
    ];
    return ctrs.join('');
}
//action修改
function editHomePage(index){
    //选中当前行
    $('#homapage_datagrid').datagrid('uncheckAll');
    $('#homapage_datagrid').datagrid('unselectAll');
    $('#homapage_datagrid').datagrid('selectRow',index);
    $('#homapage_datagrid').datagrid('checkRow',index);
    var row = $("#homapage_datagrid").datagrid("getSelected");
    if(row){
        $('#dialog_homepage').dialog({iconCls: 'icon-edit'});
        $("#dialog_homepage").dialog("open");
        $("#dialog_homepage").dialog("setTitle","修改主页菜单");
        $('#form_homepage').form('load',row);
    }else{
        $.messager.alert('提示','请选择一行记录进行修改!','warning');
    }
}
//删除单个
function delHomePageRow(index){
    $('#homapage_datagrid').datagrid('uncheckAll');
    $('#homapage_datagrid').datagrid('unselectAll');
    $('#homapage_datagrid').datagrid('selectRow',index);
    $('#homapage_datagrid').datagrid('checkRow',index);
    var row = $("#homapage_datagrid").datagrid("getSelected");
    if(!row){
        $.messager.alert('提示','请选择要删除的行!','warning');
        return;
    }
    var rows = $('#homapage_datagrid').datagrid('getChecked');
    console.log(rows);
    deleteHomePage(rows);
}
//批量删除
function delHomePageRows(){
    var rows = $('#homapage_datagrid').datagrid('getChecked');
    if (rows.length==0) {
        $.messager.alert('提示','请选择要删除的行!','warning');
        return;
    }else{
        deleteHomePage(rows);
    }
}
/**
 * 删除记录
 */
function deleteHomePage(rows) {
    var  homepageIds="";
    for (var i = 0; i < rows.length; i++) {
        if(homepageIds==""){
            homepageIds=rows[i].homepageId;
        }else{
            homepageIds=rows[i].homepageId+","+homepageIds;
        }
    }
    $.messager.confirm('提示','你确定要删除选中的数据 ?',function(r){
        if (r){
            $.messager.progress({
                title:'请稍后',
                msg:'系统正删除数据...'
            });
            Swet.request.delete(
                Swet.server.cloud+"/homepage/"+homepageIds,"",function(result){
                    $.messager.progress('close');
                    $("#homapage_datagrid").datagrid("reload");
                    $.messager.show({
                        title: "操作提示",
                        msg: "删除成功！",
                        showType: 'slide',
                        timeout:3000,
                        style:{
                            top:document.body.scrollTop+document.documentElement.scrollTop
                        }
                    });
                });
        }else{
            $('#homapage_datagrid').datagrid('uncheckAll');
            $('#homapage_datagrid').datagrid('unselectAll');
        }
    });
}
