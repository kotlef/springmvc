var userInfo,currentUser,currentRoles;
var rolesIds = [];
userInfo=JSON.parse(Swet.dic.translate("currentUserInfo"));
currentUser=userInfo.user;
if(null != userInfo || userInfo != ""){
    currentRoles = userInfo.role;
    if(null != currentRoles || currentRoles != ""){
        for(var i=0;i<currentRoles.length;i++){
            rolesIds.push(currentRoles[i].roleId);
        }
    }
}
$(function(){
    $("#tree_list_menu").tree({
        url:Swet.server.cloud+'/menu/searchByRoleIds/toTree',
        method:"get",
        id:"id",
        text:"text",
        queryParams:{roleIds:rolesIds},
        onLoadSuccess: function (node,data) {
            //$("#tree_list_menu").tree("expandAll");//默认展开树的所有菜单
        },
        onClick: function(node){
            if(node.menuUrl==undefined||node.menuUrl==""){
                //console.log("菜单没有地址");
            }else{
                if(node.state=="open"){
                    createMenu(node);
                }else{
                    //console.log("该菜单不是末节点");
                }
            }
        }
    });
});
function createMenu(config){
    var title = config.text;
    if ($('#mainTabs').tabs('exists', title)){
        $('#mainTabs').tabs('select', title);
    }else{
        var url = "/springmvc/"+config.menuUrl;
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
        $('#mainTabs').tabs('add',{
            title: config.text,
            width:'200px',
            content:content,
            closable: true
        });
        //$('#testFrame').context.location.href = "/springmvc/"+config.menuUrl;
    }
}
function refreshTreeMenu(){
    $("#tree_list_menu").tree("reload");
}