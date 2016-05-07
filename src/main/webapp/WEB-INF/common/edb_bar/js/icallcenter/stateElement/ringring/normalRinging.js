hojo.provide("icallcenter.stateElement.ringring.normalRinging");

hojo.declare("icallcenter.stateElement.ringring.normalRinging", null, {
	constructor: function(base) {
		//console.debug("进入到普通振铃状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	//状态名称
	_callState: "stBelling",
	
	_changeToolBarState: function(obj) {
		//console.debug("改变软电话条状态");
		hojo.publish("EvtCallToolBarChange",[obj._callState]);
	},
	
	_switchCallState: function(evtJson) {
		//console.debug("开始转换状态");
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.Exten == this._base._phone.sipNo) {
				if(evtJson.ChannelStatus == "Hangup") {
					//console.debug("挂机事件到达");
					this._base._curCallState = this._base._getInvalid();	
					this._changeToolBarState(this._base._curCallState);
					if(this._base._phone._isRing) {
						this._base._phone.stopSound();
						this._base._phone._isRing = false;
					}
				} else if(evtJson.ChannelStatus == "Link") {
					if(evtJson.LinkedChannel.ChannelType == "normal") {
						this._base._curCallState = this._base._getNormalLink();
						this._changeToolBarState(this._base._curCallState);
						if(this._base._phone._isRing) {
							this._base._phone.stopSound();
							this._base._phone._isRing = false;
						}
					} else if(evtJson.LinkedChannel.ChannelType == "threeWayCall") {
						this._base._curCallState = this._base._getThreeWayCallLink();
						this._changeToolBarState(this._base._curCallState);
						if(this._base._phone._isRing) {
							this._base._phone.stopSound();
							this._base._phone._isRing = false;
						}
					}
					
					//旧版本用
					else if(evtJson.LinkedChannel.ChannelType == "transfer") {
						this._base._curCallState = this._base._getNormalLink();
						this._changeToolBarState(this._base._curCallState);
						if(this._base._phone._isRing) {
							this._base._phone.stopSound();
							this._base._phone._isRing = false;
						}
					}
					
				}
			}
		} 
	},

	_publish:function() {
		//console.debug("开始抛事件");
	}

});