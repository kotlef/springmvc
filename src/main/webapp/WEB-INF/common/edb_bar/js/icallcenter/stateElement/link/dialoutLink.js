hojo.provide("icallcenter.stateElement.link.dialoutLink");

hojo.declare("icallcenter.stateElement.link.dialoutLink", null, {
	constructor: function(base) {
		//console.debug("进入到外呼通话状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	//状态名称
	_callState: "stDialTalking",
	
	_changeToolBarState:function(obj) {
		hojo.publish("EvtCallToolBarChange",[obj._callState]);
	},
	
	_switchCallState:function(evtJson) {
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.Exten == this._base._phone.sipNo) {
				if(evtJson.ChannelStatus == "Hangup") {
					//console.debug("挂机事件到达");
					this._base._curCallState = this._base._getInvalid();	
					this._changeToolBarState(this._base._curCallState);
				} else if(evtJson.ChannelStatus == "Link") {
					if(evtJson.LinkedChannel.ChannelType == "consultation") {
						this._base._curCallState = this._base._getConsultationLink();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "ThreeWayCall") {
						this._base._curCallState = this._base._getThreeWayCallLink();
						this._changeToolBarState(this._base._curCallState);
					} 
				} else if(evtJson.ChannelStatus == "hold") {
					this._base._curCallState = new icallcenter.stateElement.hold();
					this._changeToolBarState(this._base._curCallState);
				} 
			}
		}
	},

	_publish:function() {
		//console.debug("开始抛事件");
	}

});