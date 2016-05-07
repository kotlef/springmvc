hojo.provide("icallcenter.stateElement.link.consultationLink");

hojo.declare("icallcenter.stateElement.link.consultationLink", null, {
	constructor: function(base) {
		//console.debug("进入到咨询状态");
		this._base = base;
	},
	
	//根状态
	_base: null,
	
	_changeState:function() {
		//console.debug("改变咨询状态");
	},
	
	_switchState:function(evtJson) {
		//console.debug("开始转换状态");
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.ChannelStatus == "Hangup") {
				//console.debug("挂机事件到达");
				this._base._curCallState = this._base._getInvalid();	
			} else if(evtJson.ChannelStatus == "Link") {
				if(evtJson.LinkedChannel.ChannelType == "threeWayCall") {
					this._base._curCallState = this._base._getThreeWayCallLink();
				} else if(evtJson.LinkedChannel.ChannelType == "normal") {
					this._base._curCallState = this._base._getNormalLink();
				} else if(evtJson.LinkedChannel.ChannelType == "dialout") {
					this._base._curCallState = this._base._getDialoutLink();
				} else if(evtJson.LinkedChannel.ChannelType == "inner") {
					this._base._curCallState = this._base._getInnerLink();
				} else if(evtJson.LinkedChannel.ChannelType == "transfer") {
					this._base._bussiness();
				}
			} else if(evtJson.ChannelStatus == "hold") {
				this._base._curCallState = this._base._getHold();
			}
		}
	},

	_publish:function() {
		//console.debug("开始抛事件");
	}

});