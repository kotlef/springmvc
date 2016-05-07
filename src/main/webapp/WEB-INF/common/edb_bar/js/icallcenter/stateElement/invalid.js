hojo.provide("icallcenter.stateElement.invalid");

hojo.declare("icallcenter.stateElement.invalid", null, {
	
	constructor: function(base) {
		//console.debug("进入到无效状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	//状态名称
	_callState: "stInvalid",
	
	_changeToolBarState: function(obj) {
		hojo.publish("EvtCallToolBarChange",[obj._callState]);
	},
	
	//转通话状态
	_switchCallState: function(evtJson) {
		console.debug("开始通话转换状态");
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.Exten == this._base._phone.sipNo) {
				if(evtJson.ChannelStatus == "Hangup") {
					this._base._curCallState = this._base._getInvalid();
					this._changeToolBarState(this._base._curCallState);
				} else if(evtJson.ChannelStatus == "Ringing") {
					if(evtJson.LinkedChannel.ChannelType == "normal") {
						this._base._curCallState = this._base._getNormalRinging();
						this._changeToolBarState(this._base._curCallState);
						if(!this._base._phone._isRing) {
							 this._base._phone.playSound();
							 this._base._phone._isRing = true;
						}
					} else if(evtJson.LinkedChannel.ChannelType == "consultation") {
						this._base._curCallState = this._base._getConsultationRinging();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "inner") {
						this._base._curCallState = this._base._getInnerRinging();
						this._changeToolBarState(this._base._curCallState);
						if(!this._base._phone._isRing) {
							 this._base._phone.playSound();
							 this._base._phone._isRing = true;
						}
					} else if(evtJson.LinkedChannel.ChannelType == "dialTransfer") {
						this._base._curCallState = this._base._getNormalRing();
						this._changeToolBarState(this._base._curCallState);
					}
					
					//本版本用
					else if(evtJson.LinkedChannel.ChannelType == "transfer"){
						this._base._curCallState = this._base._getNormalRinging();
						this._changeToolBarState(this._base._curCallState);
						if(!this._base._phone._isRing) {
							 this._base._phone.playSound();
							 this._base._phone._isRing = true;
						}
					}
					
				} else if(evtJson.ChannelStatus == "Ring") {
					if(evtJson.ChannelType == "dialout") {
						this._base._curCallState = this._base._getNormalRing();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.ChannelType == "inner") {
						this._base._curCallState = this._base._getInnerRing();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.ChannelType == "listen") {
						this._base._curCallState = this._base._getListenRing();
						this._changeToolBarState(this._base._curCallState);
					} 
				} else if(evtJson.ChannelStatus == "Link") {
					if(evtJson.LinkedChannel.ChannelType == "normal") {
						this._base._curCallState = this._base._getNormalLink();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "consultation") {
						this._base._curCallState = this._base._getConsultationLink();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "threeWayCall") {
						this._base._curCallState = this._base._getThreeWayCallLink();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "inner") {
						this._base._curCallState = this._base._getInnerLink();
						this._changeToolBarState(this._base._curCallState);
					} else if(evtJson.LinkedChannel.ChannelType == "dialout") {
						this._base._curCallState = this._base._getDialoutLink();
						this._changeToolBarState(this._base._curCallState);
					}

					//旧版本用
					else if(evtJson.LinkedChannel.ChannelType == "transfer") {
						this._base._curCallState = this._base._getNormalLink();
						this._changeToolBarState(this._base._curCallState);
					}

				} else if(evtJson.ChannelStatus == "Up") {
					if(evtJson.ChannelType == "listen") {
						this._base._curCallState = this._base._getListenLink();
						this._changeToolBarState(this._base._curCallState);
					}
				} else if(evtJson.ChannelStatus == "hold") {
					this._base._curCallState = this._base._getHold();
					this._changeToolBarState(this._base._curCallState);
				} 
			}
		} 
		
	},
	
	_publish: function() {
		//console.debug("开始抛事件");
	}
});