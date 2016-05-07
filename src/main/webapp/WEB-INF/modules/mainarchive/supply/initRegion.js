/**
 * 模块说明
 * @module 房屋信息
 */
$(function(){
	//房屋添加界面(选择区域)
	$('#combobox_region_house').combobox({
		url:Swet.server.mainArchive+'/region/'+0,
		method:'GET',
		onLoadSuccess: function(){
			$('#combobox_region_house').combobox("select","5");
		},
		onSelect: function(record){
			Swet.request.get(Swet.server.mainArchive+"/region/"+record.regionId,"",
				function(data){
					$('#combobox_city_house').combobox('clear');
					$('#combobox_district_house').combobox('clear');
					//$('#customer_add_house_community').combobox('clear');
					$('#combobox_province_house').combobox('loadData',data);
					$('#combobox_province_house').combobox('setValue', '');
				}
			);
		}
	});
	//房屋添加界面(选择省份)
	$('#combobox_province_house').combobox({
		onSelect: function(record){
			Swet.request.get(Swet.server.mainArchive+"/region/"+record.regionId,"",
				function(data){
					$('#combobox_district_house').combobox('clear');
					//$('#customer_add_house_community').combobox('clear');
					$('#combobox_city_house').combobox('loadData',data);
					$('#combobox_city_house').combobox('setValue', '');
				}
			);
		}
	});
	//房屋添加界面(选择城市)
	$('#combobox_city_house').combobox({
		onSelect: function(record){
			Swet.request.get(Swet.server.mainArchive+"/region/"+record.regionId,"",
				function(data){
					//$('#customer_add_house_community').combobox('clear');
					$('#combobox_district_house').combobox('loadData',data);
					$('#combobox_district_house').combobox('setValue', '');
				}
			);
		}
	});
	//房屋添加界面(选择区（如：天河区）)
	/*$('#combobox_district_house').combobox({
		onSelect: function(record){
			var districtId = $('#combobox_district_house').combobox('getValue');
			Swet.request.get(Swet.server.mainArchive+"/community/searchByDistrictId/"+districtId,"",
				function(data){
					$('#customer_add_house_town').combobox('loadData',data);
					$('#customer_add_house_town').combobox('setValue', '');
				}
			);
			Swet.request.get(Swet.server.mainArchive+"/community/existingTown?districtId="+districtId,"",
				function(data){
					$('#customer_add_house_town').combobox('loadData',data);
					$('#customer_add_house_town').combobox('setValue', '');
				}
			);


		}
	});
	//房屋添加界面(选择街道)
	$('#customer_add_house_town').combobox({
		onLoadSuccess:function(){
		},
		onSelect: function(record){
			Swet.request.get(Swet.server.mainArchive+"/community/searchByTown",{
					town:record.town
				},
				function(data){
					$('#customer_add_house_community').combobox('loadData',data);
					$('#customer_add_house_community').combobox('setValue','');
					$('#customer_add_house_community').combobox('setText','');
				}
			);
		}
	});*/
});