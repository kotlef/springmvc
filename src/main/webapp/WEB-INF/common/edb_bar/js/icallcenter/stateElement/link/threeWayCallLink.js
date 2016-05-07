hojo.provide("icallcenter.stateElement.link.threeWayCallLink");

hojo.declare("icallcenter.stateElement.link.threeWayCallLink", null, {
	constructor: function(base) {
		//console.debug("进入到三方通话状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	//状态名称
	_callState: "stThreeWayTalking",
	
	_changeToolBarState:function(obj) {
		//console.debug("改变软电话条状态");
		hojo.publish("EvtCallToolBarChange",[obj._callState]);
	},
	
	_switchCallState:function(evtJson) {
		//console.debug("开始转换状态");
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.Exten == this._base._phone.sipNo) {
				if(evtJson.ChannelStatus == "Hangup") {
					//console.debug("挂机事件到达");
					this._base._curCallState = this._base._getInvalid();	
					this._changeToolBarState(this._base._curCallState);
				} 
			}
		}
	},

	_publish:function() {
		//console.debug("开始抛事件");
	}

});