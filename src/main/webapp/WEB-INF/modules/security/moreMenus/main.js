var userInfo,currentUser,currentRoles,globalMenu;
var rolesIds = [];
userInfo=JSON.parse(Swet.dic.translate("currentUserInfo"));
currentUser=userInfo.user;

$(function(){
	$("#panel_more_menus").empty();
	if(null != userInfo || userInfo != ""){
		currentRoles = userInfo.role;
		if(null != currentRoles || currentRoles != ""){
			for(var i=0;i<currentRoles.length;i++){
				rolesIds.push(currentRoles[i].roleId);
			}
		}
	}
	$.messager.progress({
		title:'请稍后',
		msg:'系统正加载数据...'
	});
	Swet.request.get(Swet.server.cloud + "/menu/searchByRoleIds",
		{roleIds:rolesIds},
		function(result){
			$.messager.progress('close');
			var menus = new Array();
			if(result.success){
				$.each(result.rows, function(index, item){
					$(item).attr("icon", "images/guide.png");
					menus.push(item);
					globalMenu = item;
				});
				createDeskTopMenu(menus);
			}
		}
	);
	//tab监听右键事件，创建右键菜单
	$('#more_menu_tab').tabs({
		onContextMenu:function(e, title,index){
			e.preventDefault();
			if(index>0){
				$('#more_menu_tab_mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				}).data("tabTitle", title);
			}
		},
		onSelect: function(title,index){
			switch (title) {
				case "导航菜单":
					$('#div_undo_levelMenu').show();
					$('#div_undo_indexMenu').hide();
					break;
				default:
					$('#div_undo_levelMenu').hide();
					$('#div_undo_indexMenu').show();
					break;
			}
		}
	});
	//右键菜单click
	$("#more_menu_tab_mm").menu({
		onClick : function (item) {
			closeTab(this, item.name);
		}
	});
});
//返回上一级菜单
function undoRefreshMenu(){
	$("#panel_more_menus").empty();
	if(globalMenu.menuLevel==0||globalMenu==undefined){
		$.messager.progress({
			title:'请稍后',
			msg:'系统正加载数据...'
		});
		Swet.request.get(Swet.server.cloud + "/menu/searchByRoleIds",
			{roleIds:rolesIds},
			function(result){
				var menus = new Array();
				$.messager.progress('close');
				if(result.success){
					$.each(result.rows, function(index, item){
						$(item).attr("icon", "images/guide.png");
						menus.push(item);
						globalMenu = item;
					});
					createDeskTopMenu(menus);
				}
			}
		);
	}else{
		$.messager.progress({
			title:'请稍后',
			msg:'系统正加载数据...'
		});
		Swet.request.get(Swet.server.cloud + "/menu/serchByMenuId/"+globalMenu.parentId,"",
			function(resultParentId){
				$.messager.progress('close');
				Swet.request.get(Swet.server.cloud + "/menu/serchByParentIdAndRoles",
					{id:resultParentId,
						roleIds:rolesIds
					},
					function(result){
						var menus = new Array();
						if(result.lenght!=0){
							$("#panel_more_menus").empty();
							$.each(result, function(index, item){
								$(item).attr("icon", "images/overlays.png");
								menus.push(item);
								globalMenu = item;
							});
							createDeskTopMenu(menus);
						}
					}
				);
			}
		);
	}
}
//返回导航菜单主页
function undoRefreshIndexMenu(){
	$('#more_menu_tab').tabs('select',0);
}
function createMenu(config){
	if(config.state=="closed"){
		$.messager.progress({
			title:'请稍后',
			msg:'系统正加载数据...'
		});
		Swet.request.get(Swet.server.cloud + "/menu/serchByParentIdAndRoles",
			{id:config.id, roleIds:rolesIds},
			function(result){
				$.messager.progress('close');
				var menus = new Array();
				if(result.lenght!=0){
					$("#panel_more_menus").empty();
					$.each(result, function(index, item){
						$(item).attr("icon", "images/overlays.png");
						menus.push(item);
						globalMenu = item;
					});
					createDeskTopMenu(menus);
				}
			}
		);
	}else{
		var menuUrl = config.menuUrl;
		if(menuUrl==""||menuUrl==undefined||menuUrl==null){
			$.messager.alert('提示','缺少菜单地址!','error');
		}else{
			var title = config.text;
			if ($('#more_menu_tab').tabs('exists', title)){
				$('#more_menu_tab').tabs('select', title);
			}else{
				var url = "/springmvc/"+config.menuUrl;
				var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
				$('#more_menu_tab').tabs('add',{
					title: config.text,
					width:'200px',
					content:content,
					closable: true
				});
				//$('#testFrame').context.location.href = "/springmvc/"+config.menuUrl;
			}
		}
	}
}
function createDeskTopMenu(data){
	var win_h = $(window).height();
	var cur_top = 50;
	var cur_left = 10;
	for(var idx=0;idx<data.length;idx++){
		var deskMenu = data[idx];
		//组合桌面图标
		var html = "<div id='icon_"+deskMenu.id+"' style='left:"+cur_left+"px;top:"+cur_top+"px;' class='deskicon' onclick='createMenu("+JSON.stringify(deskMenu)+")'>";
		html = html + "<div>";
		html = html + "<p><img src='"+deskMenu.icon+"'/><p>";
		html = html + "<em style='font-style:normal;font-weight:normal;color:black'>"+deskMenu.text+"</em>";
		html = html + "</div></div>";
		$("#panel_more_menus").append(html);
		//计算左边距和顶边距
		if((cur_top+110)>(win_h-140)){
			cur_top = 50;
			cur_left = cur_left + 110;
		}else{
			cur_top = cur_top+110;
		}
	}
}
var index = 0;
//删除Tabs
function closeTab(menu, type) {
	var allTabs = $("#more_menu_tab").tabs('tabs');
	var allTabtitle = [];
	$.each(allTabs, function (i, n) {
		var opt = $(n).panel('options');
		if (opt.closable)
			allTabtitle.push(opt.title);
	});
	var curTabTitle = $(menu).data("tabTitle");
	var curTabIndex = $("#more_menu_tab").tabs("getTabIndex", $("#more_menu_tab").tabs("getTab", curTabTitle));
	switch (type) {
		case 1://关闭当前
			$("#more_menu_tab").tabs("close", curTabIndex);
			return false;
			break;
		case 2://全部关闭
			for (var i = 0; i < allTabtitle.length; i++) {
				$('#more_menu_tab').tabs('close', allTabtitle[i]);
			}
			break;
		case 3://除此之外全部关闭
			for (var i = 0; i < allTabtitle.length; i++) {
				if (curTabTitle != allTabtitle[i])
					$('#more_menu_tab').tabs('close', allTabtitle[i]);
			}
			$('#more_menu_tab').tabs('select', curTabTitle);
			break;
		case 4://当前侧面右边
			for (var i = curTabIndex; i < allTabtitle.length; i++) {
				$('#more_menu_tab').tabs('close', allTabtitle[i]);
			}
			$('#more_menu_tab').tabs('select', curTabTitle);
			break;
		case 5: //当前侧面左边
			for (var i = 0; i < curTabIndex - 1; i++) {
				$('#more_menu_tab').tabs('close', allTabtitle[i]);
			}
			$('#more_menu_tab').tabs('select', curTabTitle);
			break;
	}
}
