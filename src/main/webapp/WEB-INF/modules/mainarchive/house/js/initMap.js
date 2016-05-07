/**
 * 模块说明
 * @module 房屋信息模块-初始化地图
 */
var house_handler = {},windowsArr = [],marker = [];
if("undefined" == typeof AMap){
	console.log("地图初始化失败!");
	$.messager.alert("提示","地图加载错误，请重新打开窗口!","warning");
}
var mapObjAdd = new AMap.Map("mapContainer", {
     resizeEnable: true,
     //center: [116.397428, 39.90923],//地图中心点(北京),默认当前地点
     zoom: 13,//地图显示的缩放级别
     keyboardEnable: false
});
//创建右键菜单
var contextMenu = new AMap.ContextMenu();
//右键放大
contextMenu.addItem("放大一级", function() {
	mapObjAdd.zoomIn();
}, 0);
//右键缩小
contextMenu.addItem("缩小一级", function() {
	mapObjAdd.zoomOut();
}, 1);
//右键显示全国范围
contextMenu.addItem("缩放至全国范围", function(e) {
	mapObjAdd.setZoomAndCenter(4, [108.946609, 34.262324]);
}, 2);
//地图绑定鼠标右击事件——弹出右键菜单
mapObjAdd.on( 'rightclick', function(e) {
    contextMenu.open(mapObjAdd, e.lnglat);
    contextMenuPositon = e.lnglat;
});
document.getElementById("keyword").onkeyup = keydown;
//输入提示
function autoSearch() {
    var keywords = document.getElementById("keyword").value;
    var auto;
    //加载输入提示插件
    AMap.service(["AMap.Autocomplete"], function() {
        var autoOptions = {
            city: "" //城市，默认全国
        };
        auto = new AMap.Autocomplete(autoOptions);
        //查询成功时返回查询结果
        if (keywords.length > 0) {
            auto.search(keywords, function(status, result) {
                autocomplete_CallBack(result);
            });
        }
        else {
            document.getElementById("result1").style.display = "none";
        }
    });
}

//输出输入提示结果的回调函数
function autocomplete_CallBack(data) {
    var resultStr = "";
    var tipArr = data.tips;
    if (tipArr && tipArr.length > 0) {
        for (var i = 0; i < tipArr.length; i++) {
            resultStr += "<div id='divid" + (i + 1) + "' onmouseover='openMarkerTipById(" + (i + 1)
                    + ",this)' onclick='selectResult(" + i + ")' onmouseout='onmouseout_MarkerStyle(" + (i + 1)
                    + ",this)' style=\"font-size: 13px;cursor:pointer;padding:5px 5px 5px 5px;\"" + "data=" + tipArr[i].adcode + ">" + tipArr[i].name + "<span style='color:#C1C1C1;'>" + tipArr[i].district + "</span></div>";
        }
    }
    else {
        resultStr = " π__π 亲,人家找不到结果!<br />要不试试：<br />1.请确保所有字词拼写正确<br />2.尝试不同的关键字<br />3.尝试更宽泛的关键字";
    }
    document.getElementById("result1").curSelect = -1;
    document.getElementById("result1").tipArr = tipArr;
    document.getElementById("result1").innerHTML = resultStr;
    document.getElementById("result1").style.display = "block";
}

//输入提示框鼠标滑过时的样式
function openMarkerTipById(pointid, thiss) {  //根据id打开搜索结果点tip
    thiss.style.background = '#CAE1FF';
}

//输入提示框鼠标移出时的样式
function onmouseout_MarkerStyle(pointid, thiss) {  //鼠标移开后点样式恢复
    thiss.style.background = "";
}

//从输入提示框中选择关键字并查询
function selectResult(index) {
    if (index < 0) {
        return;
    }
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        document.getElementById("keyword").onpropertychange = null;
        document.getElementById("keyword").onfocus = focus_callback;
    }
    //截取输入提示的关键字部分
    var text = document.getElementById("divid" + (index + 1)).innerHTML.replace(/<[^>].*?>.*<\/[^>].*?>/g, "");
    var cityCode = document.getElementById("divid" + (index + 1)).getAttribute('data');
    document.getElementById("keyword").value = text;
    document.getElementById("result1").style.display = "none";
    //根据选择的输入提示关键字查询
    mapObjAdd.plugin(["AMap.PlaceSearch"], function() {
        var msearch = new AMap.PlaceSearch();  //构造地点查询类
        AMap.event.addListener(msearch, "complete", placeSearch_CallBack); //查询成功时的回调函数
        msearch.setCity(cityCode);
        msearch.search(text);  //关键字查询查询
    });
}

//定位选择输入提示关键字
function focus_callback() {
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        document.getElementById("keyword").onpropertychange = autoSearch;
    }
}

//输出关键字查询结果的回调函数
function placeSearch_CallBack(data) {
    //清空地图上的InfoWindow和Marker
    windowsArr = [];
    marker = [];
    mapObjAdd.clearMap();
    var resultStr1 = "";
    var poiArr = data.poiList.pois;
    var resultCount = poiArr.length;
    for (var i = 0; i < resultCount; i++) {
        resultStr1 += "<div id='divid" + (i + 1) + "' onmouseover='openMarkerTipById1(" + i + ",this)' onmouseout='onmouseout_MarkerStyle(" + (i + 1) + ",this)' style=\"font-size: 12px;cursor:pointer;padding:0px 0 4px 2px; border-bottom:1px solid #C1FFC1;\"><table><tr><td><img src=\"http://webapi.amap.com/images/" + (i + 1) + ".png\"></td>" + "<td><h3><font color=\"#00a6ac\">名称: " + poiArr[i].name + "</font></h3>";
        resultStr1 += createContent(poiArr[i].type, poiArr[i].address, poiArr[i].tel) + "</td></tr></table></div>";
        addmarker(i, poiArr[i]);
    }
    mapObjAdd.setFitView();
}

//鼠标滑过查询结果改变背景样式，根据id打开信息窗体
function openMarkerTipById1(pointid, thiss) {
    thiss.style.background = '#CAE1FF';
    windowsArr[pointid].open(mapObj, marker[pointid]);
}

//添加查询结果的marker&infowindow
function addmarker(i, d) {
    var lngX = d.location.getLng();
    var latY = d.location.getLat();
    var markerOption = {
        map: mapObjAdd,
        icon:"http://webapi.amap.com/theme/v1.3/markers/n/mark_b"+(i+1)+".png",
        position: [lngX, latY]
    };
    var mar = new AMap.Marker(markerOption);
    marker.push([lngX, latY]);

    var infoWindow = new AMap.InfoWindow({
        content: "<h3><font color=\"#00a6ac\">  " + (i + 1) + ". " + d.name + "</font></h3>" + createContent(d.type, d.address, d.tel),
        size: new AMap.Size(300, 0),
        autoMove: true,
        offset: new AMap.Pixel(0, -30)
    });
    windowsArr.push(infoWindow);
    var aa = function(e) {
        infoWindow.open(mapObjAdd, mar.getPosition());
    };
    mar.on( "mouseover", aa);
}

//infowindow显示内容
function parseStr(p){
    if(!p || p == "undefined" || p == " undefined"||p=="tel"){
        p="暂无";
    }
    return p;
}
function createContent(type, address, tel) {  //窗体内容
    type=parseStr(type);
    address=parseStr(address);
    tel=parseStr(tel);
    var s=[];
    s.push("地址：" +address);
    s.push("电话：" +tel);
    s.push("类型：" +type);
    return s.join("<br>");
}
function keydown(event) {
    var key = (event || window.event).keyCode;
    var result = document.getElementById("result1")
    var cur = result.curSelect;
    if (key === 40) {//down
        if (cur + 1 < result.childNodes.length) {
            if (result.childNodes[cur]) {
                result.childNodes[cur].style.background = '';
            }
            result.curSelect = cur + 1;
            result.childNodes[cur + 1].style.background = '#CAE1FF';
            document.getElementById("keyword").value = result.tipArr[cur + 1].name;
        }
    } else if (key === 38) {//up
        if (cur - 1 >= 0) {
            if (result.childNodes[cur]) {
                result.childNodes[cur].style.background = '';
            }
            result.curSelect = cur - 1;
            result.childNodes[cur - 1].style.background = '#CAE1FF';
            document.getElementById("keyword").value = result.tipArr[cur - 1].name;
        }
    } else if (key === 13) {
        var res = document.getElementById("result1");
        if (res && res['curSelect'] !== -1) {
            selectResult(document.getElementById("result1").curSelect);
        }
    } else {
        autoSearch();
    }
}
house_handler.mapObjAdd = mapObjAdd;
AMap.event.addListener(mapObjAdd,'click',function getLnglat(e) {
	var x=e.lnglat.getLng();
	var y=e.lnglat.getLat();
	house_handler.marker.setPosition(new AMap.LngLat(x,y));
	$('#txt_house_longitude').textbox('setValue',x);
	$('#txt_house_dimension').textbox('setValue',y);
});
mapObjAdd.plugin(["AMap.ToolBar", "AMap.OverView", "AMap.Scale","AMap.MapType"], function(){
	mapObjAdd.addControl(new AMap.ToolBar);
	mapObjAdd.addControl(new AMap.OverView({isOpen: true}));
	mapObjAdd.addControl(new AMap.Scale);
	mapObjAdd.addControl(new AMap.MapType({defaultType:0}));
});
house_handler.marker = new AMap.Marker({
	map : house_handler.mapObjAdd
});
function getData(e){
	  var dList = e.districtList;
    for(var m = 0,ml = dList.length; m < ml; m++){
      var data = e.districtList[m].level;
      var bounds = e.districtList[m].boundaries;
		//只绘制 区, 且 本级别行政区划是上一级区划的下级行政区
      if(data == "district" && dList[m].citycode === citycode){
        if(bounds) {
          for(var i =0, l = bounds.length; i < l; i++){
            //生成行政区划polygon
            var polygon = new AMap.Polygon({
              map:mapObjAdd,
              strokeWeight:1,
              strokeColor:'#CC66CC',
              fillColor:'#CCF3FF',
              fillOpacity:0.7,
              path:bounds[i]
            });
            polygons.push(polygon);
          }
          mapObjAdd.setFitView();//地图自适应
        }
      }

      var list = e.districtList || [],
          subList =[], level, nextLevel;
      if(list.length >= 1) {
        subList = list[0].districtList;
        level = list[0].level;
      }

      //清空下一级别的下拉列表
      if(level === 'province'){
        
        nextLevel = 'city';
        citySelect.innerHTML = '';
        districtSelect.innerHTML = '';
        areaSelect.innerHTML = '';
      }else if(level === 'city'){

        nextLevel = 'district';
        districtSelect.innerHTML = '';
        areaSelect.innerHTML = '';
      } else if(level === 'district') {
          
          nextLevel = 'biz_area';
          areaSelect.innerHTML = '';
      }

      if(subList){
        var contentSub = '<option>--请选择--</option>';
        for(var i=0,l=subList.length; i<l; i++){
          var name = subList[i].name; 
          var levelSub = subList[i].level;
          var cityCode = subList[i].citycode;
          contentSub += '<option value="'+levelSub+'|'+cityCode+'">'+name+'</option>';
          document.querySelector('#'+levelSub).innerHTML = contentSub;
        }
      }
    } 
};
function search(obj){
	console.log(obj);
  //清除地图上所有覆盖物
  for(var i = 0, l = polygons.length; i < l; i ++){
    polygons[i].setMap(null);
  }
  
  var option = obj[obj.options.selectedIndex];
  var arrTemp = option.value.split('|');
  var level = arrTemp[0];//行政级别
  citycode = arrTemp[1];// 城市编码
  var keyword = option.text; //关键字

  district.setLevel(level); //行政区级别
  //行政区查询
  district.search(keyword, function(status, result){
  	getData(result);
  }); 
};