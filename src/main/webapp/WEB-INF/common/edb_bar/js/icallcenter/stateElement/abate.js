hojo.provide("icallcenter.stateElement.abate");

hojo.declare("icallcenter.stateElement.abate", null, {
	constructor: function(base) {
		//console.debug("进入到失效状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	//状态名称
	_callState: "stAbate",
	
	_changeToolBarState: function(obj) {
		//console.debug("改变软电话条状态");
		hojo.publish("EvtCallToolBarChange",[obj._callState]);
	},
	
	_switchCallState:function(evtJson) {
		//console.debug("开始转换状态");
		if(evtJson.Event == "PeerStatus") {
			if(evtJson.Exten == this._base._phone.sipNo) {
				var isRegistered = false;
        		if(evtJson.PeerStatus == "Registered") {
        			isRegistered = true;
        		}
        		//console.debug("当前状态："+this._base._curCallState._callState);	
        		if (isRegistered && this._base._curCallState._callState == "stAbate"){
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